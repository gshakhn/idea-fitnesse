package fitnesse.idea.lang.parser

import fitnesse.idea.lang.lexer.FitnesseTokenType

class MiscParserSuite extends ParserSuite {

  test("Period followed by word") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.PERIOD, "."),
        Leaf(FitnesseTokenType.WORD, "Hello")
      ))
    ) {
      parse(".Hello")
    }
  }

  test("> followed by word") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.GT, ">"),
        Leaf(FitnesseTokenType.WORD, "Hello")
      ))
    ) {
      parse(">Hello")
    }
  }

  test("< followed by word") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.LT, "<"),
        Leaf(FitnesseTokenType.WORD, "Hello")
      ))
    ) {
      parse("<Hello")
    }
  }
}
