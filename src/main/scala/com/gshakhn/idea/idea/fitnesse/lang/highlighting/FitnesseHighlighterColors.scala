package com.gshakhn.idea.idea.fitnesse.lang.highlighting

import java.awt.Color
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes

object FitnesseHighlighterColors {
  final val WIKI_WORD = TextAttributesKey.createTextAttributesKey("FITNESSE.WIKI_WORD", new TextAttributes(Color.BLUE, null, null, null, 0))
  final val CELL_TEXT = TextAttributesKey.createTextAttributesKey("FITNESSE.CELL_TEXT", new TextAttributes(Color.ORANGE, null, null, null, 0))
  final val TABLE_TYPE = TextAttributesKey.createTextAttributesKey("FITNESSE.TABLE_TYPE", new TextAttributes(Color.GREEN, null, null, null, 0))
}
