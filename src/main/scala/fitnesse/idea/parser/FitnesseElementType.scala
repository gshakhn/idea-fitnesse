package fitnesse.idea.parser

import com.intellij.psi.tree.{IElementType, IStubFileElementType}
import fitnesse.idea.decisiontable.{DecisionInputElementType, DecisionOutputElementType}
import fitnesse.idea.fixtureclass.FixtureClassElementType
import fitnesse.idea.filetype.FitnesseLanguage
import fitnesse.idea.scenariotable.ScenarioNameElementType
import fitnesse.idea.scripttable.ScriptRowElementType
import fitnesse.idea.table.CellElementType

class FitnesseElementType(debugName: String) extends IElementType(debugName, FitnesseLanguage.INSTANCE) {
  override def toString = "Fitnesse:" + this.debugName
}

object FitnesseElementType {
  final val FILE = new IStubFileElementType(FitnesseLanguage.INSTANCE)
  final val TABLE_TYPE = new FitnesseElementType("TABLE_TYPE")
  final val ROW = new FitnesseElementType("ROW")
  final val SCRIPT_ROW = ScriptRowElementType.INSTANCE
  final val FIXTURE_CLASS = FixtureClassElementType.INSTANCE
  final val SCENARIO_NAME = ScenarioNameElementType.INSTANCE
  final val DECISION_INPUT = DecisionInputElementType.INSTANCE
  final val DECISION_OUTPUT = DecisionOutputElementType.INSTANCE
  final val QUERY_OUTPUT = new FitnesseElementType("QUERY_OUTPUT")
  final val IMPORT = new FitnesseElementType("IMPORT")
  final val CELL= CellElementType.INSTANCE
  final val COMMENT = new FitnesseElementType("COMMENT")
  final val COLLAPSIBLE = new FitnesseElementType("COLLAPSIBLE")
  final val WIKI_WORD = new FitnesseElementType("WIKI_WORD")
}

class TableElementType(debugName: String) extends FitnesseElementType(debugName)

object TableElementType {
  final val DECISION_TABLE = new TableElementType("DECISION_TABLE")
  final val QUERY_TABLE = new TableElementType("QUERY_TABLE")
  final val SCRIPT_TABLE = new TableElementType("SCRIPT_TABLE")
  final val TABLE_TABLE = new TableElementType("TABLE_TABLE")
  final val IMPORT_TABLE = new TableElementType("IMPORT_TABLE")
  final val COMMENT_TABLE = new TableElementType("COMMENT_TABLE")
  final val SCENARIO_TABLE = new TableElementType("SCENARIO_TABLE")
  final val LIBRARY_TABLE = new TableElementType("LIBRARY_TABLE")
  final val DEFINE_TABLE_TYPE_TABLE = new TableElementType("DEFINE_TABLE_TYPE_TABLE")
  final val DEFINE_ALIAS_TABLE = new TableElementType("DEFINE_ALIAS_TABLE")
  final val UNKNOWN_TABLE = new TableElementType("UNKNOWN_TABLE")
}
