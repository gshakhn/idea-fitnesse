package fitnesse.idea.lang.lexer

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
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "B|\n|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "C|D|\n"),
        (FitnesseTokenType.CELL_START, "C|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "D|\n"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.CELL_END, ""),
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
        (FitnesseTokenType.TABLE_START, "|A|B|\r\n|C|D|\r\n"),
        (FitnesseTokenType.ROW_START, "A|B|\r\n|"),
        (FitnesseTokenType.CELL_START, "A|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "B|\r\n|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "C|D|\r\n"),
        (FitnesseTokenType.CELL_START, "C|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "D|\r\n"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.CELL_END, ""),
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
        (FitnesseTokenType.TABLE_START, "|A|B|\n|C|D|"),
        (FitnesseTokenType.ROW_START, "A|B|\n|"),
        (FitnesseTokenType.CELL_START, "A|"),
        (FitnesseTokenType.WORD, "A"),
        (FitnesseTokenType.CELL_END, "|"),
        (FitnesseTokenType.CELL_START, "B|\n|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.CELL_END, "|\n|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "C|D|"),
        (FitnesseTokenType.CELL_START, "C|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "D|"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
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
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "B|\n|"),
        (FitnesseTokenType.WORD, "B"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "C|D|\n"),
        (FitnesseTokenType.CELL_START, "C|"),
        (FitnesseTokenType.WORD, "C"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "D|\n"),
        (FitnesseTokenType.WORD, "D"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.TABLE_START, "|E|F|\n|G|H|"),
        (FitnesseTokenType.ROW_START, "E|F|\n|"),
        (FitnesseTokenType.CELL_START, "E|"),
        (FitnesseTokenType.WORD, "E"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "F|\n|"),
        (FitnesseTokenType.WORD, "F"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "G|H|"),
        (FitnesseTokenType.CELL_START, "G|"),
        (FitnesseTokenType.WORD, "G"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "H|"),
        (FitnesseTokenType.WORD, "H"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
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
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "Arg1|"),
        (FitnesseTokenType.WORD, "Arg1"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "Arg2|\n|"),
        (FitnesseTokenType.WORD, "Arg2"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "numerator|denominator|quotient?|\n|"),
        (FitnesseTokenType.CELL_START, "numerator|"),
        (FitnesseTokenType.WORD, "numerator"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "denominator|"),
        (FitnesseTokenType.WORD, "denominator"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "quotient?|\n|"),
        (FitnesseTokenType.WORD, "quotient?"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "10|5|2|\n|"),
        (FitnesseTokenType.CELL_START, "10|"),
        (FitnesseTokenType.WORD, "10"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "5|"),
        (FitnesseTokenType.WORD, "5"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "2|\n|"),
        (FitnesseTokenType.WORD, "2"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "20|4|5|\n"),
        (FitnesseTokenType.CELL_START, "20|"),
        (FitnesseTokenType.WORD, "20"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "4|"),
        (FitnesseTokenType.WORD, "4"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "5|\n"),
        (FitnesseTokenType.WORD, "5"),
        (FitnesseTokenType.CELL_END, ""),
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
        (FitnesseTokenType.TABLE_START, "|Query:some stuff|with param1|\n|Col1|Col2|\n|Result1 Col1|Result1 Col2|\n"),
        (FitnesseTokenType.ROW_START, "Query:some stuff|with param1|\n|"),
        (FitnesseTokenType.CELL_START, "Query:some stuff|"),
        (FitnesseTokenType.WORD, "Query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.WORD, "some"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "stuff"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "with param1|\n|"),
        (FitnesseTokenType.WORD, "with"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "param1"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "Col1|Col2|\n|"),
        (FitnesseTokenType.CELL_START, "Col1|"),
        (FitnesseTokenType.WORD, "Col1"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "Col2|\n|"),
        (FitnesseTokenType.WORD, "Col2"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "Result1 Col1|Result1 Col2|\n"),
        (FitnesseTokenType.CELL_START, "Result1 Col1|"),
        (FitnesseTokenType.WORD, "Result1"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Col1"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "Result1 Col2|\n"),
        (FitnesseTokenType.WORD, "Result1"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "Col2"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Query:some stuff|with param1|\n|Col1|Col2|\n|Result1 Col1|Result1 Col2|\n\n")
    }
  }

  ignore("Ordered Query table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Ordered query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.CELL_TEXT, "some stuff"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col2"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Ordered query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  ignore("Subset Query table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Subset query"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.CELL_TEXT, "some stuff"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Col2"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Subset query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  ignore("Table table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Table"),
        (FitnesseTokenType.COLON, ":"),
        (FitnesseTokenType.CELL_TEXT, "Some Table"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "row1 col1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "row1 col2"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "row2 col1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "row2 col2"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Table:Some Table|\n|row1 col1|row1 col2|\n|row2 col1|row2 col2|\n\n")
    }
  }

  ignore("Import Table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.TABLE_TYPE, "Import"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "import1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "import2"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Import|\n|import1|\n|import2|\n\n")
    }
  }

  ignore("Comment Table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.TABLE_TYPE, "comment"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "this doesn't matter"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|comment|\n|this doesn't matter|")
    }
  }

  ignore("Library Table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.TABLE_TYPE, "library"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "SupportClass"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|library|\n|SupportClass|")
    }
  }

  test("Script table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, "|script|SomeClass|with param1|\n|do this thing|with param|"),
        (FitnesseTokenType.ROW_START, "script|SomeClass|with param1|\n|"),
        (FitnesseTokenType.CELL_START, "script|"),
        (FitnesseTokenType.WORD, "script"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "SomeClass|"),
        (FitnesseTokenType.WIKI_WORD, "SomeClass"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "with param1|\n|"),
        (FitnesseTokenType.WORD, "with"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "param1"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, "do this thing|with param|"),
        (FitnesseTokenType.CELL_START, "do this thing|"),
        (FitnesseTokenType.WORD, "do"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "this"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "thing"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.CELL_START, "with param|"),
        (FitnesseTokenType.WORD, "with"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "param"),
        (FitnesseTokenType.CELL_END, ""),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|script|SomeClass|with param1|\n|do this thing|with param|")
    }
  }

  ignore("Script table with spaces") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.TABLE_TYPE, " script "),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "SomeClass"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "do this thing"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "with param"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("| script |SomeClass|with param1|\n|do this thing|with param|")
    }
  }

  ignore("Scenario table") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.TABLE_TYPE, "scenario"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "scenario name"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "param1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "do this thing"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "@param1"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|scenario|scenario name|param1|\n|do this thing|@param1|")
    }
  }

  ignore("Table with empty cell") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Some table"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Some table||\n\n")
    }
  }

  ignore("Table with empty first cell") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Some table"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, ""),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("||Some table|\n\n")
    }
  }

  ignore("'Table' with newline after the first |") {
    assertResult(
      List(
        (FitnesseTokenType.REGULAR_TEXT, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|\n")
    }
  }

  ignore("'Table' with newline in middle of first cell") {
    assertResult(
      List(
        (FitnesseTokenType.REGULAR_TEXT, "|"),
        (FitnesseTokenType.WORD, "Hello"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n")
      )) {
      lex("|Hello\n")
    }
  }

  ignore("Table with row being currently written at EOF") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Some table"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.TABLE_END, "")
      )) {
      lex("|Some table|\n|")
    }
  }

  ignore("Table with row being currently written in middle of file") {
    assertResult(
      List(
        (FitnesseTokenType.TABLE_START, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.CELL_TEXT, "Some table"),
        (FitnesseTokenType.CELL_START, "|"),
        (FitnesseTokenType.LINE_TERMINATOR, "\n"),
        (FitnesseTokenType.ROW_END, ""),
        (FitnesseTokenType.ROW_START, ""),
        (FitnesseTokenType.CELL_START, "|"),
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