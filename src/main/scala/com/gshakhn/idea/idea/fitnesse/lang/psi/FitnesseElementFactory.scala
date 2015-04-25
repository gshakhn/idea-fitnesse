package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

object FitnesseElementFactory {
  def createFile(project: Project, text : String) = PsiFileFactory.getInstance(project).createFileFromText(FitnesseFileType.FILE_NAME, FitnesseFileType.INSTANCE, text).asInstanceOf[FitnesseFile]
}
