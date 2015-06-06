package fitnesse.idea.lang.parser

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.{ASTNode, ParserDefinition}
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.TokenSet
import fitnesse.idea.decisiontable.{DecisionInput, DecisionOutput, DecisionTable}
import fitnesse.idea.fixtureclass.FixtureClassImpl
import fitnesse.idea.lang.lexer.{FitnesseLexer, FitnesseTokenType}
import fitnesse.idea.lang.psi._
import fitnesse.idea.querytable.{QueryOutput, QueryTable}
import fitnesse.idea.scripttable.{ScenarioName, ScenarioTable, ScriptRow, ScriptTable}
import fitnesse.idea.wikilink.WikiLink

class FitnesseParserDefinition extends ParserDefinition {
  override def createLexer(project: Project) = new FitnesseLexer

  override def createParser(project: Project) = new FitnesseParser

  override def getFileNodeType = FitnesseElementType.FILE

  override def getWhitespaceTokens = TokenSet.create(FitnesseTokenType.WHITE_SPACE)

  override def getCommentTokens = TokenSet.EMPTY

  override def getStringLiteralElements = TokenSet.EMPTY

  override def createElement(astNode: ASTNode) = {
    astNode.getElementType match {
      case TableElementType.DECISION_TABLE => new DecisionTable(astNode)
      case TableElementType.QUERY_TABLE => new QueryTable(astNode)
      case TableElementType.SCRIPT_TABLE => new ScriptTable(astNode)
      case TableElementType.SCENARIO_TABLE => new ScenarioTable(astNode)
      case FitnesseElementType.ROW => new Row(astNode)
      case FitnesseElementType.SCRIPT_ROW => new ScriptRow(astNode)
      case FitnesseElementType.FIXTURE_CLASS => new FixtureClassImpl(astNode)
      case FitnesseElementType.SCENARIO_NAME => new ScenarioName(astNode)
      case FitnesseElementType.DECISION_INPUT => new DecisionInput(astNode)
      case FitnesseElementType.DECISION_OUTPUT => new DecisionOutput(astNode)
      case FitnesseElementType.QUERY_OUTPUT => new QueryOutput(astNode)
      case _:WikiLinkElementType => new WikiLink(astNode)
      case _ => new ASTWrapperPsiElement(astNode)
    }
  }

  override def createFile(fileViewProvider: FileViewProvider) = new FitnesseFile(fileViewProvider)

  override def spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY
}
