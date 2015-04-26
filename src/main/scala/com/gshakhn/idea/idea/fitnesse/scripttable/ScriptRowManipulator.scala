package com.gshakhn.idea.idea.fitnesse.scripttable

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class ScriptRowManipulator extends AbstractElementManipulator[ScriptRow] {
   override def handleContentChange(element: ScriptRow, range: TextRange, newContent: String) = element
 }
