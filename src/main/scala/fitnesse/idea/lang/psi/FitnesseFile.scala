package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.filetype.FitnesseFileType

class FitnesseFile(fileViewProvider: FileViewProvider) extends PsiFileBase(fileViewProvider, FitnesseLanguage.INSTANCE) {
  override def getFileType = FitnesseFileType.INSTANCE

  def getTables = findChildrenByClass(classOf[Table])
}