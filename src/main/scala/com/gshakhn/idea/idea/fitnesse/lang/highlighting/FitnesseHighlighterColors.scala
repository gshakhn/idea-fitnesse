package com.gshakhn.idea.idea.fitnesse.lang.highlighting

import java.awt.Color
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes

object FitnesseHighlighterColors {
  final val DARK_GREEN = new Color(0.00f, 0.60f, 0.00f)

  final val WIKIWORD = TextAttributesKey.createTextAttributesKey("FITNESSE.WIKIWORD", new TextAttributes(DARK_GREEN, null, null, null, 0))
}
