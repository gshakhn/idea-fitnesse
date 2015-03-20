package com.gshakhn.idea.idea.fitnesse.lang.manipulator

import com.intellij.psi.AbstractElementManipulator
import com.gshakhn.idea.idea.fitnesse.lang.psi.{DecisionInput, FitnesseElementFactory, FixtureClass}
import com.intellij.openapi.util.TextRange

class DecisionInputManipulator extends AbstractElementManipulator[DecisionInput] {
  override def handleContentChange(element: DecisionInput, range: TextRange, newContent: String) = element
}
