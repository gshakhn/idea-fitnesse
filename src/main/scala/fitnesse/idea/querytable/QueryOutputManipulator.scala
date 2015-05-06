package fitnesse.idea.querytable

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class QueryOutputManipulator extends AbstractElementManipulator[QueryOutput] {
   override def handleContentChange(element: QueryOutput, range: TextRange, newContent: String) = element
 }
