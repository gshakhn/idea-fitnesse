package fitnesse.idea.lang.manipulator

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import fitnesse.idea.lang.psi.WikiLink

class WikiLinkManipulator extends AbstractElementManipulator[WikiLink] {
  override def handleContentChange(element: WikiLink, range: TextRange, newContent: String) = element
}
