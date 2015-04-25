package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.decisiontable.DecisionInput
import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

object FitnesseElementFactory {
  def createFile(project: Project, text : String) = PsiFileFactory.getInstance(project).createFileFromText(FitnesseFileType.FILE_NAME, FitnesseFileType.INSTANCE, text).asInstanceOf[FitnesseFile]

  def createFixtureClass(project : Project, className : String) = {
    val text = "|" + className + "|"
    val file = createFile(project, text)
    file.getTables(0).getFixtureClass
  }

  def createDecisionInput(project : Project, methodName : String) = {
    val text = "|SomeClass|\n|" + methodName + "|\n"
    val file = createFile(project, text)
    file.getTables(0).getRows(1).getCells(0).asInstanceOf[DecisionInput]
  }
}
