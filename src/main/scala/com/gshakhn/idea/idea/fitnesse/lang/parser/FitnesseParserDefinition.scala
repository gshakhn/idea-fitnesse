package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.gshakhn.idea.idea.fitnesse.lang.lexer.{FitnesseTokenType, FitnesseLexer}
import com.gshakhn.idea.idea.fitnesse.lang.psi.{WikiLink, Row, Table, FitnesseFile}
import com.intellij.lang.{ASTNode, ParserDefinition}
import com.intellij.psi.FileViewProvider
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.TokenSet
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ParserDefinition.SpaceRequirements

class FitnesseParserDefinition extends ParserDefinition {
  def createLexer(project: Project) = new FitnesseLexer

  def createParser(project: Project) = new FitnesseParser

  def getFileNodeType = FitnesseElementType.FILE

  def getWhitespaceTokens = TokenSet.create(FitnesseTokenType.WHITE_SPACE)

  def getCommentTokens = TokenSet.EMPTY

  def getStringLiteralElements = TokenSet.EMPTY

  def createElement(astNode: ASTNode) = {
    astNode.getElementType match {
      case FitnesseElementType.TABLE => new Table(astNode)
      case FitnesseElementType.ROW => new Row(astNode)
      case FitnesseElementType.WIKI_LINK => new WikiLink(astNode)
      case _ => new ASTWrapperPsiElement(astNode)
    }
  }

  def createFile(fileViewProvider: FileViewProvider) = new FitnesseFile(fileViewProvider)

  def spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY
}
