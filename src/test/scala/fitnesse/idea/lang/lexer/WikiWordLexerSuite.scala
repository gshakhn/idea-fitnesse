package fitnesse.idea.lang.lexer

class WikiWordLexerSuite extends LexerSuite {
  test("Regular Wiki Word") {
    assertResult(List((FitnesseTokenType.WIKI_WORD, "WikiWord"))) {
      lex("WikiWord")
    }
  }

  test("Wiki Word with numbers") {
    assertResult(List((FitnesseTokenType.WIKI_WORD, "WikiWord123"))) {
      lex("WikiWord123")
    }
  }

  test("Longer Wiki Word") {
    assertResult(List((FitnesseTokenType.WIKI_WORD, "LongerWikiWord"))) {
      lex("LongerWikiWord")
    }
  }

  test("Wiki Word that ends in a capital letter") {
    assertResult(List((FitnesseTokenType.WIKI_WORD, "WikiWordThisIsA"))) {
      lex("WikiWordThisIsA")
    }
  }

  test("Multiple Wiki Words") {
    assertResult(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikiWord.AnotherWikiWord.YetAnotherWikiWord")
      )) {
      lex("WikiWord.AnotherWikiWord.YetAnotherWikiWord")
    }
  }

  test("WikiWord prefixed with period") {
    assertResult(
      List(
        (FitnesseTokenType.WIKI_WORD, ".WikiWord")
      )) {
      lex(".WikiWord")
    }
  }

  test("WikiWord prefixed with >") {
    assertResult(
      List(
        (FitnesseTokenType.WIKI_WORD, "<WikiWord")
      )) {
      lex("<WikiWord")
    }
  }

  test("WikiWord prefixed with <") {
    assertResult(
      List(
        (FitnesseTokenType.WIKI_WORD, ">WikiWord")
      )) {
      lex(">WikiWord")
    }
  }
}
