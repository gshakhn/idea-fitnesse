package fitnesse.idea.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import fitnesse.idea.filetype.FitnesseFileType

object FitnesseElementFactory {
  def createFile(project: Project, text : String) = PsiFileFactory.getInstance(project).createFileFromText(FitnesseFileType.FILE_NAME, FitnesseFileType.INSTANCE, text).asInstanceOf[FitnesseFile]
}
