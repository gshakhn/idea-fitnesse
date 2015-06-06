package fitnesse.idea.lang.parser

import com.intellij.psi.tree.{IStubFileElementType, IElementType, IFileElementType}
import fitnesse.idea.fixtureclass.FixtureClassElementTypeHolder
import fitnesse.idea.lang.FitnesseLanguage

class FitnesseElementType(debugName: String) extends IElementType(debugName, FitnesseLanguage.INSTANCE) {
  override def toString = "Fitnesse:" + this.debugName
}

object FitnesseElementType {
  final val FILE = new IStubFileElementType(FitnesseLanguage.INSTANCE)
  final val ROW = new FitnesseElementType("ROW")
  final val SCRIPT_ROW = new FitnesseElementType("SCRIPT_ROW")
  final val TABLE_TYPE = new FitnesseElementType("TABLE_TYPE")
  final val FIXTURE_CLASS = FixtureClassElementTypeHolder.INSTANCE
  final val SCENARIO_NAME = new FitnesseElementType("SCENARIO_NAME")
  final val DECISION_INPUT = new FitnesseElementType("DECISION_INPUT")
  final val DECISION_OUTPUT = new FitnesseElementType("DECISION_OUTPUT")
  final val QUERY_OUTPUT = new FitnesseElementType("QUERY_OUTPUT")
  final val CELL= new FitnesseElementType("CELL")
  final val COMMENT = new FitnesseElementType("COMMENT")
}

class TableElementType(debugName: String) extends FitnesseElementType(debugName) {

}

object TableElementType {
  final val DECISION_TABLE = new TableElementType("DECISION_TABLE")
  final val QUERY_TABLE = new TableElementType("QUERY_TABLE")
  final val SCRIPT_TABLE = new TableElementType("SCRIPT_TABLE")
  final val TABLE_TABLE = new TableElementType("TABLE_TABLE")
  final val IMPORT_TABLE = new TableElementType("IMPORT_TABLE")
  final val COMMENT_TABLE = new TableElementType("COMMENT_TABLE")
  final val SCENARIO_TABLE = new TableElementType("SCENARIO_TABLE")
  final val LIBRARY_TABLE = new TableElementType("LIBRARY_TABLE")
  final val UNKNOWN_TABLE = new TableElementType("UNKNOWN_TABLE")
  final val DEFINE_TABLE_TYPE_TABLE = new TableElementType("DEFINE_TABLE_TYPE_TABLE")
  final val DEFINE_ALIAS_TABLE = new TableElementType("DEFINE_ALIAS_TABLE")
}

class WikiLinkElementType(debugName: String) extends FitnesseElementType(debugName) {

}

object WikiLinkElementType {
  final val RELATIVE_WIKI_LINK = new WikiLinkElementType("RELATIVE_WIKI_LINK")
  final val ABSOLUTE_WIKI_LINK = new WikiLinkElementType("ABSOLUTE_WIKI_LINK")
  final val SUBPAGE_WIKI_LINK = new WikiLinkElementType("SUPPAGE_WIKI_LINK")
  final val ANCESTOR_WIKI_LINK = new WikiLinkElementType("ANCESTOR_WIKI_LINK")
}