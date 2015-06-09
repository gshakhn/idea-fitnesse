package fitnesse.idea.lang.highlighting

import com.intellij.openapi.options.colors.{AttributesDescriptor, ColorDescriptor, ColorSettingsPage}
import fitnesse.idea.lang.filetype.FitnesseFileType

class FitnesseColorSettingsPage extends ColorSettingsPage {
  def getAttributeDescriptors = FitnesseColorSettingsPage.DESCRIPTORS

  def getColorDescriptors = ColorDescriptor.EMPTY_ARRAY

  def getDisplayName = "FitNesse"

  def getIcon = FitnesseFileType.FILE_ICON

  def getHighlighter = new FitnesseHighlighter

  def getDemoText = "WikiWord\n\n|script | example script |\n|ensure | do action |\n|check | outcome | foo |\n\n"

  def getAdditionalHighlightingTagToDescriptorMap = null
}

object FitnesseColorSettingsPage {
  final val DESCRIPTORS = Array(
    new AttributesDescriptor("WikiWord", FitnesseHighlighter.WIKI_WORD),
    new AttributesDescriptor("Table", FitnesseHighlighter.TABLE_START)
  )
}
