package fitnesse.idea.fixtureclass

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import fitnesse.idea.psi.FitnesseElementFactory.createFile

class FixtureClassManipulator extends AbstractElementManipulator[FixtureClass] {
  override def handleContentChange(element: FixtureClass, range: TextRange, newContent: String) = {
    val newElement = FixtureClassManipulator.createFixtureClass(element.getProject, newContent)
    element.replace(newElement)
    newElement
  }
}

object FixtureClassManipulator {
  def createFixtureClass(project : Project, className : String) = {
    val text = "|" + className + "|"
    // Why parse text as a file and retrieve the fixtureClass from there?
    val file = createFile(project, text)
    file.getTables(0).fixtureClass match {
      case Some(fixtureClass) => fixtureClass
      case None => null
    }
  }
}