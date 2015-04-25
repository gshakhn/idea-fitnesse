package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class DecisionInputManipulator extends AbstractElementManipulator[DecisionInput] {
  override def handleContentChange(element: DecisionInput, range: TextRange, newContent: String) = element
}
