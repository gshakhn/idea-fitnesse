package fitnesse.idea.lang.filetype

import java.util

import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.projectView.{TreeStructureProvider, ViewSettings}
import com.intellij.ide.util.treeView.AbstractTreeNode

import scala.collection.JavaConversions._


/**
 * Decorate the Project View with FitNesse goodness.
 *
 * For now, let's hide backup zip files.
 */
class FitnesseTreeStructureProvider extends TreeStructureProvider {

  override def modify(parent: AbstractTreeNode[_], children: util.Collection[AbstractTreeNode[_]], viewSettings: ViewSettings): util.Collection[AbstractTreeNode[_]] = {
    children.find {
      case fileNode: PsiFileNode =>
        val virtualFile = fileNode.getVirtualFile
        virtualFile != null && virtualFile.getFileType == FitnesseFileType.INSTANCE
      case _ => false
    } match {
      case Some(_) =>
        children.filter {
          case fileNode: PsiFileNode =>
            val virtualFile = fileNode.getVirtualFile
            virtualFile == null || virtualFile.getExtension != "zip"
          case _ => true
        }
      case None =>
        children
    }
  }

  override def getData(collection: util.Collection[AbstractTreeNode[_]], s: String): AnyRef = null
}
