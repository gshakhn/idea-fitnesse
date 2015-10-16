package fitnesse.idea.lang.formatter

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
import fitnesse.idea.lang.parser.ParserSuite

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

  test("should create blocks in tables") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|no| yes |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[TableBlock:Fitnesse:DECISION_TABLE]")
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString == "[LeafBlock:FitnesseTokenType.TABLE_START, TableSubBlock:Fitnesse:ROW, LeafBlock:FitnesseTokenType.ROW_END, TableSubBlock:Fitnesse:ROW, LeafBlock:FitnesseTokenType.ROW_END, TableSubBlock:Fitnesse:ROW, LeafBlock:FitnesseTokenType.TABLE_END]")
    // |no| yes |
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.get(5).getSubBlocks.toString == "[TableSubBlock:Fitnesse:CELL, LeafBlock:FitnesseTokenType.CELL_END, TableSubBlock:Fitnesse:CELL]")
  }

  test("should create blocks for table found in a collapsible section") {
    val parseTree = parseFile("!*** title\n|Should I buy it|\n|have money|buy it?|\n*!\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[FitnesseBlock:Fitnesse:COLLAPSIBLE]")
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString == "[LeafBlock:FitnesseTokenType.COLLAPSIBLE_START, LeafBlock:FitnesseTokenType.WORD, TableBlock:Fitnesse:DECISION_TABLE, LeafBlock:FitnesseTokenType.COLLAPSIBLE_END]")
  }

  test("create width for tables") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|no| yes |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)
    val table = rootBlock.getSubBlocks.get(0).asInstanceOf[TableBlock]
    val formatter = table.tableFormatter

    assert(formatter.rightPadding(0, 0) == 6)
    assert(formatter.rightPadding(1, 0) == 1)
    assert(formatter.rightPadding(1, 1) == 1)
    assert(formatter.rightPadding(2, 0) == 9)
    assert(formatter.rightPadding(2, 1) == 5)
  }
}
