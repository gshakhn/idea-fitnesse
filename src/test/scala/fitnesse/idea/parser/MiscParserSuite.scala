package fitnesse.idea.parser

import fitnesse.idea.lexer.FitnesseTokenType

class MiscParserSuite extends ParserSuite {

  test("Period followed by word") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.WORD, ".Hello")
      ))
    ) {
      parse(".Hello")
    }
  }

  test("> followed by word") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.WORD, ">Hello")
      ))
    ) {
      parse(">Hello")
    }
  }

  test("< followed by word") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.WORD, "<Hello")
      ))
    ) {
      parse("<Hello")
    }
  }

  test("Text with unpossitioned symbols") {
    parse("!1 Suites of Tests\nA suite is a collection of test pages that can all be run together, and whose results are then collected for you on a single results page, and in TestHistory. For an example of a Suite of tests, see .FitNesse.SuiteAcceptanceTests.\n\n!3 Setting up a '''Suite''' page.\nTo create a Suite page you set the \"Suite\" property of that page (see [[Page Properties][<UserGuide.FitNesseWiki.PageProperties]]).  That page will then include a \"Suite\" button that will execute all the test pages in that hierarchy.\n\n!3 Composing Suites.\nThere are several ways to compose a suite.  You can convert a whole sub-wiki into a suite.  You can list individual pages using cross references, and you can create queries that search all the pages for matches.\n\n!contents -R2 -g\n\n!3 Getting suite results in XML rather than HTML format.\nIf you want the results in xml, simply put !style_code(&format=xml) in the url.")
  }

  test("Another page from the FitNesse user guide") {
    parse("!define TEST_SYSTEM {slim}\n\n!path lib/*.jar\n!define PACKET {|Bob|\n||Angela|\n|||Lexy|6|\n|||Sami|4|\n|||Mandy|2|\n||Micah|\n|||Luka|5|\n||Gina|\n||Justin|\n}\n\n" +
      "!define JSON ({\"tables\": [{\"Bob\": {\n \"Angela\": {\n  \"Lexy\": \"6\",\n  \"Mandy\": \"2\",\n  \"Sami\": \"4\"\n },\n \"Gina\": {},\n \"Justin\": {},\n \"Micah\": {\"Luka\": \"5\"}\n}}]})\n\n" +
      "!|script|page driver|\n|given page|PacketPage|with content|${PACKET}|\n|request page|$IT?packet|\n|contains json packet|${JSON}|\n|show|content|\n")
  }

  test("Collapsible containing a table") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.COLLAPSIBLE, List(
          Leaf(FitnesseTokenType.COLLAPSIBLE_START, "!* "),
          Leaf(FitnesseTokenType.WORD, "title\n"),
          Node(TableElementType.DECISION_TABLE, List(
            Leaf(FitnesseTokenType.TABLE_START,"|"),
            Node(FitnesseElementType.ROW, List(
              Node(FitnesseElementType.FIXTURE_CLASS, List(
                Leaf(FitnesseTokenType.WORD, "abc")
              ))
            ))
          )),
          Leaf(FitnesseTokenType.COLLAPSIBLE_END, "|\n*!\n")
        ))
      ))
    ) {
      parse("!* title\n|abc|\n*!\n")
    }
  }

}
