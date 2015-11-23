package fitnesse.idea.fixtureclass

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiFile}
import com.intellij.refactoring.rename.PsiElementRenameHandler

class FixtureClassRenameHandler extends PsiElementRenameHandler {
  // Or: VariableInplaceRenameHandler??

  override def isAvailableOnDataContext(dataContext: DataContext): Boolean = {
//    val elements: Array[PsiElement] = getElements(dataContext)
//    elements != null && elements.length > 1
    val element = getElement(dataContext)
    PsiTreeUtil.getParentOfType(element, classOf[FixtureClass], false) != null
  }

  override def invoke(project: Project, editor: Editor, file: PsiFile, dataContext: DataContext) {
    val element: PsiElement = getElement(dataContext)
    if (element != null) {
      invoke(project, Array[PsiElement](element), dataContext)
    }
  }

  override def invoke(project: Project, elements: Array[PsiElement], dataContext: DataContext) {
    super.invoke(project, elements, dataContext)
  }

  private def getElement(dataContext: DataContext): PsiElement = PsiElementRenameHandler.getElement(dataContext) //CommonDataKeys.PSI_ELEMENT.getData(dataContext)

//  private def getElements(dataContext: DataContext): Array[PsiElement] = {
//    val psiFile: PsiFile = CommonDataKeys.PSI_FILE.getData(dataContext)
//    if (!psiFile.isInstanceOf[FitnesseFile]) {
//      return null
//    }
//    val editor: Editor = CommonDataKeys.EDITOR.getData(dataContext)
//    if (editor == null) {
//      return null
//    }
//    getPsiElementsIn(editor, psiFile)
//  }

//  private def getPsiElementsIn(editor: Editor, psiFile: PsiFile): Array[PsiElement] = {
//    // TODO: TargetElementUtilBase will be removed in IntelliJ 16.
//    val reference: PsiReference = TargetElementUtilBase.findReference(editor)
//    if (reference == null) null else TargetElementUtilBase.getInstance.getTargetCandidates(reference).toArray.asInstanceOf[Array[PsiElement]]
//  }
}