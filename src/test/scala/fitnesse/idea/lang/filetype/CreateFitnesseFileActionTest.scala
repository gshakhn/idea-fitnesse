package fitnesse.idea.lang.filetype

import com.intellij.mock.{MockPsiDirectory, MockPsiFile, MockVirtualFile}
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.{PsiDirectory, PsiFile}
import fitnesse.idea.lang.parser.ParserSuite
import org.scalatest.BeforeAndAfter

class CreateFitnesseFileActionTest extends ParserSuite with BeforeAndAfter {

  var action: CreateFitnesseFileAction = _

  before {
    action = new CreateFitnesseFileAction()
  }

  test("action name") {
    assertResult("Create FitNesse page fooBar") {
      action.getActionName(new MyMockPsiDirectory("root"), "fooBar", "")
    }
  }

  test("create FitNesse file in directory") {
    val dir = new MyMockPsiDirectory("root")
    assertResult ("NewPage") {
      val contentFile = action.createFile("NewPage", "TestPage", dir)
      contentFile.getParent.getName
    }
  }

  test("properties file for test page") {
    val dir = new MyMockPsiDirectory("root")
    val propertiesXml = action.createPropertiesFileContent("TestPage")

    assert(propertiesXml.contains("<Test/>"))
  }

  test("properties file for suite page") {
    val dir = new MyMockPsiDirectory("root")
    val propertiesXml = action.createPropertiesFileContent("SuitePage")

    assert(propertiesXml.contains("<Suite/>"))
  }

  test("properties file for static page") {
    val dir = new MyMockPsiDirectory("root")
    val propertiesXml = action.createPropertiesFileContent("StaticPage")

    assert(!propertiesXml.contains("<Test/>"))
    assert(!propertiesXml.contains("<Suite/>"))
  }

  test("property files are nicely aligned") {
    val dir = new MyMockPsiDirectory("root")
    val propertiesXml = action.createPropertiesFileContent("StaticPage")

    assert(propertiesXml.contains("\n<properties>\n"))
  }

  class MyMockPsiDirectory(name: String) extends MockPsiDirectory(myProject, myProject) {

    override def getName: String = name

    override def createSubdirectory(name: String): PsiDirectory = {
      val mpd = new MyMockPsiDirectory(name)
      mpd.setParent(this)
      mpd
    }

    override def createFile(name: String): PsiFile = {
      val dir = this
      new MockPsiFile(new MockVirtualFile(name, ""), myPsiManager) {
        override def getParent: PsiDirectory = dir
      }
    }
  }
}
