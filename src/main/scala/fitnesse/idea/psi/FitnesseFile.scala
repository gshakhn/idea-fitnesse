package fitnesse.idea.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import fitnesse.idea.filetype.{FitnesseLanguage, FitnesseFileType}
import fitnesse.idea.table.Table

class FitnesseFile(fileViewProvider: FileViewProvider) extends PsiFileBase(fileViewProvider, FitnesseLanguage.INSTANCE) {
  override def getFileType = FitnesseFileType.INSTANCE

  def getTables = findChildrenByClass(classOf[Table])
}