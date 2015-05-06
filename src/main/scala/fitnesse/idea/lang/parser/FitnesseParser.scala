package fitnesse.idea.lang.parser

import com.intellij.lang.PsiBuilder.Marker
import com.intellij.lang.{PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType
import fitnesse.idea.lang.lexer.FitnesseTokenType

class FitnesseParser extends PsiParser {

  override def parse(root: IElementType, builder: PsiBuilder) = {
    val rootMarker = builder.mark()
    while (!builder.eof()) {
      builder.getTokenType match {
        case FitnesseTokenType.TABLE_START => parseTable(builder)
        case FitnesseTokenType.PERIOD => parsePotentialLink(builder, WikiLinkElementType.ABSOLUTE_WIKI_LINK)
        case FitnesseTokenType.GT => parsePotentialLink(builder, WikiLinkElementType.SUBPAGE_WIKI_LINK)
        case FitnesseTokenType.LT => parsePotentialLink(builder, WikiLinkElementType.ANCESTOR_WIKI_LINK)
        case FitnesseTokenType.WIKI_WORD => parseLink(builder, WikiLinkElementType.RELATIVE_WIKI_LINK)()
        case _ => builder.advanceLexer()
      }
    }

    rootMarker.done(root)
    builder.getTreeBuilt
  }

  private def parsePotentialLink(builder: PsiBuilder, linkType: WikiLinkElementType): Unit = {
    val start = builder.mark()
    builder.lookAhead(1) match {
      case FitnesseTokenType.WIKI_WORD => {
        builder.advanceLexer()
        parseLink(builder, linkType)(start)
      }
      case _ => {
        start.drop()
        builder.advanceLexer()
      }
    }
  }

  private def parseLink(builder: PsiBuilder, linkType: WikiLinkElementType)(start: Marker = builder.mark()): Unit = {
    while (!builder.eof() && (builder.getTokenType == FitnesseTokenType.WIKI_WORD || builder.getTokenType == FitnesseTokenType.PERIOD)) {
      builder.advanceLexer()
    }

    start.done(linkType)
  }

  private def parseTable(builder: PsiBuilder): Unit = {
    val start = builder.mark()

    val tableType = findTableType(builder)
    builder.advanceLexer() // Past TABLE_START
    parseTopRow(builder, tableType)

    if (!builder.eof() && builder.getTokenType != FitnesseTokenType.TABLE_END) tableType match {
      case TableElementType.DECISION_TABLE => {
        builder.advanceLexer() // Past ROW_END
        parseDecisionMethodRow(builder)
      }
      case TableElementType.QUERY_TABLE => {
        builder.advanceLexer() // Past ROW_END
        parseQueryMethodRow(builder)
      }
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

  private def findTableType(builder: PsiBuilder) : TableElementType = {
    val start = builder.mark()

    while (!builder.eof() && builder.getTokenType != FitnesseTokenType.TABLE_END) {
      val tableType = if (builder.getTokenType == FitnesseTokenType.TABLE_TYPE) {
        val tableType =  builder.getTokenText.trim.toLowerCase match {
          case "dt" => TableElementType.DECISION_TABLE
          case "ddt" => TableElementType.DECISION_TABLE
          case "decision" => TableElementType.DECISION_TABLE
          case "query" => TableElementType.QUERY_TABLE
          case "subset query" => TableElementType.QUERY_TABLE
          case "ordered query" => TableElementType.QUERY_TABLE
          case "script" => TableElementType.SCRIPT_TABLE
          case "table" => TableElementType.TABLE_TABLE
          case "import" => TableElementType.IMPORT_TABLE
          case "comment" => TableElementType.COMMENT_TABLE
          case "scenario" => TableElementType.SCENARIO_TABLE
          case "library" => TableElementType.LIBRARY_TABLE
          case _ => TableElementType.DECISION_TABLE
        }
        start.rollbackTo()
        return tableType
      }
      builder.advanceLexer()
    }

    start.rollbackTo()
    TableElementType.DECISION_TABLE
  }

  private def parseDecisionMethodRow(builder: PsiBuilder): Unit = {
    val start = builder.mark()

    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      if (builder.getTokenType == FitnesseTokenType.CELL_TEXT) {
        val method = builder.mark()
        val methodType = builder.getTokenText.trim match {
          case output if output.endsWith("?") => FitnesseElementType.DECISION_OUTPUT
          case comment if comment.startsWith("#") => FitnesseElementType.COMMENT
          case _ => FitnesseElementType.DECISION_INPUT
        }
        builder.advanceLexer()
        method.done(methodType)
      } else {
        builder.advanceLexer()
      }
    }

    start.done(FitnesseElementType.ROW)
  }

  private def parseQueryMethodRow(builder: PsiBuilder): Unit = {
    val start = builder.mark()

    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      if (builder.getTokenType == FitnesseTokenType.CELL_TEXT) {
        val method = builder.mark()
        builder.advanceLexer()
        method.done(FitnesseElementType.QUERY_OUTPUT)
      } else {
        builder.advanceLexer()
      }
    }

    start.done(FitnesseElementType.ROW)
  }

  private def parseTopRow(builder: PsiBuilder, tableType: TableElementType): Unit = {
    val start = builder.mark()

    builder.advanceLexer() // Past ROW_START
    builder.advanceLexer() // Past CELL_DELIM
    if (builder.getTokenType == FitnesseTokenType.TABLE_TYPE) {
      builder.advanceLexer() // Past TABLE_TYPE
      builder.advanceLexer() // Past COLON
    }

    if (builder.getTokenType != FitnesseTokenType.LINE_TERMINATOR) {
      val fixtureClassOrScenarioName = builder.mark()
      builder.advanceLexer() // Past FIXTURE_CLASS
      fixtureClassOrScenarioName.done(tableType match {
        case TableElementType.SCENARIO_TABLE => FitnesseElementType.SCENARIO_NAME
        case _ => FitnesseElementType.FIXTURE_CLASS
      })

      advanceTillEndOfRow(builder)
    }
    start.done(FitnesseElementType.ROW)
  }

  private def parseRow(builder: PsiBuilder, tableType: TableElementType): Unit = {
    val start = builder.mark()

    advanceTillEndOfRow(builder)
    start.done(tableType match {
      case TableElementType.SCRIPT_TABLE | TableElementType.SCENARIO_TABLE => FitnesseElementType.SCRIPT_ROW
      case _ => FitnesseElementType.ROW
    })
  }

  private def advanceTillEndOfRow(builder: PsiBuilder): Unit = {
    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      builder.advanceLexer()
    }
  }
}
