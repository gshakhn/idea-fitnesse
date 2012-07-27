package com.gshakhn.idea.idea.fitnesse.lang.lexer

class WikiWordSuite extends LexerSuite {
  test("Regular Wiki Word") {
    expect(List((FitnesseElementType.WIKI_WORD, "WikiWord"))) {
      lex("WikiWord")
    }
  }

  test("Wiki Word with numbers") {
    expect(List((FitnesseElementType.WIKI_WORD, "WikiWord123"))) {
      lex("WikiWord123")
    }
  }

  test("Longer Wiki Word") {
    expect(List((FitnesseElementType.WIKI_WORD, "LongerWikiWord"))) {
      lex("LongerWikiWord")
    }
  }
}
