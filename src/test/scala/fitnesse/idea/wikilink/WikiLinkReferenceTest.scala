package fitnesse.idea.wikilink

import com.intellij.mock.{MockPsiDirectory, MockPsiFile, MockVirtualFile}
import com.intellij.psi.{PsiDirectory, PsiFile}
import fitnesse.idea.psi.PsiSuite

class WikiLinkReferenceTest extends PsiSuite {

  def makeWikiLinkReference(link: String = ".SuitePage.TestPage"): WikiLinkReference = {
    val psiFile = parsePsiFile(link)
    val wikiLinkReference = new WikiLinkReference(psiFile.getFirstChild.asInstanceOf[WikiLink])
    wikiLinkReference
  }

  ignore("resolve absolute link") {
    val fitnesseRoot = new MyMockPsiDirectory("FitNesseRoot")
    val wikiSuitePageFile = fitnesseRoot.createSubdirectory("SuitePage")
    val wikiPageFile = wikiSuitePageFile.createFile("TestPage.wiki")

    val wikiLinkReference = makeWikiLinkReference()
    assertResult("SuitePage.TestPage") {
      wikiLinkReference.resolve()
    }
  }

  ignore("getParentBaseDir up to top level directory") {
    val fitnesseRoot = new MyMockPsiDirectory("FitNesseRoot")
    val wikiSuitePageFile = fitnesseRoot.createSubdirectory("SuitePage")
    val wikiPageFile = wikiSuitePageFile.createFile("TestPage.wiki")

    val wikiLinkReference = makeWikiLinkReference()
    assertResult(fitnesseRoot) {
      wikiLinkReference.getParentBaseDir(Option(wikiSuitePageFile), "foo")
    }
  }

  test("findFile with no directory") {
    val wikiLinkReference: WikiLinkReference = makeWikiLinkReference()
    assertResult(None) {
      wikiLinkReference.findFile(None, "childName")
    }
  }

  test("findSubDirectory with no directory") {
    val wikiLinkReference: WikiLinkReference = makeWikiLinkReference()
    assertResult(None) {
      wikiLinkReference.findSubdirectory(None, "childName")
    }
  }

  class MyMockPsiDirectory(name: String) extends MockPsiDirectory(myProject, myProject) {

    var subDirs: List[PsiDirectory] = Nil
    var files: List[PsiFile] = Nil

    override def getName: String = name

    override def createSubdirectory(name: String): PsiDirectory = {
      val mpd = new MyMockPsiDirectory(name)
      mpd.setParent(this)
      subDirs = mpd :: Nil
      mpd
    }

    override def createFile(name: String): PsiFile = {
      val dir = this
      new MockPsiFile(new MockVirtualFile(name, ""), myPsiManager) {
        override def getParent: PsiDirectory = dir
      }
    }

    override def findSubdirectory(name: String): PsiDirectory = {
      subDirs.find(_.getName == name).orNull
    }

    override def findFile(name: String): PsiFile = {
      files.find(_.getName == name).orNull
    }
  }
}
