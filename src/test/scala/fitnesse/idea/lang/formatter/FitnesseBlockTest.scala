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

    assert(rootBlock.getSubBlocks.size == 1)
  }

  test("should create blocks for multiple tables found in a file") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|yes|yes|\n\n| script |\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.size == 3)
  }

  ignore("should create blocks for table found in a collapsible section") {
    val parseTree = parseFile("!*** title\n|Should I buy it|\n|have money|buy it?|\n*!\n")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.size == 1)
  }

  ignore("find all row and cell endings, since those are the elements we have to align") {
    val parseTree = parseFile("|Should I buy it|\n|have money|buy it?|\n|yes|yes|")

    val rootBlock: FitnesseBlock = new FitnesseBlock(parseTree)

    assert(rootBlock.getSubBlocks.get(0).getSubBlocks.size == 4, rootBlock.getSubBlocks.get(0).getSubBlocks)
  }

//  test("verify file parsing with com.intellij.formatting.InitialInfoBuilder") {
//    val builder: FitnesseFormattingModelBuilder = new FitnesseFormattingModelBuilder()
//    val psiTree = parsePsiFile("!*** title\n|Should I buy it|\n|have money|buy it?|\n*!\n")
//    new FormatterImpl().format(builder.createModel(psiTree, codeStyleSettings), codeStyleSettings, indentOptions, new FormatTextRanges())
//  }
}
