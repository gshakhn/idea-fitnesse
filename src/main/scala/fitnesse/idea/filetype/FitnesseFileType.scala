package fitnesse.idea.filetype

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader

class FitnesseFileType extends LanguageFileType(FitnesseLanguage.INSTANCE) {
  def getName = "FitNesse"

  def getDescription = "FitNesse file"

  def getDefaultExtension = "txt"

  def getIcon = FitnesseFileType.FILE_ICON
}

object FitnesseFileType {
  final val INSTANCE = new FitnesseFileType
  final val FILE_ICON = IconLoader.getIcon("/fitnesse/idea/filetype/FitNesseLogo.png")
  final val FILE_NAME = "content.txt"
}