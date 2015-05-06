package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import fitnesse.idea.lang.reference.WikiLinkReference

class WikiLink(node: ASTNode) extends ASTWrapperPsiElement(node) {
  override def getReference = new WikiLinkReference(this)
}
