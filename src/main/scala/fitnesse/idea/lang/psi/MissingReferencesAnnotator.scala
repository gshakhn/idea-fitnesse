package fitnesse.idea.lang.psi

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.psi.{PsiElement, ResolveResult}
import fitnesse.idea.FitnesseBundle
import fitnesse.idea.fixtureclass.{CreateClassQuickFix, FixtureClass}
import fitnesse.idea.fixturemethod.{CreateMethodQuickFix, FixtureMethod}

class MissingReferencesAnnotator extends Annotator {

  def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    element match {
      case fixtureClass: FixtureClass =>
        val results: Array[ResolveResult] = fixtureClass.getReference.multiResolve(false)
        if (results.isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, FitnesseBundle.message("no.fixture.class.found"))
            .registerFix(new CreateClassQuickFix(fixtureClass))
        } else if (results.length > 1) {
          holder.createWarningAnnotation(element.getTextRange, FitnesseBundle.message("multiple.candidates.found"))
        }
      case fixtureMethod: FixtureMethod =>
        val results: Array[ResolveResult] = fixtureMethod.getReference.multiResolve(false)
        if (results.isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, FitnesseBundle.message("no.fixture.method.found"))
            .registerFix(new CreateMethodQuickFix(fixtureMethod))
        } else if (results.length > 1) {
          holder.createWarningAnnotation(element.getTextRange, FitnesseBundle.message("multiple.candidates.found"))
        }
      case _ =>
    }
  }
}
