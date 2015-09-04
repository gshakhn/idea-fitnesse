package fitnesse.idea.lang.parser

import com.intellij.psi.{PsiElement, SingleRootFileViewProvider}
import com.intellij.testFramework.LightVirtualFile
import fitnesse.idea.lang.FitnesseLanguage

class SymbolicLinksPageSuite extends ParserSuite {

  test("page length and content length should match") {
    val text =
        "!img-r http://files/fitnesse/images/symlinkDiagram.gif\n" +
        "!3 What are SymbolicLinks?\nSymbolic Links allow a user to easily create parent-child links between pages without permanently affecting the wiki structure.  Refer to the diagram on the right.  You will notice that the !-ApplicationTests-! page has a child page named !-SuiteTests-! which contains several other pages.  The !-EnvironmentOne-! page has no static child pages but it does have a symbolic link to the !-SuiteTests-! page.  This symbolic link acts like a child page in almost every way and give two full names to the !-SuiteTests-! page:\n"
//      "\n" +
//      " * !-.ApplicationTests.SuiteTests-! is the real name,\n" +
//      " * !-.EnviromentOne.SymbolicLink-! is a symbolic name for the same page.\n" +
//      "\n" +
//      "SymbolicLinks may also be made to external FitNesse directories.  See below.\n" +
//      "\n" +
//      "!3 Why Should I Use SymbolicLinks?\nOne common reason need for Symbolic Links is the the testing of a system on multiple environments.  For example, imagine an application that site on top of an Oracle database.  Hundreds of FitNesse tests have been written for this application using Oracle settings and then the team is confronted with the need to run the application on !-MySql-!.  Getting both databases running under the same suite of tests can be very difficult and may result in duplicating all the tests.  With Symbolic Links, the database configurations can be stored in high level pages along with appropriate path elements.  Then the high-level pages may symbolically link to the test suite.  In this manner one suite of tests can be executed in multiple environments.\n" +
//      "\n" +
//      "Another reason is that you want to test two implementations for the same interface. \n" +
//      ".FitNesse.SuiteAcceptanceTests.SuiteVersionsControllerTests is a practical example using symbolic links.\n" +
//      "\n" +
//      "!3 How do I use SymbolicLinks?\nNavigate to the page where you'd like to add a symbolic link.  Click the '''Properties''' button to load the ''properties'' view.  There is a section titled '''Symbolic Links'''.  There is a form here where may create a new symbolic link by suppling a single WikiWord name for the link and relative or absolute path to the page to which you'd like to link.  Once created, the symbolic link will be listed in the '''Symbolic Links''' section of the same ''properties'' view.    You may remove or rename existing links by clicking the corresponding '''Unlink''' or '''Create/Replace''' link.\n\n!3 External SymbolicLinks\nFor various reasons, you may want to keep a branch of your FitNesse wiki in a different location on your disk, than the rest of the wiki.  You may link external branches into the main wiki by using SymbolicLinks.  To do so, simply provide the file path to the external FitNesse directory in URL format preceded with '''file:/''', e.g. ''!-<i>file:///User/MicahMartin/fitnesse/ExternalFitNesseRoot</i>-!''\n" +
//      "\n" +
//      "SymbolicLinks can also use environment variables to find a branch to add to your wiki, e.g. ''!-<i>file://${HOME}/MicahMartin/fitnesse/ExternalFitNesseRoot</i>-!''"

    val virtualFile: LightVirtualFile = new LightVirtualFile("content.txt", FitnesseLanguage.INSTANCE, text)
    val viewProvider = new SingleRootFileViewProvider(myPsiManager, virtualFile, true)
    val file: PsiElement = parserDefinition.createFile(viewProvider)

    println(s"Text length = ${text.length}")
    verifyLengthAddsUp(file)
    assertResult(file.getTextLength) { verifyLengthAddsUp(file) }
  }

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
