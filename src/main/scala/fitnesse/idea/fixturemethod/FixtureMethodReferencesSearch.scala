package fitnesse.idea.fixturemethod

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.{PsiElement, PsiMethod, PsiReference}
import com.intellij.util.Processor
import fitnesse.idea.filetype.FitnesseFileType

import scala.collection.JavaConversions._

class FixtureMethodReferencesSearch extends QueryExecutorBase[PsiReference, MethodReferencesSearch.SearchParameters](true) {

  def processQuery(p: MethodReferencesSearch.SearchParameters, consumer: Processor[PsiReference]): Unit = {
    p.getMethod match {
      case method: PsiMethod =>
        p.getEffectiveSearchScope match {
          case scope: GlobalSearchScope =>
            val restrictedScope = GlobalSearchScope.getScopeRestrictedByFileTypes(scope, FitnesseFileType.INSTANCE)
            find(method.getName, method.getProject, restrictedScope)
              .map(fixtureMethod => consumer.process(new MethodReference(fixtureMethod) {
                override def resolve(): PsiElement = method
              }))
          case _ =>
        }
      case _ =>
    }
  }

  def find(key: String, project: Project, scope: GlobalSearchScope): List[FixtureMethod] = {
    val references: List[FixtureMethod] = FixtureMethodIndex.INSTANCE.get(key, project, scope).toList

    // TODO: Take into account the class name (via Stub structure?)
    references
  }

}
