package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.intellij.psi.tree.{IElementType, IFileElementType}
import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage

class FitnesseElementType(debugName: String) extends IElementType(debugName, FitnesseLanguage.INSTANCE) {
  override def toString = "Fitnesse:" + this.debugName
}

object FitnesseElementType {
  final val FILE = new IFileElementType(FitnesseLanguage.INSTANCE)
  final val ROW = new FitnesseElementType("ROW")
  final val FIXTURE_CLASS = new FitnesseElementType("FIXTURE_CLASS")
}

class TableElementType(debugName: String) extends FitnesseElementType(debugName) {

}

object TableElementType {
  final val DECISION_TABLE = new TableElementType("DECISION_TABLE")
  final val QUERY_TABLE = new TableElementType("QUERY_TABLE")
  final val SUBSET_QUERY_TABLE = new TableElementType("SUBSET_QUERY_TABLE")
  final val ORDERED_QUERY_TABLE = new TableElementType("ORDERED_QUERY_TABLE")
  final val SCRIPT_TABLE = new TableElementType("SCRIPT_TABLE")
  final val TABLE_TABLE = new TableElementType("TABLE_TABLE")
  final val IMPORT_TABLE = new TableElementType("IMPORT_TABLE")
  final val COMMENT_TABLE = new TableElementType("COMMENT_TABLE")
  final val SCENARIO_TABLE = new TableElementType("SCENARIO_TABLE")
  final val LIBRARY_TABLE = new TableElementType("LIBRARY_TABLE")
  final val UNKNOWN_TABLE = new TableElementType("UNKNOWN_TABLE")
}

class WikiLinkElementType(debugName: String) extends FitnesseElementType(debugName) {

}

object WikiLinkElementType {
  final val RELATIVE_WIKI_LINK = new WikiLinkElementType("RELATIVE_WIKI_LINK")
  final val ABSOLUTE_WIKI_LINK = new WikiLinkElementType("ABSOLUTE_WIKI_LINK")
  final val SUBPAGE_WIKI_LINK = new WikiLinkElementType("SUPPAGE_WIKI_LINK")
  final val ANCESTOR_WIKI_LINK = new WikiLinkElementType("ANCESTOR_WIKI_LINK")
}