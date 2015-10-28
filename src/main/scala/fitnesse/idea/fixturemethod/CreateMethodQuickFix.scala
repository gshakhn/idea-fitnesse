package fitnesse.idea.fixturemethod

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.daemon.QuickFixBundle
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory
import com.intellij.openapi.fileEditor.{FileEditorManager, OpenFileDescriptor}
import com.intellij.openapi.project.Project
import com.intellij.psi._
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiUtil

class CreateMethodQuickFix(_refElement: FixtureMethod) extends BaseIntentionAction {
  val elementPointer = SmartPointerManager.getInstance(_refElement.getProject).createSmartPsiElementPointer(_refElement)

  setText(getTitle(_refElement.fixtureMethodName))

  def getRefElement: FixtureMethod = elementPointer.getElement

  def getTitle(varName: String): String = QuickFixBundle.message("create.method.from.usage.text", varName)

  override def getFamilyName: String = QuickFixBundle.message("create.method.from.usage.family")

  override def startInWriteAction: Boolean = false

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = {
    val element = getRefElement
    val fixtureClassRef = getClassForFixtureClass
    element != null && element.getManager.isInProject(element) && fixtureClassRef.isDefined
  }

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    PsiDocumentManager.getInstance(project).commitAllDocuments()
    val element = getRefElement
    if (element != null && FileModificationService.getInstance.preparePsiElementForWrite(element)) {
      getClassForFixtureClass match {
        case Some(aClass) => createMethod(aClass, element) match {
          case Some(method) =>
            ApplicationManager.getApplication.runWriteAction(new Runnable() {
              override def run() = {
                aClass.add(method)
                IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace()
                val descriptor = new OpenFileDescriptor(element.getProject, aClass.getContainingFile.getVirtualFile, method.getTextOffset)
                FileEditorManager.getInstance(aClass.getProject).openTextEditor(descriptor, true)
              }
            })
          case _ =>
        }
        case _ =>
      }
    }
  }

  def getClassForFixtureClass: Option[PsiClass] = {
    val element: FixtureMethod = getRefElement
    element.fixtureClass match {
      case Some(fixtureClass) => Option(fixtureClass.getReference.resolve()) match {
        case Some(aClass : PsiClass) => Some(aClass)
        case _ => None
      }
      case None => None
    }
  }

  def createMethod(aClass: PsiClass, fixtureMethod: FixtureMethod): Option[PsiMethod] = {
    val project = aClass.getProject
    val javaLangString = PsiType.getJavaLangString(aClass.getManager, aClass.getResolveScope)
    val factory = JavaPsiFacade.getInstance(project).getElementFactory

    val method: PsiMethod = factory.createMethod(fixtureMethod.fixtureMethodName, fixtureMethod.returnType)
    fixtureMethod.parameters.foreach(parameterName => {
      val param = factory.createParameter(parameterName, javaLangString)
      method.getParameterList.add(param)
    })

    PsiUtil.setModifierProperty(method, PsiModifier.PUBLIC, true)

    val buffer = new StringBuilder
    buffer.append("{\n")
    buffer.append(fixtureMethod.returnType match {
      case PsiType.BOOLEAN => "return false;\n"
      case _: PsiClassType => "return null;\n"
      case _ => ""
    })
    buffer.append("}")
    val body: PsiCodeBlock = factory.createCodeBlockFromText(buffer.toString(), null)
    method.getBody.replace(body)

    Option(CodeStyleManager.getInstance(project).reformat(method).asInstanceOf[PsiMethod])
  }
}
