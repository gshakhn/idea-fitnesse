package fitnesse.idea.lang.parser

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.{ASTNode, ParserDefinition}
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.TokenSet
import fitnesse.idea.decisiontable._
import fitnesse.idea.fixtureclass.FixtureClassImpl
import fitnesse.idea.importtable.Import
import fitnesse.idea.lang.lexer.{FitnesseLexer, FitnesseTokenType}
import fitnesse.idea.lang.psi._
import fitnesse.idea.librarytable.LibraryClass
import fitnesse.idea.querytable.{QueryOutput, QueryTable}
import fitnesse.idea.scripttable._
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
      case _: TableElementType => new Table(astNode)
      case FitnesseElementType.ROW => new SimpleRow(astNode)
      case FitnesseElementType.CELL => new SimpleCell(astNode)
      case FitnesseElementType.SCRIPT_ROW => ScriptRowImpl(astNode)
      case FitnesseElementType.FIXTURE_CLASS => FixtureClassImpl(astNode)
      case FitnesseElementType.SCENARIO_NAME => ScenarioNameImpl(astNode)
      case FitnesseElementType.DECISION_INPUT => DecisionInputImpl(astNode)
      case FitnesseElementType.DECISION_OUTPUT => DecisionOutputImpl(astNode)
      case FitnesseElementType.QUERY_OUTPUT => new QueryOutput(astNode)
      case FitnesseElementType.IMPORT => new Import(astNode)
      case FitnesseElementType.LIBRARY_CLASS => new LibraryClass(astNode)
      case FitnesseElementType.WIKI_WORD => new WikiLink(astNode)
      case _ => new ASTWrapperPsiElement(astNode)
    }
  }

  override def createFile(fileViewProvider: FileViewProvider) = new FitnesseFile(fileViewProvider)

  override def spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY
}
