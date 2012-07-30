package com.gshakhn.idea.idea.fitnesse.lang.lexer

class MiscSuite extends LexerSuite {
  test("Regular text followed by Wiki Word") {
    expect(
      List(
        (FitnesseElementType.REGULAR_TEXT, "S"),
        (FitnesseElementType.REGULAR_TEXT, "o"),
        (FitnesseElementType.REGULAR_TEXT, "m"),
        (FitnesseElementType.REGULAR_TEXT, "e"),
        (FitnesseElementType.REGULAR_TEXT, " "),
        (FitnesseElementType.REGULAR_TEXT, "t"),
        (FitnesseElementType.REGULAR_TEXT, "e"),
        (FitnesseElementType.REGULAR_TEXT, "x"),
        (FitnesseElementType.REGULAR_TEXT, "t"),
        (FitnesseElementType.REGULAR_TEXT, " "),
        (FitnesseElementType.REGULAR_TEXT, "a"),
        (FitnesseElementType.REGULAR_TEXT, "n"),
        (FitnesseElementType.REGULAR_TEXT, "d"),
        (FitnesseElementType.REGULAR_TEXT, " "),
        (FitnesseElementType.WIKI_WORD, "WikiWord")
      )) {
      lex("Some text and WikiWord")
    }
  }

  test("Wiki Word followed by a LF") {
    expect(
      List(
        (FitnesseElementType.WIKI_WORD, "WikWord"),
        (FitnesseElementType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikWord\n")
    }
  }

  test("Wiki Word followed by a CR LF") {
    expect(
      List(
        (FitnesseElementType.WIKI_WORD, "WikWord"),
        (FitnesseElementType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikWord\n")
    }
  }

  test("Wiki Word that ends in a capital letter followed by a LF") {
    expect(
      List(
        (FitnesseElementType.WIKI_WORD, "WikiWordThisIsA"),
        (FitnesseElementType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikiWordThisIsA\n")
    }
  }

  test("Wiki Word that ends in a capital letter followed by a CR LF") {
    expect(
      List(
        (FitnesseElementType.WIKI_WORD, "WikiWordThisIsA"),
        (FitnesseElementType.LINE_TERMINATOR, "\r\n")
      )) {
      lex("WikiWordThisIsA\r\n")
    }
  }
}
