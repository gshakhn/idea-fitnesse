package fitnesse.idea.run

import com.intellij.execution.{Location, PsiLocation}
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.{VirtualFile, VirtualFileManager}
import com.intellij.psi.{PsiElement, PsiManager}
import com.intellij.testIntegration.TestLocationProvider

import scala.collection.JavaConversions._

class FitnesseTestLocationProvider extends TestLocationProvider {

  def getLocation(protocolId: String, locationData: String, project: Project) = {
    protocolId match {
      case "fitnesse" =>
        val virtualFile: VirtualFile = VirtualFileManager.getInstance().findFileByUrl(VirtualFileManager.constructUrl("file", locationData))
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
        List(PsiLocation.fromPsiElement(project, psiFile))
      case _ =>
        List[Location[_ <: PsiElement]]()
    }
  }
}
