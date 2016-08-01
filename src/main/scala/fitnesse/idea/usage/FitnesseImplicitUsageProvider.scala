package fitnesse.idea.usage

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.{PsiClass, PsiElement, PsiMethod}
import fitnesse.idea.etc.SearchScope.searchScope
import fitnesse.idea.fixtureclass.FixtureClassIndex
import fitnesse.idea.fixturemethod.FixtureMethodIndex

class FitnesseImplicitUsageProvider extends ImplicitUsageProvider {
  override def isImplicitRead(psiElement: PsiElement): Boolean = false

  override def isImplicitWrite(psiElement: PsiElement): Boolean = false

  override def isImplicitUsage(psiElement: PsiElement): Boolean = psiElement match {
    case cls: PsiClass => cls.getName != null && !FixtureClassIndex.INSTANCE.get(cls.getName, cls.getProject, searchScope(cls.getProject)).isEmpty
    case mtd: PsiMethod => mtd.getName != null && !FixtureMethodIndex.INSTANCE.get(mtd.getName, mtd.getProject, searchScope(mtd.getProject)).isEmpty
    case _ => false
  }
}
