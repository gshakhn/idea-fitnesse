package com.gshakhn.idea.idea.fitnesse.lang.reference

import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType
import com.gshakhn.idea.idea.fitnesse.lang.parser.WikiLinkElementType
import com.gshakhn.idea.idea.fitnesse.lang.psi.WikiLink
import com.intellij.psi.search.{FileTypeIndex, GlobalSearchScopes}
import com.intellij.psi.{PsiDirectory, PsiElement, PsiManager, PsiReferenceBase}

import scala.collection.JavaConversions._

class WikiLinkReference(element: WikiLink) extends PsiReferenceBase[WikiLink](element) {
  override def resolve() : PsiElement = {
    val linkText = element.getNode.getElementType match {
      case WikiLinkElementType.RELATIVE_WIKI_LINK => element.getText
      case _:WikiLinkElementType => element.getText.substring(1)
    }
    var dirs = linkText.split('.')
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
    val targetFiles = FileTypeIndex.getFiles(FitnesseFileType.INSTANCE, GlobalSearchScopes.openFilesScope(element.getProject))

    PsiManager.getInstance(element.getProject).findFile(targetFiles.head)
  }


  private def getParentDir(_dirs: Array[String]): PsiDirectory = {
    var dirs: Array[String] = _dirs
    element.getNode.getElementType match {
      case WikiLinkElementType.RELATIVE_WIKI_LINK => element.getContainingFile.getParent.getParent // go up twice since the immediate parent only contains content.txt and properties.xml
      case WikiLinkElementType.SUBPAGE_WIKI_LINK => element.getContainingFile.getParent
      case WikiLinkElementType.ABSOLUTE_WIKI_LINK => {
        var currentFolder = element.getContainingFile.getParent
        while (FileTypeIndex.getFiles(FitnesseFileType.INSTANCE, GlobalSearchScopes.openFilesScope(element.getProject)).size() > 0) {
          currentFolder = currentFolder.getParent
        }
        currentFolder
      }
      case WikiLinkElementType.ANCESTOR_WIKI_LINK => {
        var currentFolder = element.getContainingFile.getParent
        while (!currentFolder.getName.equalsIgnoreCase(dirs(0))
            && FileTypeIndex.getFiles(FitnesseFileType.INSTANCE, GlobalSearchScopes.openFilesScope(element.getProject)).size() > 0) {
          currentFolder = currentFolder.getParent
        }
        dirs = dirs.tail
        currentFolder
      }
    }
  }

  override def getVariants = Array()
}
