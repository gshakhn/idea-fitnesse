package com.gshakhn.idea.idea.fitnesse.lang.lexer

class WikiWordSuite extends LexerSuite {
  test("Regular Wiki Word") {
    expect(List((FitnesseTokenType.WIKI_WORD, "WikiWord"))) {
      lex("WikiWord")
    }
  }

  test("Wiki Word with numbers") {
    expect(List((FitnesseTokenType.WIKI_WORD, "WikiWord123"))) {
      lex("WikiWord123")
    }
  }

  test("Longer Wiki Word") {
    expect(List((FitnesseTokenType.WIKI_WORD, "LongerWikiWord"))) {
      lex("LongerWikiWord")
    }
  }

  test("Wiki Word that ends in a capital letter") {
    expect(List((FitnesseTokenType.WIKI_WORD, "WikiWordThisIsA"))) {
      lex("WikiWordThisIsA")
    }
  }
}
