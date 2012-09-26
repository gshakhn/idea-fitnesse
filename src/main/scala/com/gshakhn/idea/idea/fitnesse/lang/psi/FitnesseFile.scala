package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage
import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType

class FitnesseFile(fileViewProvider: FileViewProvider) extends PsiFileBase(fileViewProvider, FitnesseLanguage.INSTANCE) {
  def getFileType = FitnesseFileType.INSTANCE

  override def getName = getContainingDirectory.getName
}