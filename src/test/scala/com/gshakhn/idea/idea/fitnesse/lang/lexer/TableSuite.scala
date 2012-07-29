package com.gshakhn.idea.idea.fitnesse.lang.lexer

class TableSuite extends LexerSuite {
  test("Decision table") {
    expect(
      List(
        (FitnesseElementType.TABLE_START, ""),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Class"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Arg1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Arg2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "numerator"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "denominator"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "quotient?"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "10"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "5"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "20"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "4"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "5"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.TABLE_END, "\n")
      )) {
      lex("|Class|Arg1|Arg2|\n|numerator|denominator|quotient?|\n|10|5|2|\n|20|4|5|\n\n")
    }
  }

  test("Query table") {
    expect(
      List(
        (FitnesseElementType.TABLE_START, ""),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Query:some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.TABLE_END, "\n")
      )) {
      lex("|Query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Ordered Query table") {
    expect(
      List(
        (FitnesseElementType.TABLE_START, ""),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Ordered query:some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.TABLE_END, "\n")
      )) {
      lex("|Ordered query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Subset Query table") {
    expect(
      List(
        (FitnesseElementType.TABLE_START, ""),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Subset query:some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.TABLE_END, "\n")
      )) {
      lex("|Subset query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Table table") {
    expect(
      List(
        (FitnesseElementType.TABLE_START, ""),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Table:Some Table"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row1 col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row1 col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row2 col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row2 col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.TABLE_END, "\n")
      )) {
      lex("|Table:Some Table|\n|row1 col1|row1 col2|\n|row2 col1|row2 col2|\n\n")
    }
  }

  test("Import Table") {
    expect(
      List(
        (FitnesseElementType.TABLE_START, ""),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Import"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "import1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "import2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.TABLE_END, "\n")
      )) {
      lex("|Import|\n|import1|\n|import2|\n\n")
    }
  }

  test("Table with empty cell") {
    expect(
      List(
        (FitnesseElementType.TABLE_START, ""),
        (FitnesseElementType.ROW_START, ""),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Some table"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.TABLE_END, "\n")
      )) {
      lex("|Some table||\n\n")
    }
  }
}