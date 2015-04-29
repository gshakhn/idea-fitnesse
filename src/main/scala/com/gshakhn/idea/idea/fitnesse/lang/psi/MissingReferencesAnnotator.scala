package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.psi.PsiElement

class MissingReferencesAnnotator extends Annotator {

  def annotate(element: PsiElement, holder: AnnotationHolder) {
    element match {
      case fixtureClass: FixtureClass =>
        if (fixtureClass.getReferencedClasses.isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, "No fixture class found")
        }
      case methodReferences: MethodReferences =>
        if (methodReferences.getReferencedMethods.isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, "No fixture method found")
        }
      case _ =>
    }
  }
}
