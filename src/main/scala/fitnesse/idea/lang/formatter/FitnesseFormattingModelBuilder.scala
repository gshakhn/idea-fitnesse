package fitnesse.idea.lang.formatter

import com.intellij.formatting.{FormattingModelProvider, FormattingModel, FormattingModelBuilder}
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.formatter.{FormattingDocumentModelImpl, DocumentBasedFormattingModel}
import com.intellij.psi.impl.source.SourceTreeToPsiMap
import com.intellij.psi.impl.source.tree.{TreeElement, FileElement, TreeUtil}
import com.intellij.psi.{PsiFile, PsiElement}
import com.intellij.psi.codeStyle.CodeStyleSettings

class FitnesseFormattingModelBuilder extends FormattingModelBuilder {
  def createModel (element: PsiElement, settings: CodeStyleSettings): FormattingModel = {
    val file: PsiFile = element.getContainingFile
    val fileElement: FileElement = TreeUtil.getFileElement (SourceTreeToPsiMap.psiElementToTree(element).asInstanceOf[TreeElement] )
    val rootBlock: FitnesseBlock = new FitnesseBlock(fileElement)
//    new DocumentBasedFormattingModel(rootBlock, file.getProject, settings, file.getFileType, file)
    FormattingModelProvider.createFormattingModelForPsiFile(file, rootBlock, settings)
  }

  def getRangeAffectingIndent (file: PsiFile, offset: Int, elementAtOffset: ASTNode): TextRange = null
}
