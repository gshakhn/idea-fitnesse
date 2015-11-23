package fitnesse.idea.filetype

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project

class FitnesseNamesValidator extends NamesValidator {
  override def isIdentifier(s: String, project: Project): Boolean = true

  override def isKeyword(s: String, project: Project): Boolean = false
}
