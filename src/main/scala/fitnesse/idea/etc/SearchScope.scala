package fitnesse.idea.etc

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope

/**
  * Define the scope to use for looking up dependencies
  */
object SearchScope {

  def searchScope(project: Project) = GlobalSearchScope.projectScope(project)
}
