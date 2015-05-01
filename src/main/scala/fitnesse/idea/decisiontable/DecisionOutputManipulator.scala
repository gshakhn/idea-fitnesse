package fitnesse.idea.decisiontable

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class DecisionOutputManipulator extends AbstractElementManipulator[DecisionOutput] {
   override def handleContentChange(element: DecisionOutput, range: TextRange, newContent: String) = element
 }
