package fitnesse.idea.lang.parser

import com.intellij.lang.{PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType
import fitnesse.idea.lang.lexer.FitnesseTokenType

class FitnesseParser extends PsiParser {

  override def parse(root: IElementType, builder: PsiBuilder) = {
    val rootMarker = builder.mark()
    while (!builder.eof()) {
      builder.getTokenType match {
        case FitnesseTokenType.TABLE_START => parseTable(builder)
        case _ => builder.advanceLexer()
      }
    }

    rootMarker.done(root)
    builder.getTreeBuilt
  }

  private def parseTable(builder: PsiBuilder) {
    val start = builder.mark()

    assert(builder.getTokenType == FitnesseTokenType.TABLE_START)
    builder.advanceLexer() // Past TABLE_START

    val tableType = parseTopRow(builder)

    tableType match {
      case TableElementType.DECISION_TABLE =>
        parseDecisionMethodRow(builder)
      case _ =>
    }

    while (!builder.eof() && builder.getTokenType != FitnesseTokenType.TABLE_END) {
      builder.getTokenType match {
        case FitnesseTokenType.ROW_START => parseRow(builder, tableType)
        case _ => builder.advanceLexer()
      }
    }

    start.done(tableType)
  }

  private def parseDecisionMethodRow(builder: PsiBuilder) {
    if  (builder.getTokenType == FitnesseTokenType.TABLE_END) return

    val start = builder.mark()

    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      if (builder.getTokenType == FitnesseTokenType.WORD) {
        val method = builder.mark()
        val methodType = readCellText(builder) match {
          case output if output.endsWith("?") => FitnesseElementType.DECISION_OUTPUT
          case comment if comment.startsWith("#") => FitnesseElementType.COMMENT
          case _ => FitnesseElementType.DECISION_INPUT
        }
        method.done(methodType)
      }
      builder.advanceLexer()
    }

    start.done(FitnesseElementType.ROW)
  }

  private def parseTopRow(builder: PsiBuilder) : TableElementType = {
    val start = builder.mark()

    assert(builder.getTokenType == FitnesseTokenType.ROW_START)
    builder.advanceLexer() // Past ROW_START

    assert(builder.getTokenType == FitnesseTokenType.CELL_START)
    builder.advanceLexer() // Past CELL_START

    val tableType = findTableType(builder)

    if (!isCellEnd(builder)) {
      val fixtureClassOrScenarioName = builder.mark()
      while (!isCellEnd(builder)) builder.advanceLexer() // Past FIXTURE_CLASS
      fixtureClassOrScenarioName.done(tableType match {
        case TableElementType.SCENARIO_TABLE => FitnesseElementType.SCENARIO_NAME
        case _ => FitnesseElementType.FIXTURE_CLASS
      })
    }

    advanceTillEndOfRow(builder)

    start.done(FitnesseElementType.ROW)

    assert(builder.getTokenType == FitnesseTokenType.ROW_END)
    builder.advanceLexer() // Past ROW_END

    tableType
  }

  private def findTableType(builder: PsiBuilder) : TableElementType = {
    val tableType = builder.mark()
    var tableName = ""
    while (!isCellEnd(builder)) {
      if (builder.getTokenType == FitnesseTokenType.COLON) {
        tableType.done(FitnesseElementType.TABLE_TYPE)
        builder.advanceLexer() // Past COLON
        return tableName.toLowerCase.trim match {
          case "dt" => TableElementType.DECISION_TABLE
          case "ddt" => TableElementType.DECISION_TABLE
          case "decision" => TableElementType.DECISION_TABLE
          case "dynamic decision" => TableElementType.DECISION_TABLE
          case "query" => TableElementType.QUERY_TABLE
          case "subset query" => TableElementType.QUERY_TABLE
          case "ordered query" => TableElementType.QUERY_TABLE
          case "script" => TableElementType.SCRIPT_TABLE
          case "table" => TableElementType.TABLE_TABLE
          case "import" => TableElementType.IMPORT_TABLE
          case "comment" => TableElementType.COMMENT_TABLE
          case "scenario" => TableElementType.SCENARIO_TABLE
          case "library" => TableElementType.LIBRARY_TABLE
          case "define table type" => TableElementType.DEFINE_TABLE_TYPE_TABLE
          case "define alias" => TableElementType.DEFINE_ALIAS_TABLE
          case _ => TableElementType.UNKNOWN_TABLE
        }
      } else {
        tableName = tableName + " " + builder.getTokenText
        builder.advanceLexer()
      }
    }

    tableName.toLowerCase.trim match {
      case "script" =>
        tableType.done(FitnesseElementType.TABLE_TYPE)
        builder.advanceLexer() // Pass cell end
        TableElementType.SCRIPT_TABLE
      case "scenario" =>
        tableType.done(FitnesseElementType.TABLE_TYPE)
        builder.advanceLexer() // Pass cell end
        TableElementType.SCENARIO_TABLE
      case _ =>
        tableType.rollbackTo()
        TableElementType.DECISION_TABLE
    }
  }

  private def parseRow(builder: PsiBuilder, tableType: TableElementType) {
    if  (builder.getTokenType == FitnesseTokenType.TABLE_END) return

    val start = builder.mark()

    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      if (builder.getTokenType == FitnesseTokenType.WORD) {
        val cell = builder.mark()
        readCellText(builder)
        cell.done(tableType match {
          case TableElementType.QUERY_TABLE => FitnesseElementType.QUERY_OUTPUT
          case _ => FitnesseElementType.CELL
        })
      }
      builder.advanceLexer()
    }

    start.done(tableType match {
      case TableElementType.SCRIPT_TABLE | TableElementType.SCENARIO_TABLE => FitnesseElementType.SCRIPT_ROW
      case _ => FitnesseElementType.ROW
    })
  }

  private def readCellText(builder: PsiBuilder): String = {
    var s = ""
    while (!isCellEnd(builder)) {
      s = s + builder.getTokenText
      builder.advanceLexer()
    }
    s
  }

  private def isCellEnd(builder: PsiBuilder): Boolean = {
    val tokenType = builder.getTokenType
    return tokenType == FitnesseTokenType.CELL_END || tokenType == FitnesseTokenType.ROW_END || tokenType == FitnesseTokenType.TABLE_END
  }

  private def advanceTillEndOfRow(builder: PsiBuilder) {
    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      builder.advanceLexer()
    }
  }
}
