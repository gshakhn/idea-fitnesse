package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage
import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class FitnesseFile(fileViewProvider: FileViewProvider) extends PsiFileBase(fileViewProvider, FitnesseLanguage.INSTANCE) {
  override def getFileType = FitnesseFileType.INSTANCE

  def getTables = findChildrenByClass(classOf[Table])
}