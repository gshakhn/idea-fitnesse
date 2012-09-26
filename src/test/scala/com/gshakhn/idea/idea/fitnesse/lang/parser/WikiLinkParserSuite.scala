package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType

class WikiLinkParserSuite extends ParserSuite {
  test("Relative reference") {
    expect(
      Node(FitnesseElementType.FILE, List(
        Node(WikiLinkElementType.RELATIVE_WIKI_LINK, List(
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
        Node(WikiLinkElementType.RELATIVE_WIKI_LINK, List(
          Leaf(FitnesseTokenType.WIKI_WORD, "SiblingPage"),
          Leaf(FitnesseTokenType.PERIOD, "."),
          Leaf(FitnesseTokenType.WIKI_WORD, "SiblingsChild")
        ))
      ))
    ) {
      parse("SiblingPage.SiblingsChild")
    }
  }

  test("Absolute reference") {
    expect(
      Node(FitnesseElementType.FILE, List(
        Node(WikiLinkElementType.ABSOLUTE_WIKI_LINK, List(
          Leaf(FitnesseTokenType.PERIOD, "."),
          Leaf(FitnesseTokenType.WIKI_WORD, "TopPage")
        ))
      ))
    ) {
      parse(".TopPage")
    }
  }

  test("Absolute reference with child") {
    expect(
      Node(FitnesseElementType.FILE, List(
        Node(WikiLinkElementType.ABSOLUTE_WIKI_LINK, List(
          Leaf(FitnesseTokenType.PERIOD, "."),
          Leaf(FitnesseTokenType.WIKI_WORD, "TopPage"),
          Leaf(FitnesseTokenType.PERIOD, "."),
          Leaf(FitnesseTokenType.WIKI_WORD, "TopPageChild")
        ))
      ))
    ) {
      parse(".TopPage.TopPageChild")
    }
  }

}
