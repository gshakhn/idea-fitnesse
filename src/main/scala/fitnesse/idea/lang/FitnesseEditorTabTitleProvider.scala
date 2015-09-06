package fitnesse.idea.lang

import com.intellij.openapi.fileEditor.impl.EditorTabTitleProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import fitnesse.idea.lang.filetype.FitnesseFileType

class FitnesseEditorTabTitleProvider extends EditorTabTitleProvider {
  override def getEditorTabTitle(project: Project, virtualFile: VirtualFile): String = {
    if (virtualFile.getName == FitnesseFileType.FILE_NAME) {
      virtualFile.getParent.getName
    } else {
      null
    }
  }
}
