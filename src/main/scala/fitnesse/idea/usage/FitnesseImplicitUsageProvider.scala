package fitnesse.idea.usage

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{PsiClass, PsiElement, PsiMethod}
import fitnesse.idea.fixtureclass.FixtureClassIndex
import fitnesse.idea.fixturemethod.FixtureMethodIndex

class FitnesseImplicitUsageProvider extends ImplicitUsageProvider {
  override def isImplicitRead(psiElement: PsiElement): Boolean = false

  override def isImplicitWrite(psiElement: PsiElement): Boolean = false

  override def isImplicitUsage(psiElement: PsiElement): Boolean = psiElement match {
    case cls: PsiClass => FixtureClassIndex.INSTANCE.get(cls.getName, cls.getProject, GlobalSearchScope.projectScope(cls.getProject)) != null
    case mtd: PsiMethod => FixtureMethodIndex.INSTANCE.get(mtd.getName, mtd.getProject, GlobalSearchScope.projectScope(mtd.getProject)) != null
    case _ => false
  }
}
