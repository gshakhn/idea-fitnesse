package fitnesse.idea.lang.filetype

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import fitnesse.idea.lang.FitnesseLanguage

class FitnesseFileType extends LanguageFileType(FitnesseLanguage.INSTANCE) {
  def getName = "Fitnesse"

  def getDescription = "Fitnesse file"

  def getDefaultExtension = "txt"

  def getIcon = FitnesseFileType.FILE_ICON
}

object FitnesseFileType {
  final val INSTANCE = new FitnesseFileType
  final val FILE_ICON = IconLoader.getIcon("/fitnesse/idea/FitNesseLogo.png")
  final val FILE_NAME = "content.txt"
}