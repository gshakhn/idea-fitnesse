package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType

class WikiLinkParserSuite extends ParserSuite {
  test("Relative reference") {
    expect(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_LINK, List(
          Leaf(FitnesseTokenType.WIKI_WORD, "SiblingPage")
        ))
      ))
    ) {
      parse("SiblingPage")
    }
  }

  test("Relative reference with child") {
    expect(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_LINK, List(
          Leaf(FitnesseTokenType.WIKI_WORD, "SiblingPage"),
          Leaf(FitnesseTokenType.PERIOD, "."),
          Leaf(FitnesseTokenType.WIKI_WORD, "SiblingsChild")
        ))
      ))
    ) {
      parse("SiblingPage.SiblingsChild")
    }
  }

}
