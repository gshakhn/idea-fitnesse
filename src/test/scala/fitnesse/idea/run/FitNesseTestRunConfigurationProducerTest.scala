package fitnesse.idea.run

import com.intellij.mock.MockVirtualFile
import fitnesse.idea.parser.ParserSuite
import org.scalatest.mock.MockitoSugar

class FitNesseTestRunConfigurationProducerTest extends ParserSuite with MockitoSugar {

  override protected def beforeAll(): Unit = {
    super.beforeAll()
  }

  test("should retrieve wiki page name") {

    val producer = new FitNesseTestRunConfigurationProducer
    val fitnesseRoot = new MockVirtualFile(true, "/Users/arjan/Workspace/project/FitNesseRoot")
    val wikiPageFile = new MockVirtualFile(true, "/Users/arjan/Workspace/project/FitNesseRoot/SuitePage")
    assertResult("SuitePage") {
      producer.makeWikiPageName(fitnesseRoot, wikiPageFile)
    }
  }

  test("should retrieve nested wiki page name") {

    val producer = new FitNesseTestRunConfigurationProducer
    val fitnesseRoot = new MockVirtualFile(true, "/Users/arjan/Workspace/project/FitNesseRoot")
    val wikiPageFile = new MockVirtualFile(true, "/Users/arjan/Workspace/project/FitNesseRoot/SuitePage/TestPage")
    assertResult("SuitePage.TestPage") {
      producer.makeWikiPageName(fitnesseRoot, wikiPageFile)
    }
  }
}
