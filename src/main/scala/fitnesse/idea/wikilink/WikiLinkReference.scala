package fitnesse.idea.wikilink

import com.intellij.openapi.util.TextRange
import com.intellij.psi.{PsiDirectory, PsiElement, PsiReferenceBase}
import fitnesse.idea.filetype.FitnesseFileType

class WikiLinkReference(element: WikiLink) extends PsiReferenceBase[WikiLink](element, new TextRange(0, element.getTextLength)) {
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

    targetDir match {
      case null => null
      case _ => targetDir.findFile(FitnesseFileType.FILE_NAME)
    }
  }

  private def getParentDir(pagePath: Array[String]): PsiDirectory = {
    element.getContainingFile.getParent.getParent // go up twice since the immediate parent only contains content.txt and properties.xml
  }

  override def getVariants = Array()
}
