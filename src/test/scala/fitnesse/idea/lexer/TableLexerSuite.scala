package fitnesse.idea.lexer

class TableLexerSuite extends LexerSuite {

  test("iterators") {
    val iter = Iterator("A", "B")

    assertResult(true) { iter.hasNext }
    assertResult("A") { iter.next }

    val iter2 = Iterator("C") ++ iter

    assertResult(true) { iter2.hasNext }
    assertResult("C") { iter2.next }
    assertResult(true) { iter2.hasNext }
    assertResult("B") { iter2.next }
    assertResult(false) { iter2.hasNext }
  }

  test("Simple table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|A|B|\n|C|D|\n\n")
    }
  }

  test("Simple table with CRLF") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.ROW_END, "|\r\n|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.TABLE_END, "|\r\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\r\n")
      )) {
      lex("|A|B|\r\n|C|D|\r\n\r\n")
    }
  }

  test("Simple table with nothing at end") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.TABLE_END, "|")
      )) {
      lex("|A|B|\n|C|D|")
    }
  }

  test("Two simple tables") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WORD, "E"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "F"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "G"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "H"),
        (FitnesseTokenType.TABLE_END, "|")
      )) {
      lex("|A|B|\n|C|D|\n\n|E|F|\n|G|H|")
    }
  }


  test("Decision table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WORD, "Class"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "Arg1"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "Arg2"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "numerator"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "denominator"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "quotient?"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "10"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "5"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "2"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "20"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "4"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "5"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Class|Arg1|Arg2|\n|numerator|denominator|quotient?|\n|10|5|2|\n|20|4|5|\n\n")
    }
  }

  test("Query table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WORD, "Query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.WORD, "some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "stuff"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "with"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "param1"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "Col1"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "Col2"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.WORD, "Result1"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Col1"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "Result1"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Col2"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Query:some stuff|with param1|\n|Col1|Col2|\n|Result1 Col1|Result1 Col2|\n\n")
    }
  }

  test("Escaped table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "!|"),
        (FitnesseTokenType.WORD, "Query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.WORD, "some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "stuff"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("!|Query:some stuff|\n\n")
    }
  }

  test("Table with empty cell") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "table"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Some table||\n\n")
    }
  }

  test("Table with empty first cell") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "table"),
        (FitnesseTokenType.TABLE_END,"|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("||Some table|\n\n")
    }
  }

  test("Table with spaces in cells") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "table"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "value"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.TABLE_END,"|")
      )) {
      lex("| Some table | Some value |")
    }
  }

  test("'Table' with newline after the first |") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|\n")
    }
  }

  test("'Table' with newline in middle of first cell") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "|Hello"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Hello\n")
    }
  }

  test("Table with row being currently written at EOF") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, 0, 1, "|"),
        (FitnesseTokenType.WORD,        1, 5, "Some"),
        (FitnesseTokenType.WHITE_SPACE, 5, 6, " "),
        (FitnesseTokenType.WORD,        6, 11, "table"),
        (FitnesseTokenType.ROW_END,     11, 14, "|\n|"),
        (FitnesseTokenType.TABLE_END,   14, 14, "")
      )) {
      lexWithOffset("|Some table|\n|")
    }
  }

  test("Table with row being currently written in middle of file") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "|Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "table|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.WORD, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.WORD, "Hello")
      )) {
      lex("|Some table|\n|\n\nHello")
    }
  }

  test("Misformed table with !define statement") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, 0, 1, "|"),
        (FitnesseTokenType.WORD,        1, 25, "!-ArrangementToOrderV1-!"),
        (FitnesseTokenType.ROW_END,     25, 28, "|\n|"),
        (FitnesseTokenType.WORD,        28, 79, "!define ArrangementToOrderAccountNumber {125673051}"),
        (FitnesseTokenType.TABLE_END,   79, 81, "|\n")
      )) {
      lexWithOffset("|!-ArrangementToOrderV1-!|\n|!define ArrangementToOrderAccountNumber {125673051}|\n")
    }
  }

  test("Table offsets should not overlap") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, 0, 1, "|"),
        (FitnesseTokenType.ROW_END,     1, 4, "|\n|"),
        (FitnesseTokenType.TABLE_END,   4, 5, "|")
      )) {
      lexWithOffset("||\n||")
    }
  }

  test("Escaped table offsets should not overlap") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, 0, 2, "!|"),
        (FitnesseTokenType.ROW_END,     2, 5, "|\n|"),
        (FitnesseTokenType.TABLE_END,   5, 6, "|")
      )) {
      lexWithOffset("!||\n||")
    }
  }
}