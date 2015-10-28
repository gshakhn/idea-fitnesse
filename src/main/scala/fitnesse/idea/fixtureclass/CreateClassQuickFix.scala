package fitnesse.idea.fixtureclass

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.daemon.QuickFixBundle
import com.intellij.codeInsight.daemon.impl.quickfix.CreateClassKind
import com.intellij.codeInsight.intention.impl.{BaseIntentionAction, CreateClassDialog}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory
import com.intellij.openapi.fileEditor.{FileEditorManager, OpenFileDescriptor}
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.{DialogWrapper, Messages}
import com.intellij.openapi.util.Computable
import com.intellij.psi._
import com.intellij.psi.util.PsiUtil
import com.intellij.util.IncorrectOperationException
import fitnesse.idea.FitnesseBundle

class CreateClassQuickFix(_refElement: FixtureClass) extends BaseIntentionAction {
  val elementPointer = SmartPointerManager.getInstance(_refElement.getProject).createSmartPsiElementPointer(_refElement)

  setText(getTitle(_refElement.fixtureClassName match {
    case Some(name) => name
    case None => "What's this class called?"
  }))

  def getRefElement: FixtureClass = elementPointer.getElement

  def getTitle(varName: String): String = QuickFixBundle.message("create.class.from.usage.text", CreateClassKind.CLASS.getDescription, varName)

  override def getFamilyName: String = QuickFixBundle.message("create.class.from.usage.family")

  override def startInWriteAction: Boolean = false

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = {
    val element = getRefElement
    element != null && element.getManager.isInProject(element)
  }

  override def invoke(project: Project, editor: Editor, file: PsiFile) {
    PsiDocumentManager.getInstance(project).commitAllDocuments()
    val element = getRefElement
    if (element != null && FileModificationService.getInstance.preparePsiElementForWrite(element)) {
      askForTargetPackage(element) match {
        case Some(directory) =>
          createClass(directory, element.fixtureClassName.get, element.getManager, element.getContainingFile) match {
            case Some(aClass) =>
              ApplicationManager.getApplication.runWriteAction(new Runnable() {
                override def run() = {
                  IdeDocumentHistory.getInstance(element.getProject).includeCurrentPlaceAsChangePlace()
                  val descriptor = new OpenFileDescriptor(element.getProject, aClass.getContainingFile.getVirtualFile, aClass.getTextOffset)
                  FileEditorManager.getInstance(aClass.getProject).openTextEditor(descriptor, true)
                }
              })
            case _ =>
          }
        case _ =>
      }
    }
  }

  def askForTargetPackage(referenceElement: FixtureClass): Option[PsiDirectory] = {
    assert(!ApplicationManager.getApplication.isWriteAccessAllowed, "You must not run askForTargetPackage() from under write action")
    val manager = referenceElement.getManager
    val project = referenceElement.getProject
    val name = referenceElement.fixtureClassName.get
    val qualifierName = ""
    val sourceFile = referenceElement.getContainingFile
    val module = ModuleUtilCore.findModuleForPsiElement(sourceFile)
    val title = FitnesseBundle.message("quickfix.create.class")
    // Warning: hooking into code from idea.jar (not openapi.jar)
    val dialog = new CreateClassDialog(project, title, name, qualifierName, CreateClassKind.CLASS, false, module)
    dialog.show()
    dialog.getExitCode match {
      case DialogWrapper.OK_EXIT_CODE => Some(dialog.getTargetDirectory)
      case _ => None
    }
  }

  def createClass(directory: PsiDirectory, name: String, manager: PsiManager, sourceFile: PsiFile): Option[PsiClass] = {
    val facade: JavaPsiFacade = JavaPsiFacade.getInstance(manager.getProject)
    val factory: PsiElementFactory = facade.getElementFactory
    ApplicationManager.getApplication.runWriteAction(new Computable[Option[PsiClass]]() {
      def compute: Option[PsiClass] = {
        try {
          val targetClass = JavaDirectoryService.getInstance.createClass(directory, name)
          PsiUtil.setModifierProperty(targetClass, PsiModifier.PUBLIC, true)
          Some(targetClass)
        } catch {
          case e: IncorrectOperationException =>
            scheduleFileOrPackageCreationFailedMessageBox(e, name, directory, false)
            None
        }
      }
    })
  }

  def scheduleFileOrPackageCreationFailedMessageBox(e: IncorrectOperationException, name: String, directory: PsiDirectory, isPackage: Boolean) {
    ApplicationManager.getApplication.invokeLater(new Runnable() {
      def run() {
        Messages.showErrorDialog(QuickFixBundle.message(if (isPackage) "cannot.create.java.package.error.text" else "cannot.create.java.file.error.text", name, directory.getVirtualFile.getName, e.getLocalizedMessage),
          QuickFixBundle.message(if (isPackage) "cannot.create.java.package.error.title" else "cannot.create.java.file.error.title"))
      }
    })
  }
}
