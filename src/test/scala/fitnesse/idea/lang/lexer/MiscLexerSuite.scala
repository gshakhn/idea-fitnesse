package fitnesse.idea.lang.lexer

class MiscLexerSuite extends LexerSuite {
  test("Regular text followed by Wiki Word") {
    assertResult(
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
    assertResult(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikWord"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikWord\n")
    }
  }

  test("Wiki Word followed by a CR LF") {
    assertResult(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikWord"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikWord\n")
    }
  }

  test("Wiki Word that ends in a capital letter followed by a LF") {
    assertResult(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikiWordThisIsA"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("WikiWordThisIsA\n")
    }
  }

  test("Wiki Word that ends in a capital letter followed by a CR LF") {
    assertResult(
      List(
        (FitnesseTokenType.WIKI_WORD, "WikiWordThisIsA"),
        (FitnesseTokenType.LINE_TERMINATOR, "\r\n")
      )) {
      lex("WikiWordThisIsA\r\n")
    }
  }

  test("Table that has regular text right after it") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "abc"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "xyz"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.WORD, "some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "text")
      )) {
      lex("|abc|\n|xyz|\nsome text")
    }
  }

  test("Table that has spaces regular text right after it") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
//        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.CELL_TEXT, " abc "),
//        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "xyz"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.WORD, "some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "text")
      )) {
      lex("| abc |\n|xyz|\nsome text")
    }
  }

  test("Table that has a WikiWord right after it") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "abc"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "xyz"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.WIKI_WORD, "WikiWord")
      )) {
      lex("|abc|\n|xyz|\nWikiWord")
    }
  }
}
