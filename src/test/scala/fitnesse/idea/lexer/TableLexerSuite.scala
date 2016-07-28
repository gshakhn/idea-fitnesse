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
        (FitnesseTokenType.TABLE_START, "|A|B|\n|C|D|\n"),
        (FitnesseTokenType.ROW_START, "A|B|\n|"),
        (FitnesseTokenType.CELL_START, "A|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "B|\n|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "C|D|\n"),
        (FitnesseTokenType.CELL_START, "C|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "D|\n"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.CELL_END, "|\n"),
        (FitnesseTokenType.ROW_END, "|\n"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|A|B|\n|C|D|\n\n")
    }
  }

  test("Simple table with CRLF") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|A|B|\r\n|C|D|\r\n"),
        (FitnesseTokenType.ROW_START, "A|B|\r\n|"),
        (FitnesseTokenType.CELL_START, "A|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "B|\r\n|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.CELL_END, "|\r\n|"),
        (FitnesseTokenType.ROW_END, "|\r\n|"),
        (FitnesseTokenType.ROW_START, "C|D|\r\n"),
        (FitnesseTokenType.CELL_START, "C|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "D|\r\n"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.CELL_END, "|\r\n"),
        (FitnesseTokenType.ROW_END, "|\r\n"),
        (FitnesseTokenType.TABLE_END, "|\r\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\r\n")
      )) {
      lex("|A|B|\r\n|C|D|\r\n\r\n")
    }
  }

  test("Simple table with nothing at end") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|A|B|\n|C|D|"),
        (FitnesseTokenType.ROW_START, "A|B|\n|"),
        (FitnesseTokenType.CELL_START, "A|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "B|\n|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "C|D|"),
        (FitnesseTokenType.CELL_START, "C|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "D|"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.ROW_END, "|"),
        (FitnesseTokenType.TABLE_END, "|")
      )) {
      lex("|A|B|\n|C|D|")
    }
  }

  test("Two simple tables") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|A|B|\n|C|D|\n"),
        (FitnesseTokenType.ROW_START, "A|B|\n|"),
        (FitnesseTokenType.CELL_START, "A|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "B|\n|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "C|D|\n"),
        (FitnesseTokenType.CELL_START, "C|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "D|\n"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.CELL_END, "|\n"),
        (FitnesseTokenType.ROW_END, "|\n"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.TABLE_START, "|E|F|\n|G|H|"),
        (FitnesseTokenType.ROW_START, "E|F|\n|"),
        (FitnesseTokenType.CELL_START, "E|"),
        (FitnesseTokenType.WORD, "E"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "F|\n|"),
        (FitnesseTokenType.WORD, "F"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "G|H|"),
        (FitnesseTokenType.CELL_START, "G|"),
        (FitnesseTokenType.WORD, "G"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "H|"),
        (FitnesseTokenType.WORD, "H"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.ROW_END, "|"),
        (FitnesseTokenType.TABLE_END, "|")
      )) {
      lex("|A|B|\n|C|D|\n\n|E|F|\n|G|H|")
    }
  }


  test("Decision table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|Class|Arg1|Arg2|\n|numerator|denominator|quotient?|\n|10|5|2|\n|20|4|5|\n"),
        (FitnesseTokenType.ROW_START, "Class|Arg1|Arg2|\n|"),
        (FitnesseTokenType.CELL_START, "Class|"),
        (FitnesseTokenType.WORD, "Class"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "Arg1|"),
        (FitnesseTokenType.WORD, "Arg1"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "Arg2|\n|"),
        (FitnesseTokenType.WORD, "Arg2"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "numerator|denominator|quotient?|\n|"),
        (FitnesseTokenType.CELL_START, "numerator|"),
        (FitnesseTokenType.WORD, "numerator"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "denominator|"),
        (FitnesseTokenType.WORD, "denominator"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "quotient?|\n|"),
        (FitnesseTokenType.WORD, "quotient?"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "10|5|2|\n|"),
        (FitnesseTokenType.CELL_START, "10|"),
        (FitnesseTokenType.WORD, "10"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "5|"),
        (FitnesseTokenType.WORD, "5"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "2|\n|"),
        (FitnesseTokenType.WORD, "2"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "20|4|5|\n"),
        (FitnesseTokenType.CELL_START, "20|"),
        (FitnesseTokenType.WORD, "20"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "4|"),
        (FitnesseTokenType.WORD, "4"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "5|\n"),
        (FitnesseTokenType.WORD, "5"),
        (FitnesseTokenType.CELL_END, "|\n"),
        (FitnesseTokenType.ROW_END, "|\n"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Class|Arg1|Arg2|\n|numerator|denominator|quotient?|\n|10|5|2|\n|20|4|5|\n\n")
    }
  }

  test("Query table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|Query:some stuff|with param1|\n|Col1|Col2|\n|Result1 Col1|Result1 Col2|\n"),
        (FitnesseTokenType.ROW_START, "Query:some stuff|with param1|\n|"),
        (FitnesseTokenType.CELL_START, "Query:some stuff|"),
        (FitnesseTokenType.WORD, "Query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.WORD, "some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "stuff"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "with param1|\n|"),
        (FitnesseTokenType.WORD, "with"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "param1"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "Col1|Col2|\n|"),
        (FitnesseTokenType.CELL_START, "Col1|"),
        (FitnesseTokenType.WORD, "Col1"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "Col2|\n|"),
        (FitnesseTokenType.WORD, "Col2"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, "Result1 Col1|Result1 Col2|\n"),
        (FitnesseTokenType.CELL_START, "Result1 Col1|"),
        (FitnesseTokenType.WORD, "Result1"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Col1"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "Result1 Col2|\n"),
        (FitnesseTokenType.WORD, "Result1"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Col2"),
        (FitnesseTokenType.CELL_END, "|\n"),
        (FitnesseTokenType.ROW_END, "|\n"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Query:some stuff|with param1|\n|Col1|Col2|\n|Result1 Col1|Result1 Col2|\n\n")
    }
  }

  test("Escaped table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "!|Query:some stuff|\n"),
        (FitnesseTokenType.ROW_START, "Query:some stuff|\n"),
        (FitnesseTokenType.CELL_START, "Query:some stuff|\n"),
        (FitnesseTokenType.WORD, "Query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.WORD, "some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "stuff"),
        (FitnesseTokenType.CELL_END, "|\n"),
        (FitnesseTokenType.ROW_END, "|\n"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("!|Query:some stuff|\n\n")
    }
  }

  test("Table with empty cell") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|Some table||\n"),
        (FitnesseTokenType.ROW_START, "Some table||\n"),
        (FitnesseTokenType.CELL_START, "Some table|"),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "table"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "|\n"),
        (FitnesseTokenType.CELL_END, "|\n"),
        (FitnesseTokenType.ROW_END, "|\n"),
        (FitnesseTokenType.TABLE_END, "|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Some table||\n\n")
    }
  }

  test("Table with empty first cell") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "||Some table|\n"),
        (FitnesseTokenType.ROW_START, "|Some table|\n"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "Some table|\n"),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "table"),
        (FitnesseTokenType.CELL_END, "|\n"),
        (FitnesseTokenType.ROW_END,"|\n"),
        (FitnesseTokenType.TABLE_END,"|\n"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("||Some table|\n\n")
    }
  }

  test("Table with spaces in cells") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "| Some table | Some value |"),
        (FitnesseTokenType.ROW_START, " Some table | Some value |"),
        (FitnesseTokenType.CELL_START, " Some table |"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "table"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, " Some value |"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "value"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.ROW_END,"|"),
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
        (FitnesseTokenType.TABLE_START, "|Some table|\n|"),
        (FitnesseTokenType.ROW_START, "Some table|\n|"),
        (FitnesseTokenType.CELL_START, "Some table|\n|"),
        (FitnesseTokenType.WORD, "Some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "table"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|Some table|\n|")
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
        (FitnesseTokenType.TABLE_START, "|!-ArrangementToOrderV1-!|\n|!define ArrangementToOrderAccountNumber {125673051}|\n"),
        (FitnesseTokenType.ROW_START,"!-ArrangementToOrderV1-!|\n|"),
        (FitnesseTokenType.CELL_START,"!-ArrangementToOrderV1-!|\n|"),
        (FitnesseTokenType.WORD, "!-ArrangementToOrderV1-!"),
        (FitnesseTokenType.CELL_END,"|\n|"),
        (FitnesseTokenType.ROW_END, "|\n|"),
        (FitnesseTokenType.ROW_START,"!define ArrangementToOrderAccountNumber {125673051}|\n"),
        (FitnesseTokenType.CELL_START,"!define ArrangementToOrderAccountNumber {125673051}|\n"),
        (FitnesseTokenType.WORD, "!define ArrangementToOrderAccountNumber {125673051}"),
        (FitnesseTokenType.CELL_END, "|\n"),
        (FitnesseTokenType.ROW_END,"|\n"),
        (FitnesseTokenType.TABLE_END,"|\n")
      )) {
      lex("|!-ArrangementToOrderV1-!|\n|!define ArrangementToOrderAccountNumber {125673051}|\n")
    }
  }
}