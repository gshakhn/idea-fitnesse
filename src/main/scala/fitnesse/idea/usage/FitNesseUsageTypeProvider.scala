package fitnesse.idea.usage

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.{UsageType, UsageTypeProvider}
import fitnesse.idea.filetype.FitnesseFileType

class FitNesseUsageTypeProvider extends UsageTypeProvider {

  override def getUsageType(psiElement: PsiElement): UsageType = {
    if (psiElement.getContainingFile.getFileType == FitnesseFileType.INSTANCE) {
      FitNesseUsageTypeProvider.FITNESSE_USAGE
    } else {
      null
    }
  }
}

object FitNesseUsageTypeProvider {
  val FITNESSE_USAGE: UsageType = new UsageType("Usage in FitNesse")
}