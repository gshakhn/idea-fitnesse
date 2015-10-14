package fitnesse.idea.lang.formatter

import com.intellij.formatting.{FormatTextRanges, FormatterImpl}
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.IndentOptions
import com.intellij.psi.impl.source.tree.FileElement
import fitnesse.idea.lang.parser.ParserSuite

class FitnesseBlockTest extends ParserSuite {

  val codeStyleSettings: CodeStyleSettings = null
  val indentOptions: IndentOptions = null

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
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString == "[LeafBlock:FitnesseTokenType.TABLE_START, TableBlock:Fitnesse:ROW, LeafBlock:FitnesseTokenType.ROW_END, TableBlock:Fitnesse:ROW, LeafBlock:FitnesseTokenType.ROW_END, TableBlock:Fitnesse:ROW, LeafBlock:FitnesseTokenType.TABLE_END]")
    // |no| yes |
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.get(5).getSubBlocks.toString == "[TableBlock:Fitnesse:CELL, LeafBlock:FitnesseTokenType.CELL_END, TableBlock:Fitnesse:CELL]")
  }

  test("should create blocks for table found in a collapsible section") {
    val parseTree = parseFile("!*** title\n|Should I buy it|\n|have money|buy it?|\n*!\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.toString == "[FitnesseBlock:Fitnesse:COLLAPSIBLE]")
    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.toString == "[LeafBlock:FitnesseTokenType.COLLAPSIBLE_START, LeafBlock:FitnesseTokenType.WORD, TableBlock:Fitnesse:DECISION_TABLE, LeafBlock:FitnesseTokenType.COLLAPSIBLE_END]")
  }

  ignore("find all row and cell endings, since those are the elements we have to align") {
    val parseTree = parseFile("|Should I buy it|\n|have money|   buy it?    |\n|yes|yes|")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.size == 4, rootBlock.getSubBlocks.get(0).getSubBlocks)
  }

  test("create width for tables") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|no| yes |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)
    val table = rootBlock.getSubBlocks.get(0).asInstanceOf[TableBlock]
    val widthMatrix = table.tableBlocks.map(row => row.tableBlocks.map(cell => cell.width))

    assert(widthMatrix == List(
      List("Should I buy it".length),
      List("have money".length, "buy it?".length),
      List("no".length, "yes".length)))
  }

//  test("create width model for tables") {
//    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|no| yes |\n")
//
//    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)
//    val table = rootBlock.getSubBlocks.get(0).asInstanceOf[TableBlock]
//
//    // Collect all column widths for the whole table
//    val widthMatrix = table.tableBlocks.map(row => row.tableBlocks.map(cell => cell.width))
//
//    // Make all rows have the same size
//    val nColumns: Int = widthMatrix.map(_.size).max
//    val nonColspanWidthMatrix = widthMatrix.map(row => if (row.size < nColumns) row.dropRight(1) ::: List.fill(nColumns - row.size + 1)(0) else row)
//    val maxWidths = nonColspanWidthMatrix.transpose.map(_.max)
//    val colSpans = widthMatrix.map(row => if (row.size < nColumns) Some((row.size, row.last)) else None)
//
//    println(widthMatrix)
//    println(nonColspanWidthMatrix)
//    println(nonColspanWidthMatrix.transpose)
//    println(maxWidths)
//
//    // calculate max width except for last cell of colspan cells
//    // calculate column widths
//    // calculate max row width
//    // align to
//
//    /*
//      /*
//       * This is where the nastiness starts due to trying to emulate
//       * the html rendering of colspans.
//       *   - make a row/column matrix that contains data lengths
//       *   - find the max widths of those columns that don't have colspans
//       *   - update the matrix to set each non colspan column to those max widths
//       *   - find the max widths of the colspan columns
//       *   - increase the non colspan columns if the colspan columns lengths are greater
//       *   - adjust colspan columns to pad out to the max length of the row
//       *
//       * Feel free to refator as necessary for clarity
//       */
//      this.calculateColumnWidths = function(rows) {
//        var widths = this.getRealColumnWidths(rows);
//        var totalNumberOfColumns = this.getNumberOfColumns(rows);
//
//        var maxWidths = this.getMaxWidths(widths, totalNumberOfColumns);
//        this.setMaxWidthsOnNonColspanColumns(widths, maxWidths);
//
//        var colspanWidths = this.getColspanWidth(widths, totalNumberOfColumns);
//        this.adjustWidthsForColspans(widths, maxWidths, colspanWidths);
//
//        this.adjustColspansForWidths(widths, maxWidths);
//
//        return widths;
//      };
//    */
//  }
//

//  test("verify file parsing with com.intellij.formatting.InitialInfoBuilder") {
//    val builder: FitnesseFormattingModelBuilder = new FitnesseFormattingModelBuilder()
//    val psiTree = parsePsiFile("!*** title\n|Should I buy it|\n|have money|buy it?|\n*!\n")
//    new FormatterImpl().format(builder.createModel(psiTree, codeStyleSettings), codeStyleSettings, indentOptions, new FormatTextRanges())
//  }
}
