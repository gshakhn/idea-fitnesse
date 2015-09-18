package fitnesse.idea.fixtureclass

import fitnesse.idea.lang.psi.PsiSuite

class CreateClassQuickFixTest extends PsiSuite {

  test("quick fix is available") {
    val table = createTable("| foo bar |")
    val quickFix = new CreateClassQuickFix(table.fixtureClass.get)

    assertResult(false) {
      // because the mock isInProject implementation returns False
      quickFix.isAvailable(myProject, null, null)
    }
  }
}
