package com.gshakhn.idea.idea.fitnesse.lang.highlighting

import com.intellij.openapi.options.colors.{AttributesDescriptor, ColorDescriptor, ColorSettingsPage}
import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType

class FitnesseColorSettingsPage extends ColorSettingsPage {
  def getAttributeDescriptors = FitnesseColorSettingsPage.ATTRS

  def getColorDescriptors = ColorDescriptor.EMPTY_ARRAY

  def getDisplayName = "Fitnesse"

  def getIcon = FitnesseFileType.FILE_ICON

  def getHighlighter = new FitnesseHighlighter

  def getDemoText = "WikiWord\n|Abc|Def|\n|Ghi|Jkl|\n\n"

  def getAdditionalHighlightingTagToDescriptorMap = null
}

object FitnesseColorSettingsPage {
  final val ATTRS = Array(
    new AttributesDescriptor("WikiWord", FitnesseHighlighterColors.WIKI_WORD),
    new AttributesDescriptor("Cell Text", FitnesseHighlighterColors.CELL_TEXT)
  )
}
