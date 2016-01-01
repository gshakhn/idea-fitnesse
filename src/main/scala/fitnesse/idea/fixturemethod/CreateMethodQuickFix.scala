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

  def getRefElement: Option[FixtureMethod] = Option(elementPointer.getElement)

  def getTitle(varName: String): String = QuickFixBundle.message("create.method.from.usage.text", varName)

  override def getFamilyName: String = QuickFixBundle.message("create.method.from.usage.family")

  override def startInWriteAction: Boolean = false

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = getRefElement match {
    case Some(element) => element.getManager.isInProject(element) && getClassForFixtureClass(element).isDefined
    case None => false
  }

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    PsiDocumentManager.getInstance(project).commitAllDocuments()
    getRefElement collect {
      case element =>
        if (FileModificationService.getInstance.preparePsiElementForWrite(element)) {
          getClassForFixtureClass(element) collect {
            case aClass => createMethod(aClass, element) collect {
              case method =>
                ApplicationManager.getApplication.runWriteAction(new Runnable() {
                  override def run() = {
                    aClass.add(method)
                    IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace()
                    val descriptor = new OpenFileDescriptor(element.getProject, aClass.getContainingFile.getVirtualFile, method.getTextOffset)
                    FileEditorManager.getInstance(aClass.getProject).openTextEditor(descriptor, true)
                  }
                })
            }
          }
        }
    }
  }

  def getClassForFixtureClass(element: FixtureMethod): Option[PsiClass] =
    element.fixtureClass collect { case fixtureClass => fixtureClass.getReference.resolve() } collect { case aClass: PsiClass => aClass }

  def createMethod(aClass: PsiClass, fixtureMethod: FixtureMethod): Option[PsiMethod] = {
    val project = aClass.getProject
    val javaLangString = PsiType.getJavaLangString(aClass.getManager, aClass.getResolveScope)
    val factory = JavaPsiFacade.getInstance(project).getElementFactory

    val method: PsiMethod = factory.createMethod(fixtureMethod.fixtureMethodName, fixtureMethod.returnType match {
      case ReturnType.Void => PsiType.VOID
      case ReturnType.Boolean => PsiType.BOOLEAN
      case ReturnType.String => PsiType.getJavaLangString(aClass.getManager, aClass.getResolveScope)
      case ReturnType.List => PsiType.getTypeByName("java.util.List", aClass.getProject, aClass.getResolveScope)
      case ReturnType.Object => PsiType.getJavaLangObject(aClass.getManager, aClass.getResolveScope)
    })

    fixtureMethod.parameters.foreach(parameterName => {
      val param = factory.createParameter(parameterName, javaLangString)
      method.getParameterList.add(param)
    })

    PsiUtil.setModifierProperty(method, PsiModifier.PUBLIC, true)

    val buffer = new StringBuilder
    buffer.append("{\n")
    buffer.append(fixtureMethod.returnType match {
      case ReturnType.Boolean => "return false;\n"
      case ReturnType.Void => ""
      case _ => "return null;\n"
    })
    buffer.append("}")
    val body: PsiCodeBlock = factory.createCodeBlockFromText(buffer.toString(), null)
    method.getBody.replace(body)

    Option(CodeStyleManager.getInstance(project).reformat(method).asInstanceOf[PsiMethod])
  }
}
