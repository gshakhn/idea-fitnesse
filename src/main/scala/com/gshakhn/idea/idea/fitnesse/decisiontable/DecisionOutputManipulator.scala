package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class DecisionOutputManipulator extends AbstractElementManipulator[DecisionOutput] {
   override def handleContentChange(element: DecisionOutput, range: TextRange, newContent: String) = element
 }
