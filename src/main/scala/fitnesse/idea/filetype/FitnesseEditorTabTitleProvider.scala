package fitnesse.idea.filetype

import com.intellij.openapi.fileEditor.impl.EditorTabTitleProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class FitnesseEditorTabTitleProvider extends EditorTabTitleProvider {
  override def getEditorTabTitle(project: Project, virtualFile: VirtualFile): String = {
    if (virtualFile.getName == FitnesseFileType.CONTENT_TXT_NAME) {
      virtualFile.getParent.getName
    } else {
      null
    }
  }
}
