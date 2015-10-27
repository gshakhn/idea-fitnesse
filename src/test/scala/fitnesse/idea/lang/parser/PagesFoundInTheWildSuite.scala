package fitnesse.idea.lang.parser

import com.intellij.psi.{PsiElement, SingleRootFileViewProvider}
import com.intellij.testFramework.LightVirtualFile
import fitnesse.idea.lang.FitnesseLanguage

class PagesFoundInTheWildSuite extends ParserSuite {

  test("page length and content length should match") {
    val text =
        "!img-r http://files/fitnesse/images/symlinkDiagram.gif\n" +
        "!3 What are SymbolicLinks?\nSymbolic Links allow a user to easily create parent-child links between pages without permanently affecting the wiki structure.  Refer to the diagram on the right.  You will notice that the !-ApplicationTests-! page has a child page named !-SuiteTests-! which contains several other pages.  The !-EnvironmentOne-! page has no static child pages but it does have a symbolic link to the !-SuiteTests-! page.  This symbolic link acts like a child page in almost every way and give two full names to the !-SuiteTests-! page:\n"

    val virtualFile: LightVirtualFile = new LightVirtualFile("content.txt", FitnesseLanguage.INSTANCE, text)
    val viewProvider = new SingleRootFileViewProvider(myPsiManager, virtualFile, true)
    val file: PsiElement = parserDefinition.createFile(viewProvider)

    assertResult(file.getTextLength) { verifyLengthAddsUp(file) }
  }

  test("FitNesse.SuiteAcceptanceTests.SuiteEditResponderTests.TestUserNameInRecentChanges") {
    val text = "!-RecentChanges-! displays the username of the last user that modified each page.  If the user is anonymous then it is not displayed.\n" +
      "\n" +
      "!|script|\n" +
      "|page|RecentChanges|should not contain|Aladdin|\n" +
      "|save page|SomePage|by user|Aladdin|\n" +
      "|page|RecentChanges|should contain|Aladdin|"

    val virtualFile: LightVirtualFile = new LightVirtualFile("content.txt", FitnesseLanguage.INSTANCE, text)
    val viewProvider = new SingleRootFileViewProvider(myPsiManager, virtualFile, true)
    val file: PsiElement = parserDefinition.createFile(viewProvider)

    assertResult(file.getTextLength) { verifyLengthAddsUp(file) }
  }

  // Invalid formatting blocks:nonempty text is not covered by block
  val text1 = "!2 The Backwards Search widget.\nSometimes we want to search backwards through a path to a named page.  For example, if we are on the page !style_code(!-.PageOne.PageTwo.PageThree.PageFour-!) we might be able to say !style_code(!-&lt;PageTwo-!) to search backwards to !style_code(!-PageTwo-!).  Now let's say there is a page named !style_code(!-.PageOne.PageTwo.AnotherPage-!) and we are on !style_code(!-.PageOne.PageTwo.PageThree.PageFour-!)  We could say !style_code(!-&lt;PageTwo.AnotherPage-!)\n\n!|script|\n|given page|PageOne.PageTwo.AnotherPage|\n||\n|given page|PageOne.PageTwo.PageThree.PageFour|with content|<PageTwo.AnotherPage|\n|it should have link to|PageOne.PageTwo.AnotherPage|\n||\n|given page|PageOne.PageTwo|with content|<NoSuchPage|\n|it should have creating link to|NoSuchPage|\n||\n|given page|PageOne.PageX|with content|<PageOne.NoSuchPage|\n|it should have creating link to|PageOne.NoSuchPage|"
  // 214 .. 244 !style_code(
  val text2 = "!2 The Backwards Search widget.\nSometimes we want to search backwards through a path to a named page.  For example, if we are on the page !style_code(!-.PageOne.PageTwo.PageThree.PageFour-!) we might be able to say !style_code(!-&lt;PageTwo-!) to search backwards to !style_code(!-PageTwo-!).  Now let's say there is a page named !style_code(!-.PageOne.PageTwo.AnotherPage-!) and we are on !style_code(!-.PageOne.PageTwo.PageThree.PageFour-!)  We could say !style_code(!-&lt;PageTwo.AnotherPage-!)\n\n!|script|\n|given page|PageOne.PageTwo.AnotherPage|\n||\n|given page|PageOne.PageTwo.PageThree.PageFour|with content|<PageTwo.AnotherPage|\n|it should have link to|PageOne.PageTwo.AnotherPage|\n||\n|given page|PageOne.PageTwo|with content|<NoSuchPage|\n|it should have creating link to|NoSuchPage|\n||\n|given page|PageOne.PageX|with content|<PageOne.NoSuchPage|\n|it should have creating link to|PageOne.NoSuchPage|"

  def verifyLengthAddsUp(node: PsiElement) = {
    val firstChild: PsiElement = node.getFirstChild
    val calculatedLength: Int = firstChild match {
      case null => node.getText.length
      case _ => calculateLength(firstChild)
    }

    if (calculatedLength != node.getTextLength) {
      println(s"PSI element ${node} does not add up ${calculatedLength} != ${node.getTextLength}")
    }
    calculatedLength
  }

  def calculateLength(node: PsiElement): Int = node match {
    case null => 0
    case _ =>
//      println(s"Fragment: ${node.getNode.getElementType} '${node.getText}' -- ${node.getTextRange.getStartOffset} ${node.getTextRange.getEndOffset}")
      verifyLengthAddsUp(node)
      node.getTextLength + calculateLength(node.getNextSibling)
  }
}
