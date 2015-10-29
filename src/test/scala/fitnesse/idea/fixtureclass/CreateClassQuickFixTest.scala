package fitnesse.idea.fixtureclass

import com.intellij.psi.SmartPointerManager
import com.intellij.psi.impl.smartPointers.SmartPointerManagerImpl
import fitnesse.idea.psi.PsiSuite

class CreateClassQuickFixTest extends PsiSuite {


  test("quick fix is available") {
    val table = createTable("| foo bar |")
    val quickFix = new CreateClassQuickFix(table.fixtureClass.get)

    assertResult(false) {
      // because the mock isInProject implementation returns False
      quickFix.isAvailable(myProject, null, null)
    }
  }

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    myProject.getPicoContainer.registerComponentInstance(classOf[SmartPointerManager].getName, new SmartPointerManagerImpl(myProject))
  }
}
