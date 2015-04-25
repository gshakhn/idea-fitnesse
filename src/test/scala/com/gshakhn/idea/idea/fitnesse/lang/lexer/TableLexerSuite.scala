package com.gshakhn.idea.idea.fitnesse.lang.lexer

class TableLexerSuite extends LexerSuite {
  test("Simple table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "A"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "B"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "C"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "D"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|A|B|\n|C|D|\n\n")
    }
  }

  test("Simple table with CRLF") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "A"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "B"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\r\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "C"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "D"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\r\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\r\n")
      )) {
      lex("|A|B|\r\n|C|D|\r\n\r\n")
    }
  }

  test("Simple table with nothing at end") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "A"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "B"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "C"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "D"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|A|B|\n|C|D|")
    }
  }

  test("Two simple tables") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "A"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "B"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "C"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "D"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "E"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "F"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "G"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "H"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|A|B|\n|C|D|\n\n|E|F|\n|G|H|")
    }
  }


  test("Decision table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Class"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Arg1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Arg2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "numerator"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "denominator"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "quotient?"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "10"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "5"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "20"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "4"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "5"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Class|Arg1|Arg2|\n|numerator|denominator|quotient?|\n|10|5|2|\n|20|4|5|\n\n")
    }
  }

  test("Query table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.CELL_TEXT, "some stuff"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Ordered Query table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Ordered query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.CELL_TEXT, "some stuff"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Ordered query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Subset Query table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Subset query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.CELL_TEXT, "some stuff"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Subset query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Table table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Table"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.CELL_TEXT, "Some Table"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "row1 col1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "row1 col2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "row2 col1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "row2 col2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Table:Some Table|\n|row1 col1|row1 col2|\n|row2 col1|row2 col2|\n\n")
    }
  }

  test("Import Table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Import"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "import1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "import2"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Import|\n|import1|\n|import2|\n\n")
    }
  }

  test("Comment Table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "comment"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "this doesn't matter"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|comment|\n|this doesn't matter|")
    }
  }

  test("Library Table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "library"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "SupportClass"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|library|\n|SupportClass|")
    }
  }

  test("Script table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "script"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "SomeClass"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "do this thing"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|script|SomeClass|with param1|\n|do this thing|with param|")
    }
  }

  test("Script table with spaces") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, " script "),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "SomeClass"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "do this thing"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("| script |SomeClass|with param1|\n|do this thing|with param|")
    }
  }

  test("Scenario table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.TABLE_TYPE, "scenario"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "scenario name"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "param1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "do this thing"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "@param1"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|scenario|scenario name|param1|\n|do this thing|@param1|")
    }
  }

  test("Table with empty cell") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Some table"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Some table||\n\n")
    }
  }

  test("Table with empty first cell") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Some table"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("||Some table|\n\n")
    }
  }

  test("'Table' with newline after the first |") {
    assertResult(
      List(
        (FitnesseTokenType.REGULAR_TEXT, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|\n")
    }
  }

  test("'Table' with newline in middle of first cell") {
    assertResult(
      List(
        (FitnesseTokenType.REGULAR_TEXT, "|"),
        (FitnesseTokenType.WORD, "Hello"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Hello\n")
    }
  }

  test("Table with row being currently written at EOF") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Some table"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|Some table|\n|")
    }
  }

  test("Table with row being currently written in middle of file") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.CELL_TEXT, "Some table"),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_DELIM, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.WORD, "Hello")
      )) {
      lex("|Some table|\n|\n\nHello")
    }
  }
}