package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.gshakhn.idea.idea.fitnesse.lang.lexer.{FitnesseLexer, FitnesseTokenType}
import com.gshakhn.idea.idea.fitnesse.lang.psi._
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.{ASTNode, ParserDefinition}
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.TokenSet

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
      case FitnesseElementType.ROW => new Row(astNode)
      case FitnesseElementType.FIXTURE_CLASS => new FixtureClass(astNode)
      case FitnesseElementType.DECISION_INPUT => new DecisionInput(astNode)
      case FitnesseElementType.DECISION_OUTPUT => new DecisionOutput(astNode)
      case _:WikiLinkElementType => new WikiLink(astNode)
      case _ => new ASTWrapperPsiElement(astNode)
    }
  }

  override def createFile(fileViewProvider: FileViewProvider) = new FitnesseFile(fileViewProvider)

  override def spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY
}
