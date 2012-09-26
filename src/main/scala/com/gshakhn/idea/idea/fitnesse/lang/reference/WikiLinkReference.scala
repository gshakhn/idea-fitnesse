package com.gshakhn.idea.idea.fitnesse.lang.reference

import com.intellij.psi.{PsiElement, PsiDirectory, PsiManager, PsiReferenceBase}
import com.gshakhn.idea.idea.fitnesse.lang.psi.WikiLink
import com.intellij.psi.search.{GlobalSearchScopes, GlobalSearchScope, FileTypeIndex}
import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType
import scala.collection.JavaConversions._

class WikiLinkReference(element: WikiLink) extends PsiReferenceBase[WikiLink](element) {
  def resolve() : PsiElement = {
    val dirs = element.getText.split('.')
    val parentDir = element.getContainingFile.getParent.getParent // go up twice since the immediate parent only contains content.txt and properties.xml
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
    val targetFiles = FileTypeIndex.getFiles(FitnesseFileType.INSTANCE, GlobalSearchScopes.directoryScope(targetDir, false))

    PsiManager.getInstance(element.getProject).findFile(targetFiles.head)
  }

  def getVariants = Array()
}
