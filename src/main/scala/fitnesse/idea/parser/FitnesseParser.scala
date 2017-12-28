package fitnesse.idea.parser

import com.intellij.lang.{ASTNode, PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType
import fitnesse.idea.lexer.FitnesseTokenType

class FitnesseParser extends PsiParser {


  override def parse(root: IElementType, builder: PsiBuilder): ASTNode = {
    val rootMarker = builder.mark()
    while (!builder.eof()) {
      processToken(builder)
    }

    rootMarker.done(root)
    builder.getTreeBuilt
  }

  def processToken(builder: PsiBuilder): Unit = {
    builder.getTokenType match {
      case FitnesseTokenType.TABLE_START => parseTable(builder)
      case FitnesseTokenType.COLLAPSIBLE_START => parseCollapsible(builder)
      case FitnesseTokenType.WIKI_WORD => parseWikiWord(builder)
      case _ => builder.advanceLexer()
    }
  }

  private def parseTable(builder: PsiBuilder): Unit = {
    val start = builder.mark()

    val tableType = parseTopRow(builder)

    tableType match {
      case TableElementType.DECISION_TABLE =>
        parseDecisionMethodRow(builder)
      case _ =>
    }

    while (!isTableEnd(builder)) {
      parseRow(builder, tableType)
    }

    assert(builder.getTokenType == FitnesseTokenType.TABLE_END, s"${builder.getTokenType} != FitnesseTokenType.TABLE_END")

    builder.advanceLexer() // Past TABLE_END

    start.done(tableType)
  }

  private def parseDecisionMethodRow(builder: PsiBuilder): Unit = {
    if  (!isTableEnd(builder)) {
      assert(builder.getTokenType == FitnesseTokenType.ROW_END, s"${builder.getTokenType} != FitnesseTokenType.ROW_END")
      builder.advanceLexer() // Past ROW_END

      val start = builder.mark()

      while (!isRowEnd(builder)) {
        if (builder.getTokenType == FitnesseTokenType.WORD) {
          val method = builder.mark()
          val methodType = readCellText(builder) match {
            case output if output.endsWith("?") => FitnesseElementType.DECISION_OUTPUT
            case comment if comment.startsWith("#") => FitnesseElementType.COMMENT
            case _ => FitnesseElementType.DECISION_INPUT
          }
          method.done(methodType)
        }
        if (!isRowEnd(builder)) {
          builder.advanceLexer()
        }
      }
      start.done(FitnesseElementType.ROW)
    }
  }

  private def parseTopRow(builder: PsiBuilder) : TableElementType = {
    assert(builder.getTokenType == FitnesseTokenType.TABLE_START, s"${builder.getTokenType} != FitnesseTokenType.TABLE_START")
    builder.advanceLexer() // Past TABLE_START

    val start = builder.mark()

    val tableType = findTableType(builder)

    tableType match {
      case TableElementType.SCENARIO_TABLE =>
        val scenarioName = builder.mark()
        parseCells(builder, TableElementType.SCENARIO_TABLE)
        scenarioName.done(FitnesseElementType.SCENARIO_NAME)
      case _ =>
        skipWhitespace(builder)
        if (!isCellEnd(builder)) {
          val fixtureClass = builder.mark()
          while (!isCellEnd(builder)) builder.advanceLexer()
          fixtureClass.done(FitnesseElementType.FIXTURE_CLASS)
        }
        if (!isRowEnd(builder)) {
          builder.advanceLexer()
          parseCells(builder, TableElementType.UNKNOWN_TABLE)
        }
    }
    start.done(FitnesseElementType.ROW)

    tableType
  }

  private def findTableType(builder: PsiBuilder) : TableElementType = {
    val tableType = builder.mark()

    def innerFindTableType(tableName: String): TableElementType = builder.getTokenType match {
      case FitnesseTokenType.COLON =>
        tableType.done(FitnesseElementType.TABLE_TYPE)
        builder.advanceLexer() // Past COLON
        toTableType(tableName)
      case FitnesseTokenType.CELL_END | FitnesseTokenType.ROW_END | FitnesseTokenType.TABLE_END =>
        toTableType(tableName) match {
          case TableElementType.UNKNOWN_TABLE =>
            tableType.rollbackTo()
            TableElementType.DECISION_TABLE
          case tableElementType =>
            tableType.done(FitnesseElementType.TABLE_TYPE)
            if (builder.getTokenType == FitnesseTokenType.CELL_END) {
              builder.advanceLexer() // Pass cell end
            }
            tableElementType
        }
      case _ =>
        val tokenText = builder.getTokenText
        builder.advanceLexer()
        innerFindTableType(tableName + " " + tokenText) // start next iteration
    }

    innerFindTableType("")
  }

  private def toTableType(tableName: String): TableElementType = tableName.toLowerCase.trim match {
    case "dt" => TableElementType.DECISION_TABLE
    case "ddt" => TableElementType.DECISION_TABLE
    case "decision" => TableElementType.DECISION_TABLE
    case "dynamic decision" => TableElementType.DECISION_TABLE
    case "query" => TableElementType.QUERY_TABLE
    case "subset query" => TableElementType.QUERY_TABLE
    case "ordered query" => TableElementType.QUERY_TABLE
    case "script" => TableElementType.SCRIPT_TABLE
    case "storyboard" => TableElementType.SCRIPT_TABLE
    case "import" => TableElementType.IMPORT_TABLE
    case "library" => TableElementType.LIBRARY_TABLE
    case "table" => TableElementType.TABLE_TABLE
    case "comment" => TableElementType.COMMENT_TABLE
    case "scenario" => TableElementType.SCENARIO_TABLE
    case "define table type" => TableElementType.DEFINE_TABLE_TYPE_TABLE
    case "define alias" => TableElementType.DEFINE_ALIAS_TABLE
    case _ => TableElementType.UNKNOWN_TABLE
  }

  private def parseRow(builder: PsiBuilder, tableType: TableElementType): Unit = {
    if (!isTableEnd(builder)) {
      assert(builder.getTokenType == FitnesseTokenType.ROW_END, s"${builder.getTokenType} != FitnesseTokenType.ROW_END")
      builder.advanceLexer() // Past ROW_END

      val row = builder.mark()
      val cellText = parseCells(builder, tableType)

      row.done(tableType match {
        case TableElementType.SCRIPT_TABLE | TableElementType.SCENARIO_TABLE =>
          // if first cell is "start", remaining cells form a FixtureClass
          scriptRowType(cellText)
        case _ => FitnesseElementType.ROW
      })
    }
  }

  def parseCells(builder: PsiBuilder, tableType: TableElementType): String = {
    var firstCell = true
    var firstCellText = ""
    while (!isRowEnd(builder)) {
      if (builder.getTokenType == FitnesseTokenType.WORD) {
        val cell = builder.mark()
        val cellText = readCellText(builder)
        if (firstCell) {
          firstCellText = cellText
        }
        if (cellText != "") {
          cell.done(tableType match {
            case TableElementType.QUERY_TABLE => FitnesseElementType.QUERY_OUTPUT
            case TableElementType.IMPORT_TABLE => FitnesseElementType.IMPORT
            case TableElementType.LIBRARY_TABLE if firstCell => FitnesseElementType.FIXTURE_CLASS
            case _ => FitnesseElementType.CELL
          })
        } else {
          cell.drop()
        }
        firstCell = false
      }
      if (!isRowEnd(builder)) {
        builder.advanceLexer()
      }
    }
    firstCellText
  }

  private def readCellText(builder: PsiBuilder): String = {
    var cellText = ""
    while (!isCellEnd(builder)) {
      cellText = cellText + builder.getTokenText
      builder.advanceLexer()
    }
    cellText
  }

  private def isTableEnd(builder: PsiBuilder): Boolean = {
    val tokenType = builder.getTokenType
    builder.eof() || tokenType == FitnesseTokenType.TABLE_END
  }

  private def isRowEnd(builder: PsiBuilder): Boolean = {
    val tokenType = builder.getTokenType
    builder.eof() || tokenType == FitnesseTokenType.ROW_END || tokenType == FitnesseTokenType.TABLE_END
  }

  private def isCellEnd(builder: PsiBuilder): Boolean = {
    val tokenType = builder.getTokenType
    builder.eof() || tokenType == FitnesseTokenType.CELL_END || tokenType == FitnesseTokenType.ROW_END || tokenType == FitnesseTokenType.TABLE_END
  }

  def scriptRowType(cellText: String): IElementType = cellText match {
    case "note" | "#" | "*" | "" => FitnesseElementType.ROW
    case _ => FitnesseElementType.SCRIPT_ROW
  }

  def skipWhitespace(builder: PsiBuilder) =
    while(!builder.eof() && builder.getTokenType == FitnesseTokenType.WHITE_SPACE) {
      builder.advanceLexer()
    }


  private def advanceTillEndOfRow(builder: PsiBuilder): Unit =
    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      builder.advanceLexer()
    }

  private def parseCollapsible(builder: PsiBuilder): Unit = {
    val start = builder.mark()

    builder.advanceLexer()
    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.COLLAPSIBLE_END) {
      processToken(builder)
    }

    builder.advanceLexer() // Past COLLAPSIBLE_END

    start.done(FitnesseElementType.COLLAPSIBLE)
  }

  def parseWikiWord(builder: PsiBuilder): Unit = {
    val start = builder.mark()
    builder.advanceLexer()
    start.done(FitnesseElementType.WIKI_WORD)
  }
}
