package fitnesse.idea.lang.psi

import com.intellij.psi.PsiElement

trait Cell { self: PsiElement =>
  def getRow = getParent.asInstanceOf[Row]

  def getFixtureClass = getRow.getTable.getFixtureClass

  def fixtureClassName: Option[String] = getRow.getTable.getFixtureClass match {
    case Some(cls) => cls.fixtureClassName
    case None => None
  }

}
