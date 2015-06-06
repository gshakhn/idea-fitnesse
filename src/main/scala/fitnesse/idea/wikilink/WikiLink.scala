package fitnesse.idea.wikilink

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

class WikiLink(node: ASTNode) extends ASTWrapperPsiElement(node) {
  override def getReference = new WikiLinkReference(this)
}
