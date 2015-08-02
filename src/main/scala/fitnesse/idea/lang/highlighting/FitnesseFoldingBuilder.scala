package fitnesse.idea.lang.highlighting

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.{FoldingBuilderEx, FoldingDescriptor}
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.search.PsiElementProcessor
import com.intellij.psi.util.PsiTreeUtil
import fitnesse.idea.lang.lexer.FitnesseTokenType
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.FitnesseFile

class FitnesseFoldingBuilder extends FoldingBuilderEx {
  var descriptors = List[FoldingDescriptor]()

  override def isCollapsedByDefault(astNode: ASTNode): Boolean = true

  override def buildFoldRegions(psiElement: PsiElement, document: Document, quick: Boolean): Array[FoldingDescriptor] = {
    psiElement match {
      case g2: FitnesseFile => {
        if (!quick) {
          PsiTreeUtil.processElements(new FitnesseFileElementprocessor, g2)
        }
        descriptors.toArray
      }
      case _ => FoldingDescriptor.EMPTY
    }
  }

  override def getPlaceholderText(astNode: ASTNode): String =
    astNode.getElementType match {
      case FitnesseElementType.COLLAPSIBLE => {
        val toList: List[String] = astNode.getText.split("\n").toList
        toList.head + " ... " + toList.last
      }
      case _ => null
    }

  private class FitnesseFileElementprocessor extends PsiElementProcessor[PsiElement] {
    override def execute(t: PsiElement): Boolean = {
      if (t.getNode.getElementType == FitnesseElementType.COLLAPSIBLE) {
        descriptors = new FoldingDescriptor(t, t.getTextRange) :: descriptors
      }
      true
    }
  }

}