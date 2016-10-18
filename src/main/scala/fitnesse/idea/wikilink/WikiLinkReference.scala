package fitnesse.idea.wikilink

import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.TextRange
import com.intellij.psi.{PsiDirectory, PsiElement, PsiFile, PsiReferenceBase}
import fitnesse.idea.filetype.FitnesseFileType
import fitnesse.wiki.fs.WikiFilePage

class WikiLinkReference(element: WikiLink) extends PsiReferenceBase[WikiLink](element, new TextRange(0, element.getTextLength)) {
  lazy val module = Option(ModuleUtilCore.findModuleForPsiElement(element))

  override def resolve() : PsiElement = {
    val linkText = element.getText
    // <Page -> find parent (uncle) page named Page
    // >Page -> find child page Page
    // .Page -> find Page from root page (where can I find the root page?) (~= find uncle page)
    linkText.charAt(0) match {
      case '<' | '.' =>
        val path = linkText.substring(1).split('.')
        resolvePathReference(getParentBaseDir(Option(element.getContainingFile).flatMap(d => Option(d.getParent)), path(0)), path).orNull
      case '>' | '~' =>
        val path = linkText.substring(1).split('.')
        resolvePathReference(getChildBaseDir, path).orNull
      case _ =>
        val path = linkText.split('.')
        resolvePathReference(getSiblingBaseDir, path).orNull
    }
  }

  override def getVariants = Array()

  def resolvePathReference(baseDir: Option[PsiDirectory], path: Seq[String]): Option[PsiFile] = {
    val finalPage: String = path.last
    val targetDir = path.view.dropRight(1).foldLeft(baseDir)(findSubdirectory)

    findFile(targetDir, finalPage + WikiFilePage.FILE_EXTENSION)
      .orElse(
        findFile(findSubdirectory(targetDir, finalPage), FitnesseFileType.CONTENT_TXT_NAME))
  }

  // We're cheating when looking for the top-level wiki file
  def getParentBaseDir(dir: Option[PsiDirectory], containedName: String): Option[PsiDirectory] = {
    val topLevel: Boolean = dir.isEmpty || dir.exists(isTopLevelDirectory)
    val hasFile: Boolean = findSubdirectory(dir, containedName).isDefined || findFile(dir, containedName + WikiFilePage.FILE_EXTENSION).isDefined
    if (topLevel || hasFile) {
      dir
    } else {
      getParentBaseDir(dir.map(_.getParent), containedName)
    }
  }

  def getChildBaseDir: Option[PsiDirectory] = {
    val wikiFile = element.getContainingFile
    if (wikiFile.getName.equals(WikiFilePage.ROOT_FILE_NAME)) {
      Option(wikiFile.getParent)
    } else if (wikiFile.getName.endsWith(WikiFilePage.FILE_EXTENSION)) {
      Option(wikiFile.getParent.findSubdirectory(wikiFile.getName.substring(0, wikiFile.getName.length - WikiFilePage.FILE_EXTENSION.length)))
    } else {
      Option(wikiFile.getParent)
    }
  }

  def getSiblingBaseDir: Option[PsiDirectory] = {
    val wikiFile = element.getContainingFile
    if (wikiFile.getName.endsWith(WikiFilePage.FILE_EXTENSION) && !wikiFile.getName.equals(WikiFilePage.ROOT_FILE_NAME)) {
      Option(wikiFile.getParent)
    } else {
      // Go up twice since the immediate parent only contains _root.wiki, content.txt and properties.xml
      Option(wikiFile.getParent.getParent)
    }
  }

  def findSubdirectory(dir: Option[PsiDirectory], childName: String): Option[PsiDirectory] =
    dir.flatMap(dir => Option(dir.findSubdirectory(childName)))

  def findFile(dir: Option[PsiDirectory], childName: String): Option[PsiFile] =
    dir.flatMap(dir => Option(dir.findFile(childName)))

  def isTopLevelDirectory(dir: PsiDirectory): Boolean = {
    (dir.isDirectory && FitnesseFileType.FITNESSE_ROOT.equals(dir.getName)) || module.exists(m => ModuleUtilCore.isModuleDir(m, dir.getVirtualFile))
  }
}
