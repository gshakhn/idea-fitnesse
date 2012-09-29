package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType

class TableParserSuite extends ParserSuite {
  test("Simple decision table with no prefix") {
    expect(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.DECISION_TABLE, List(
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.CELL_DELIM, "|"),
            Leaf(FitnesseTokenType.CELL_TEXT, "A"),
            Leaf(FitnesseTokenType.CELL_DELIM, "|")
          )),
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.CELL_DELIM, "|"),
            Leaf(FitnesseTokenType.CELL_TEXT, "B"),
            Leaf(FitnesseTokenType.CELL_DELIM, "|")
          ))
        ))
      ))
    ) {
      parse("|A|\n|B|\n\n")
    }
  }

  test("Simple decision table with no prefix with more cell text") {
    expect(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.DECISION_TABLE, List(
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.CELL_DELIM, "|"),
            Leaf(FitnesseTokenType.CELL_TEXT, "Hello World"),
            Leaf(FitnesseTokenType.CELL_DELIM, "|")
          )),
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.CELL_DELIM, "|"),
            Leaf(FitnesseTokenType.CELL_TEXT, "goodbye world"),
            Leaf(FitnesseTokenType.CELL_DELIM, "|")
          ))
        ))
      ))
    ) {
      parse("|Hello World|\n|goodbye world|\n\n")
    }
  }
}