package fitnesse.idea.parser

import fitnesse.idea.lexer.FitnesseTokenType

class WikiLinkParserSuite extends ParserSuite {
  test("Relative reference") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_WORD, List(
          Leaf(FitnesseTokenType.WIKI_WORD, "SiblingPage")
        ))
      ))
    ) {
      parse("SiblingPage")
    }
  }

  test("Relative reference with child") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_WORD, List(
          Leaf(FitnesseTokenType.WIKI_WORD, "SiblingPage.SiblingsChild")
        ))
      ))
    ) {
      parse("SiblingPage.SiblingsChild")
    }
  }

  test("Absolute reference") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_WORD, List(
          Leaf(FitnesseTokenType.WIKI_WORD, ".TopPage")
        ))
      ))
    ) {
      parse(".TopPage")
    }
  }

  test("Absolute reference with child") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_WORD, List(
          Leaf(FitnesseTokenType.WIKI_WORD, ".TopPage.TopPageChild")
        ))
      ))
    ) {
      parse(".TopPage.TopPageChild")
    }
  }

  test("Subpage reference") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_WORD, List(
          Leaf(FitnesseTokenType.WIKI_WORD, ">ChildPage")
        ))
      ))
    ) {
      parse(">ChildPage")
    }
  }

  test("Subpage reference with child") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_WORD, List(
          Leaf(FitnesseTokenType.WIKI_WORD, ">ChildPage.GrandChildPage")
        ))
      ))
    ) {
      parse(">ChildPage.GrandChildPage")
    }
  }

  test("Ancestor reference") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_WORD, List(
          Leaf(FitnesseTokenType.WIKI_WORD, "<AncestorPage")
        ))
      ))
    ) {
      parse("<AncestorPage")
    }
  }

  test("Ancestor reference with child") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(FitnesseElementType.WIKI_WORD, List(
          Leaf(FitnesseTokenType.WIKI_WORD, "<AncestorPage.AncestorChildPage")
        ))
      ))
    ) {
      parse("<AncestorPage.AncestorChildPage")
    }
  }

}
