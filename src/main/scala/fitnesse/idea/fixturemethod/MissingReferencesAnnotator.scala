package fitnesse.idea.fixturemethod

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.psi.PsiElement
import fitnesse.idea.etc.FitnesseBundle
import fitnesse.idea.fixtureclass.{CreateClassQuickFix, FixtureClass}

class MissingReferencesAnnotator extends Annotator {

  def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    element match {
      case fixtureClass: FixtureClass =>
        fixtureClass.getReference.multiResolve(false) match {
          case results if results.isEmpty =>
            holder.createErrorAnnotation(element.getTextRange, FitnesseBundle.message("no.fixture.class.found"))
              .registerFix(new CreateClassQuickFix(fixtureClass))
          case results if results.length > 1 =>
            holder.createWarningAnnotation(element.getTextRange, FitnesseBundle.message("multiple.candidates.found"))
          case _ =>
        }
      case fixtureMethod: FixtureMethod =>
        fixtureMethod.getReference.multiResolve(false) match {
          case results if results.isEmpty =>
            holder.createErrorAnnotation(element.getTextRange, FitnesseBundle.message("no.fixture.method.found"))
              .registerFix(new CreateMethodQuickFix(fixtureMethod))
          case results if results.length > 1 =>
            holder.createWarningAnnotation(element.getTextRange, FitnesseBundle.message("multiple.candidates.found"))
          case _ =>
        }
      case _ =>
    }
  }
}
