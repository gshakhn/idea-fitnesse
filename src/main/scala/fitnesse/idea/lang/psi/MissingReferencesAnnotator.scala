package fitnesse.idea.lang.psi

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.psi.{ResolveResult, PsiElement}
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod.FixtureMethod

class MissingReferencesAnnotator extends Annotator {

  def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    element match {
      case fixtureClass: FixtureClass =>
        val results: Array[ResolveResult] = fixtureClass.getReference.multiResolve(false)
        if (results.isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, "No fixture class found")
        } else if (results.size > 1) {
          holder.createWarningAnnotation(element.getTextRange, "Multiple candidates found")
        }
      case fixtureMethod: FixtureMethod =>
        val results: Array[ResolveResult] = fixtureMethod.getReference.multiResolve(false)
        if (results.isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, "No fixture method found")
        } else if (results.size > 1) {
          holder.createWarningAnnotation(element.getTextRange, "Multiple candidates found")
        }
      case _ =>
    }
  }
}
