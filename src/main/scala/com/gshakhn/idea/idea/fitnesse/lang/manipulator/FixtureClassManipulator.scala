package com.gshakhn.idea.idea.fitnesse.lang.manipulator

import com.intellij.psi.AbstractElementManipulator
import com.gshakhn.idea.idea.fitnesse.lang.psi.{FitnesseElementFactory, FixtureClass}
import com.intellij.openapi.util.TextRange

class FixtureClassManipulator extends AbstractElementManipulator[FixtureClass] {
  override def handleContentChange(element: FixtureClass, range: TextRange, newContent: String) = {
    val newElement = FitnesseElementFactory.createFixtureClass(element.getProject, newContent)
    element.replace(newElement)
    newElement
  }
}
