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
    val fitnesseRoot = new MockVirtualFile(true, "FitNesseRoot")
    val wikiPageFile = new MockVirtualFile(true, "SuitePage")
    wikiPageFile.setParent(fitnesseRoot)
    assertResult("SuitePage") {
      producer.makeWikiPageName(fitnesseRoot, wikiPageFile)
    }
  }

  test("should retrieve nested wiki page name") {

    val producer = new FitNesseTestRunConfigurationProducer
    val fitnesseRoot = new MockVirtualFile(true, "FitNesseRoot")
    val wikiSuitePageFile = new MockVirtualFile(true, "SuitePage")
    val wikiPageFile = new MockVirtualFile(true, "TestPage")
    wikiSuitePageFile.setParent(fitnesseRoot)
    wikiPageFile.setParent(wikiSuitePageFile)
    assertResult("SuitePage.TestPage") {
      producer.makeWikiPageName(fitnesseRoot, wikiPageFile)
    }
  }
}
