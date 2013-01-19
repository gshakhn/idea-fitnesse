package com.gshakhn.idea.idea.fitnesse.lang.lexer

class WikiWordLexerSuite extends LexerSuite {
  test("Regular Wiki Word") {
    expectResult(List((FitnesseTokenType.WIKI_WORD, "WikiWord"))) {
      lex("WikiWord")
    }
  }

  test("Wiki Word with numbers") {
    expectResult(List((FitnesseTokenType.WIKI_WORD, "WikiWord123"))) {
      lex("WikiWord123")
    }
  }

  test("Longer Wiki Word") {
    expectResult(List((FitnesseTokenType.WIKI_WORD, "LongerWikiWord"))) {
      lex("LongerWikiWord")
    }
  }

  test("Wiki Word that ends in a capital letter") {
    expectResult(List((FitnesseTokenType.WIKI_WORD, "WikiWordThisIsA"))) {
      lex("WikiWordThisIsA")
    }
  }

  test("Multiple Wiki Words") {
    expectResult(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikiWord"),
        (FitnesseTokenType.PERIOD, "."),
        (FitnesseTokenType.WIKI_WORD, "AnotherWikiWord"),
        (FitnesseTokenType.PERIOD, "."),
        (FitnesseTokenType.WIKI_WORD, "YetAnotherWikiWord")
      )) {
      lex("WikiWord.AnotherWikiWord.YetAnotherWikiWord")
    }
  }

  test("WikiWord prefixed with period") {
    expectResult(
      List(
        (FitnesseTokenType.PERIOD, "."),
        (FitnesseTokenType.WIKI_WORD, "WikiWord")
      )) {
      lex(".WikiWord")
    }
  }

  test("WikiWord prefixed with >") {
    expectResult(
      List(
        (FitnesseTokenType.LT, "<"),
        (FitnesseTokenType.WIKI_WORD, "WikiWord")
      )) {
      lex("<WikiWord")
    }
  }

  test("WikiWord prefixed with <") {
    expectResult(
      List(
        (FitnesseTokenType.GT, ">"),
        (FitnesseTokenType.WIKI_WORD, "WikiWord")
      )) {
      lex(">WikiWord")
    }
  }
}
