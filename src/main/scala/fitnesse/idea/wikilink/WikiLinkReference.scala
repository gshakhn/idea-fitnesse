package fitnesse.idea.wikilink

import com.intellij.openapi.util.TextRange
import com.intellij.psi.{PsiDirectory, PsiElement, PsiFile, PsiReferenceBase}
import fitnesse.idea.filetype.FitnesseFileType
import fitnesse.wiki.fs.WikiFilePage

class WikiLinkReference(element: WikiLink) extends PsiReferenceBase[WikiLink](element, new TextRange(0, element.getTextLength)) {

  override def resolve() : PsiElement = {
    val linkText = element.getText
    val path = linkText.split('.')
    resolvePathReference(path).orNull
  }

  override def getVariants = Array()

  def resolvePathReference(path: Seq[String]): Option[PsiFile] = {
    val finalPage: String = path.last
    val targetDir = path.view.dropRight(1).foldLeft(getParentDir)(findSubdirectory)

    findFile(targetDir, finalPage + WikiFilePage.FILE_EXTENSION)
      .orElse(
        findFile(findSubdirectory(targetDir, finalPage), FitnesseFileType.CONTENT_TXT_NAME))
  }

  def findSubdirectory(dir: Option[PsiDirectory], childName: String): Option[PsiDirectory] =
    dir.flatMap(dir => Option(dir.findSubdirectory(childName)))

  def findFile(dir: Option[PsiDirectory], childName: String): Option[PsiFile] =
    dir.flatMap(dir => Option(dir.findFile(childName)))

  def getParentDir: Option[PsiDirectory] = {
    val wikiFile = element.getContainingFile
    if (wikiFile.getName.endsWith(WikiFilePage.FILE_EXTENSION)) {
      Option(wikiFile.getParent)
    } else {
      // Go up twice since the immediate parent only contains content.txt and properties.xml
      Option(wikiFile.getParent.getParent)
    }
  }
}
