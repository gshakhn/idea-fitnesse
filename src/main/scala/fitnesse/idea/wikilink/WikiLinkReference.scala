package fitnesse.idea.wikilink

import com.intellij.psi.search.{FileTypeIndex, GlobalSearchScopes}
import com.intellij.psi.{PsiDirectory, PsiElement, PsiManager, PsiReferenceBase}
import fitnesse.idea.lang.filetype.FitnesseFileType

import scala.collection.JavaConversions._

class WikiLinkReference(element: WikiLink) extends PsiReferenceBase[WikiLink](element) {
  override def resolve() : PsiElement = {
    val linkText = element.getText

    val dirs = linkText.split('.')
    val parentDir: PsiDirectory = getParentDir(dirs)
    val targetDir = dirs.foldLeft(parentDir) {
        (currentDir: PsiDirectory, childName: String) => {
          currentDir match {
            case null => null
            case dir: PsiDirectory => dir.findSubdirectory(childName)
          }
        }
      }

    if (targetDir == null) {
      return null
    }

    targetDir.findFile("content.txt")
  }

  private def getParentDir(pagePath: Array[String]): PsiDirectory = {
    element.getContainingFile.getParent.getParent // go up twice since the immediate parent only contains content.txt and properties.xml
  }

  override def getVariants = Array()
}
