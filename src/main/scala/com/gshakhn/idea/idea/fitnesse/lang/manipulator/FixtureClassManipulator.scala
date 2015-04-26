package com.gshakhn.idea.idea.fitnesse.lang.manipulator

import com.gshakhn.idea.idea.fitnesse.lang.psi.FitnesseElementFactory.createFile
import com.gshakhn.idea.idea.fitnesse.lang.psi.FixtureClass
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

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
    file.getTables(0).getFixtureClass
  }
}