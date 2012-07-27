package com.gshakhn.idea.idea.fitnesse.lang.lexer

class WikiWordSuite extends LexerSuite {
  test("Regular Wiki Word") {
    doTest("WikiWord", List(("Fitnesse:WIKI_WORD", "WikiWord")))
  }

  test("Wiki Word with numbers") {
    doTest("WikiWord123", List(("Fitnesse:WIKI_WORD", "WikiWord123")))
  }

  test("Longer Wiki Word") {
    doTest("LongerWikiWord", List(("Fitnesse:WIKI_WORD", "LongerWikiWord")))
  }
}
