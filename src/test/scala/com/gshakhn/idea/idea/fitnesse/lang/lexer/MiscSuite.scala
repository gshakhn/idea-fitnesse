package com.gshakhn.idea.idea.fitnesse.lang.lexer

class MiscSuite extends LexerSuite {
  test("Regular text followed by Wiki Word") {
    expect(
      List(
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "text"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "and"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WIKI_WORD, "WikiWord")
      )) {
      lex("Some text and WikiWord")
    }
  }

  test("Wiki Word followed by a LF") {
    expect(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikWord"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikWord\n")
    }
  }

  test("Wiki Word followed by a CR LF") {
    expect(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikWord"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikWord\n")
    }
  }

  test("Wiki Word that ends in a capital letter followed by a LF") {
    expect(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikiWordThisIsA"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikiWordThisIsA\n")
    }
  }

  test("Wiki Word that ends in a capital letter followed by a CR LF") {
    expect(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikiWordThisIsA"),
        (FitnesseTokenType.LINE_TERMINATOR, "\r\n")
      )) {
      lex("WikiWordThisIsA\r\n")
    }
  }

  test("Table that has regular text right after it") {
    expect(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "abc"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, "\n"),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "xyz"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, "\n"),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.WORD, "some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "text")
      )) {
      lex("|abc|\n|xyz|\nsome text")
    }
  }

  test("Table that has a WikiWord right after it") {
    expect(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "abc"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, "\n"),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "xyz"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, "\n"),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.WIKI_WORD, "WikiWord")
      )) {
      lex("|abc|\n|xyz|\nWikiWord")
    }
  }
}
