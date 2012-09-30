package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.{PsiDirectory, FileViewProvider}
import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage
import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType
import com.intellij.testFramework.LightVirtualFile

class FitnesseFile(fileViewProvider: FileViewProvider) extends PsiFileBase(fileViewProvider, FitnesseLanguage.INSTANCE) {
  def getFileType = FitnesseFileType.INSTANCE

  override def getName = getViewProvider.getVirtualFile match {
    case lvf: LightVirtualFile => lvf.getOriginalFile.getParent.getName
    case vf => vf.getParent.getName
  }
}