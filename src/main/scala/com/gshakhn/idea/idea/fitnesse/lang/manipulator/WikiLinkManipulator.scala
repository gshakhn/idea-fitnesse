package com.gshakhn.idea.idea.fitnesse.lang.manipulator

import com.intellij.psi.AbstractElementManipulator
import com.gshakhn.idea.idea.fitnesse.lang.psi.WikiLink
import com.intellij.openapi.util.TextRange

class WikiLinkManipulator extends AbstractElementManipulator[WikiLink] {
  override def handleContentChange(element: WikiLink, range: TextRange, newContent: String) = element
}
