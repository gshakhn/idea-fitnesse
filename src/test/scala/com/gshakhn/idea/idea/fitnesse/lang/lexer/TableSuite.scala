package com.gshakhn.idea.idea.fitnesse.lang.lexer

class TableSuite extends LexerSuite {
  test("Decision table") {
    expect(
      List(
        (FitnesseElementType.DECISION_TABLE, "|"),
        (FitnesseElementType.TABLE_HEADER_CELL, "header1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_CELL, "header2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row1 col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row1 col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row2 col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row2 col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|header1|header2|\n|row1 col1|row1 col2|\n|row2 col1|row2 col2|\n\n")
    }
  }

  test("Query table") {
    expect(
      List(
        (FitnesseElementType.QUERY_TABLE, "|Query:"),
        (FitnesseElementType.TABLE_HEADER_CELL, "some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_CELL, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|Query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Query table in a different case") {
    expect(
      List(
        (FitnesseElementType.QUERY_TABLE, "|qUeRy:"),
        (FitnesseElementType.TABLE_HEADER_CELL, "some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_CELL, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|qUeRy:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Ordered Query table") {
    expect(
      List(
        (FitnesseElementType.ORDERED_QUERY_TABLE, "|Ordered query:"),
        (FitnesseElementType.TABLE_HEADER_CELL, "some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_CELL, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|Ordered query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Ordered Query table in a different case") {
    expect(
      List(
        (FitnesseElementType.ORDERED_QUERY_TABLE, "|oRdErEd QuErY:"),
        (FitnesseElementType.TABLE_HEADER_CELL, "some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_CELL, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|oRdErEd QuErY:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Subset Query table") {
    expect(
      List(
        (FitnesseElementType.SUBSET_QUERY_TABLE, "|Subset query:"),
        (FitnesseElementType.TABLE_HEADER_CELL, "some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_CELL, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|Subset query:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Subset Query table in a different case") {
    expect(
      List(
        (FitnesseElementType.SUBSET_QUERY_TABLE, "|SuBsEt QuErY:"),
        (FitnesseElementType.TABLE_HEADER_CELL, "some stuff"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_CELL, "with param1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_CELL, "Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.QUERY_COLUMN_ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "Result 1 Col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|SuBsEt QuErY:some stuff|with param1|\n|Col1|Col2|\n|Result 1 Col1|Result 1 Col2|\n\n")
    }
  }

  test("Table table") {
    expect(
      List(
        (FitnesseElementType.TABLE_TABLE, "|Table:"),
        (FitnesseElementType.TABLE_HEADER_CELL, "Some Table"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row1 col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row1 col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row2 col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row2 col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|Table:Some Table|\n|row1 col1|row1 col2|\n|row2 col1|row2 col2|\n\n")
    }
  }

  test("Table table in a different case") {
    expect(
      List(
        (FitnesseElementType.TABLE_TABLE, "|tAbLe:"),
        (FitnesseElementType.TABLE_HEADER_CELL, "Some Table"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_HEADER_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row1 col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row1 col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.ROW_END, "\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row2 col1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "row2 col2"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.TABLE_END, "\n\n")
      )) {
      lex("|tAbLe:Some Table|\n|row1 col1|row1 col2|\n|row2 col1|row2 col2|\n\n")
    }
  }

  test("Import Table") {
    expect(
      List(
        (FitnesseElementType.IMPORT_TABLE, "|Import|\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.IMPORT_CELL, "import1"),
        (FitnesseElementType.IMPORT_ROW_END, "|\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.IMPORT_CELL, "import2"),
        (FitnesseElementType.IMPORT_TABLE_END, "|\n\n")
      )) {
      lex("|Import|\n|import1|\n|import2|\n\n")
    }
  }

  test("Import Table in a different case") {
    expect(
      List(
        (FitnesseElementType.IMPORT_TABLE, "|iMpOrT|\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.IMPORT_CELL, "import1"),
        (FitnesseElementType.IMPORT_ROW_END, "|\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.IMPORT_CELL, "import2"),
        (FitnesseElementType.IMPORT_TABLE_END, "|\n\n")
      )) {
      lex("|iMpOrT|\n|import1|\n|import2|\n\n")
    }
  }

  test("Import Table with spaces in header") {
    expect(
      List(
        (FitnesseElementType.IMPORT_TABLE, "|Import  |\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.IMPORT_CELL, "import1"),
        (FitnesseElementType.IMPORT_ROW_END, "|\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.IMPORT_CELL, "import2"),
        (FitnesseElementType.IMPORT_TABLE_END, "|\n\n")
      )) {
      lex("|Import  |\n|import1|\n|import2|\n\n")
    }
  }

  test("Import Table with tabs in header") {
    expect(
      List(
        (FitnesseElementType.IMPORT_TABLE, "|Import\t\t|\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.IMPORT_CELL, "import1"),
        (FitnesseElementType.IMPORT_ROW_END, "|\n"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.IMPORT_CELL, "import2"),
        (FitnesseElementType.IMPORT_TABLE_END, "|\n\n")
      )) {
      lex("|Import\t\t|\n|import1|\n|import2|\n\n")
    }
  }
}