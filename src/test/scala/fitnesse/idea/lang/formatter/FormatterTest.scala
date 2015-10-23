package fitnesse.idea.lang.formatter

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import fitnesse.idea.lang.filetype.FitnesseFileType
import org.scalatest._

@Ignore
class FormatterTest extends LightCodeInsightFixtureTestCase with FunSuiteLike with Matchers with BeforeAndAfter {

  before {
    setUp()
  }

  after {
    tearDown()
  }

  def assertFormat(unformatted: String, formatted: String): Unit = {
    myFixture.configureByText(FitnesseFileType.INSTANCE, unformatted)
    //    CodeStyleSettingsManager.getSettings(getProject).SPACE_AROUND_ASSIGNMENT_OPERATORS = true
    new FormattingWriteAction(getProject, myFixture).execute()
    myFixture.checkResult(formatted)
  }

  test("simple table formatting") {
    assertFormat(
      "|Should I buy it|\n" +
      "|have money|buy it?|\n",

      "| Should I buy it      |\n" +
      "| have money | buy it? |\n"
    )
  }

  test("script table") {
    assertFormat(
      "|script|echo fixture|\n" +
      "|check|echo|Hello|Hello|\n",

      "| script | echo fixture         |\n" +
      "| check  | echo | Hello | Hello |\n"
    )
  }

  test("script table with colon") {
    assertFormat(
      "|script:echo fixture|\n" +
      "|check|echo|Hello|Hello|\n",

      "| script :echo fixture         |\n" +
      "| check  | echo | Hello | Hello |\n"
    )
  }

  test("escaped script table") {
    assertFormat(
      "!|script|echo fixture|\n" +
      "|check|echo|Hello|Hello|\n",

      "!| script | echo fixture         |\n" +
      "| check   | echo | Hello | Hello |\n"
    )
  }

  test("empty table cells") {
    assertFormat(
      "!|script|echo fixture|\n" +
        "|check||Hello||\n",

      "!| script | echo fixture |\n" +
      "| check   |  | Hello |   |\n"
    )
  }

  test("scenario table") {
    assertFormat(
      "|scenario|Mydivision|numerator||denominator||quotient?|\n"+
      "|setNumerator|@numerator|\n"+
      "|setDenominator|@denominator|\n"+
      "|$quotient=|quotient|",

      "| scenario       | Mydivision | numerator |  | denominator |  | quotient? |\n" +
      "| setNumerator   | @numerator                                             |\n" +
      "| setDenominator | @denominator                                           |\n" +
      "| $quotient=     | quotient                                               |"
    )
  }


}
