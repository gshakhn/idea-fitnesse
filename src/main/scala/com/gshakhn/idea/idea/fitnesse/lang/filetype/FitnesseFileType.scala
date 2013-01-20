package com.gshakhn.idea.idea.fitnesse.lang.filetype

import com.intellij.openapi.fileTypes.LanguageFileType
import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage
import com.intellij.openapi.util.IconLoader

class FitnesseFileType extends LanguageFileType(FitnesseLanguage.INSTANCE) {
  def getName = "Fitnesse"

  def getDescription = "Fitnesse file"

  def getDefaultExtension = "txt"

  def getIcon = FitnesseFileType.FILE_ICON
}

object FitnesseFileType {
  final val INSTANCE = new FitnesseFileType
  final val FILE_ICON = IconLoader.getIcon("/com/gshakhn/idea/idea/fitnesse/FitNesseLogo.png")
  final val FILE_NAME = "content.txt"
}