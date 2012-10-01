package com.gshakhn.idea.idea.fitnesse.lang.manipulator

import com.intellij.psi.AbstractElementManipulator
import com.gshakhn.idea.idea.fitnesse.lang.psi.{FixtureClass, WikiLink}
import com.intellij.openapi.util.TextRange

class FixtureClassManipulator extends AbstractElementManipulator[FixtureClass] {
  def handleContentChange(element: FixtureClass, range: TextRange, newContent: String) = element
}
