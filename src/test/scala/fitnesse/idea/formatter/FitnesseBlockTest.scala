package fitnesse.idea.formatter

import com.intellij.formatting.{FormatTextRanges, FormatterImpl}
import com.intellij.lang.Language
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.mock.MockPsiDocumentManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.{DumbServiceImpl, DumbService}
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.{CommonCodeStyleSettings, CodeStyleSettingsManager, ProjectCodeStyleSettingsManager, CodeStyleSettings}
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.IndentOptions
import com.intellij.psi.impl.source.tree.FileElement
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageManagerImpl
import fitnesse.idea.parser.ParserSuite

class FitnesseBlockTest extends ParserSuite {

  test("should create blocks for table found in a file") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|yes|yes|")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[TableBlock:Fitnesse:DECISION_TABLE]")
  }

  test("should create blocks for multiple tables found in a file") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|yes|yes|\n\n| script |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[TableBlock:Fitnesse:DECISION_TABLE, TableBlock:Fitnesse:SCRIPT_TABLE]")
  }

  test("should create blocks for decision table") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|no| yes |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[TableBlock:Fitnesse:DECISION_TABLE]")
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString ==
      "[BarBlock:FitnesseTokenType.TABLE_START, CellBlock:FIXTURE_CLASS(0,0), BarBlock:FitnesseTokenType.ROW_END," +
      " CellBlock:DECISION_INPUT(1,0), BarBlock:FitnesseTokenType.CELL_END, CellBlock:DECISION_OUTPUT(1,1), BarBlock:FitnesseTokenType.ROW_END," +
      " CellBlock:Fitnesse:CELL(2,0), BarBlock:FitnesseTokenType.CELL_END, CellBlock:Fitnesse:CELL(2,1), BarBlock:FitnesseTokenType.TABLE_END]")
  }

  test("should create blocks for scenario table") {
    val parseTree = parseFile("|scenario | Should | something | happen|\n|do good |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[TableBlock:Fitnesse:SCENARIO_TABLE]")
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString ==
      "[BarBlock:FitnesseTokenType.TABLE_START, CellBlock:Fitnesse:TABLE_TYPE(0,0), BarBlock:FitnesseTokenType.CELL_END," +
        " CellBlock:Fitnesse:CELL(0,1), BarBlock:FitnesseTokenType.CELL_END," +
        " CellBlock:Fitnesse:CELL(0,2), BarBlock:FitnesseTokenType.CELL_END," +
        " CellBlock:Fitnesse:CELL(0,3), BarBlock:FitnesseTokenType.ROW_END," +
      " CellBlock:Fitnesse:CELL(1,0), BarBlock:FitnesseTokenType.TABLE_END]")
  }

  test("should create blocks for table found in a collapsible section") {
    val parseTree = parseFile("!*** title\n|Should I buy it|\n|have money|buy it?|\n*!\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[FitnesseBlock:Fitnesse:COLLAPSIBLE]")
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString == "[LeafBlock:FitnesseTokenType.COLLAPSIBLE_START, LeafBlock:FitnesseTokenType.WORD, TableBlock:Fitnesse:DECISION_TABLE, LeafBlock:FitnesseTokenType.COLLAPSIBLE_END]")
  }

  test("should create blocks for decision table with empty cells") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n| | yes |\n|no|  |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[TableBlock:Fitnesse:DECISION_TABLE]")
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString ==
      "[BarBlock:FitnesseTokenType.TABLE_START, CellBlock:FIXTURE_CLASS(0,0), BarBlock:FitnesseTokenType.ROW_END," +
        " CellBlock:DECISION_INPUT(1,0), BarBlock:FitnesseTokenType.CELL_END, CellBlock:DECISION_OUTPUT(1,1), BarBlock:FitnesseTokenType.ROW_END," +
        " EmptyCellBarBlock:FitnesseTokenType.CELL_END(2,0), CellBlock:Fitnesse:CELL(2,1), BarBlock:FitnesseTokenType.ROW_END," +
        " CellBlock:Fitnesse:CELL(3,0), BarBlock:FitnesseTokenType.CELL_END, EmptyCellBarBlock:FitnesseTokenType.TABLE_END(3,1)]")
  }

  test("should create blocks for decision table with all empty cells") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n| |  |\n| |  |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[TableBlock:Fitnesse:DECISION_TABLE]")
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString ==
      "[BarBlock:FitnesseTokenType.TABLE_START, CellBlock:FIXTURE_CLASS(0,0), BarBlock:FitnesseTokenType.ROW_END," +
        " CellBlock:DECISION_INPUT(1,0), BarBlock:FitnesseTokenType.CELL_END, CellBlock:DECISION_OUTPUT(1,1), BarBlock:FitnesseTokenType.ROW_END," +
        " EmptyCellBarBlock:FitnesseTokenType.CELL_END(2,0), EmptyCellBarBlock:FitnesseTokenType.ROW_END(2,1)," +
        " EmptyCellBarBlock:FitnesseTokenType.CELL_END(3,0), EmptyCellBarBlock:FitnesseTokenType.TABLE_END(3,1)]")
  }

  test("groupPrefix") {
    assertResult(List(List(1,2,3), List(4,5))) {
      TableBlock.groupPrefix(List(1, 2, 3, 4, 5))(_ == 3)
    }
  }

  test("create width for tables") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|no| yes |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)
    val table = rootBlock.getSubBlocks.get(0).asInstanceOf[TableBlock]
    println(table.cellBlocks)
    val formatter = table.tableFormatter

    assert(formatter.rightPadding(0, 0) == 3)
    assert(formatter.rightPadding(1, 0) == 0)
    assert(formatter.rightPadding(1, 1) == 0)
    assert(formatter.rightPadding(2, 0) == 8)
    assert(formatter.rightPadding(2, 1) == 4)
  }

  test("create width for escaped tables") {
    val parseTree = parseFile("!|Should I buy it|\n|have money|buy it?|\n|no| yes |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)
    val table = rootBlock.getSubBlocks.get(0).asInstanceOf[TableBlock]
    println(table.cellBlocks)
    val formatter = table.tableFormatter

    assert(formatter.rightPadding(0, 0) == 2)
    assert(formatter.rightPadding(1, 0) == 0)
    assert(formatter.rightPadding(1, 1) == 0)
    assert(formatter.rightPadding(2, 0) == 8)
    assert(formatter.rightPadding(2, 1) == 4)
  }

  test("create width for table with empty cell") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n| | yes |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)
    val table = rootBlock.getSubBlocks.get(0).asInstanceOf[TableBlock]
    println(table.cellBlocks)
    val formatter = table.tableFormatter

    assert(formatter.rightPadding(0, 0) == 3)
    assert(formatter.rightPadding(1, 0) == 0)
    assert(formatter.rightPadding(1, 1) == 0)
    assert(formatter.rightPadding(2, 0) == 10)
    assert(formatter.rightPadding(2, 1) == 4)
  }

  test("create width for table with empty row") {
    val parseTree = parseFile("|Response Examiner|\n|wrapped html?|\n||")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)
    val table = rootBlock.getSubBlocks.get(0).asInstanceOf[TableBlock]
    println(table.cellBlocks)
    val formatter = table.tableFormatter

    assert(formatter.rightPadding(0, 0) == 0)
    assert(formatter.rightPadding(1, 0) == 4)
    assert(formatter.rightPadding(2, 0) == 17)
  }


}
