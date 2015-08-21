package fitnesse.idea.lang.psi

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.psi.PsiElement
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod.FixtureMethod

class MissingReferencesAnnotator extends Annotator {

  def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    element match {
      case fixtureClass: FixtureClass =>
        if (Option(fixtureClass.getReference.resolve).isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, "No fixture class found")
        }
      case fixtureMethod: FixtureMethod =>
        if (Option(fixtureMethod.getReference.resolve).isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, "No fixture method found")
        }
      case _ =>
    }
  }
}
