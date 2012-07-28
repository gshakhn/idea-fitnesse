package com.gshakhn.idea.idea.fitnesse.lang.lexer

class TableSuite extends LexerSuite {
  test("Decision table") {
    expect(
      List(
        (FitnesseElementType.DECISION_TABLE, "|"),
        (FitnesseElementType.CELL_TEXT, "header1"),
        (FitnesseElementType.CELL_DELIM, "|"),
        (FitnesseElementType.CELL_TEXT, "header2"),
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
}
