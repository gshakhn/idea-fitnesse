package com.gshakhn.idea.idea.fitnesse.lang.lexer

class MiscSuite extends LexerSuite {
  test("Regular text followed by Wiki Word") {
    expect(
      List(
        (FitnesseElementType.WORD, "Some"),
        (FitnesseElementType.WHITE_SPACE, " "),
        (FitnesseElementType.WORD, "text"),
        (FitnesseElementType.WHITE_SPACE, " "),
        (FitnesseElementType.WORD, "and"),
        (FitnesseElementType.WHITE_SPACE, " "),
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
