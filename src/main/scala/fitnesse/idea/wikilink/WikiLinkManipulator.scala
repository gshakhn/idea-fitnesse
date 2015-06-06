package fitnesse.idea.wikilink

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class WikiLinkManipulator extends AbstractElementManipulator[WikiLink] {
  override def handleContentChange(element: WikiLink, range: TextRange, newContent: String) = element
}
