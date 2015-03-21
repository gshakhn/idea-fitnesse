package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.lang.{PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType

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

  private def parsePotentialLink(builder: PsiBuilder, linkType: WikiLinkElementType) {
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

  private def parseLink(builder: PsiBuilder, linkType: WikiLinkElementType)(start: Marker = builder.mark()) {
    while (!builder.eof() && (builder.getTokenType == FitnesseTokenType.WIKI_WORD || builder.getTokenType == FitnesseTokenType.PERIOD)) {
      builder.advanceLexer()
    }

    start.done(linkType)
  }

  private def parseTable(builder: PsiBuilder) {
    val start = builder.mark()

    val tableType = findTableType(builder)
    builder.advanceLexer() // Past TABLE_START
    parseTopRow(builder)

    if (!builder.eof() && builder.getTokenType != FitnesseTokenType.TABLE_END && tableType == TableElementType.DECISION_TABLE) {
      builder.advanceLexer() // Past ROW_END
      parseDecisionMethodRow(builder)
    }

    while (!builder.eof() && builder.getTokenType != FitnesseTokenType.TABLE_END) {
      builder.getTokenType match {
        case FitnesseTokenType.ROW_START => parseRow(builder)
        case _ => builder.advanceLexer()
      }
    }

    start.done(tableType)
  }

  private def findTableType(builder: PsiBuilder) : TableElementType = {
    val start = builder.mark()

    while (!builder.eof() && builder.getTokenType != FitnesseTokenType.TABLE_END) {
      if (builder.getTokenType == FitnesseTokenType.TABLE_TYPE) {
        val tableType =  builder.getTokenText.trim.toLowerCase match {
          case "dt" => TableElementType.DECISION_TABLE
          case "ddt" => TableElementType.DECISION_TABLE
          case "decision" => TableElementType.DECISION_TABLE
          case "query" => TableElementType.QUERY_TABLE
          case "subset query" => TableElementType.SUBSET_QUERY_TABLE
          case "ordered query" => TableElementType.ORDERED_QUERY_TABLE
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

  private def parseDecisionMethodRow(builder: PsiBuilder) {
    val start = builder.mark()

    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      if (builder.getTokenType == FitnesseTokenType.CELL_TEXT) {
        val method = builder.mark()
        val methodType = if (builder.getTokenText.trim.endsWith("?")) FitnesseElementType.DECISION_OUTPUT else FitnesseElementType.DECISION_INPUT
        builder.advanceLexer()
        method.done(methodType)
      } else {
        builder.advanceLexer()
      }
    }

    start.done(FitnesseElementType.ROW)
  }

  private def parseTopRow(builder: PsiBuilder) {
    val start = builder.mark()

    builder.advanceLexer() // Past ROW_START
    builder.advanceLexer() // Past CELL_DELIM
    if (builder.getTokenType == FitnesseTokenType.TABLE_TYPE) {
      builder.advanceLexer() // Past TABLE_TYPE
      builder.advanceLexer() // Past COLON
    }

    val fixtureClass = builder.mark()
    builder.advanceLexer() // Past FIXTURE_CLASS
    fixtureClass.done(FitnesseElementType.FIXTURE_CLASS)

    parseRestOfRow(builder, start)
  }

  private def parseRow(builder: PsiBuilder) {
    val start = builder.mark()

    parseRestOfRow(builder, start)
  }

  private def parseRestOfRow(builder: PsiBuilder, start: Marker) {
    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      builder.advanceLexer()
    }

    start.done(FitnesseElementType.ROW)
  }
}
