package com.gshakhn.idea.idea.fitnesse.lang.highlighting

import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType
import com.intellij.openapi.options.colors.{AttributesDescriptor, ColorDescriptor, ColorSettingsPage}

class FitnesseColorSettingsPage extends ColorSettingsPage {
  def getAttributeDescriptors = FitnesseColorSettingsPage.DESCRIPTORS

  def getColorDescriptors = ColorDescriptor.EMPTY_ARRAY

  def getDisplayName = "Fitnesse"

  def getIcon = FitnesseFileType.FILE_ICON

  def getHighlighter = new FitnesseHighlighter

  def getDemoText = "WikiWord\n\n|script | example script\n|ensure | do action |\n|check | outcome | foo |\n\n"

  def getAdditionalHighlightingTagToDescriptorMap = null
}

object FitnesseColorSettingsPage {
  final val DESCRIPTORS = Array(
    new AttributesDescriptor("WikiWord", FitnesseHighlighter.WIKI_WORD),
    new AttributesDescriptor("Table type", FitnesseHighlighter.TABLE_TYPE),
    new AttributesDescriptor("Cell Text", FitnesseHighlighter.CELL_TEXT),
    new AttributesDescriptor("Cell Delimiter", FitnesseHighlighter.CELL_TEXT)
  )
}
