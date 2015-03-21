package com.gshakhn.idea.idea.fitnesse.lang.manipulator

import com.gshakhn.idea.idea.fitnesse.lang.psi.{FitnesseElementFactory, FixtureClass}
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class FixtureClassManipulator extends AbstractElementManipulator[FixtureClass] {
  override def handleContentChange(element: FixtureClass, range: TextRange, newContent: String) = {
    val newElement = FitnesseElementFactory.createFixtureClass(element.getProject, newContent)
    element.replace(newElement)
    newElement
  }
}
