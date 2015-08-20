package fitnesse.idea.lang.parser

import fitnesse.idea.lang.lexer.FitnesseTokenType

class TableParserSuite extends ParserSuite {

  test("One row decision table") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.DECISION_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "A")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END,"|")))
    ) {
      parse("|A|")
    }
  }

  test("Simple decision table with no prefix") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.DECISION_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "A")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.DECISION_INPUT, List(
              Leaf(FitnesseTokenType.WORD, "B")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "C")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END,"|\n"),
        Leaf(FitnesseTokenType.LINE_TERMINATOR,"\n")))
    ) {
      parse("|A|\n|B|\n|C|\n\n")
    }
  }

  test("Simple decision table with no prefix with more cell text") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.DECISION_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "Should"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "I"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "buy"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "it")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.DECISION_INPUT, List(
              Leaf(FitnesseTokenType.WORD, "have"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "money")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.DECISION_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "buy"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "it?")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "yes")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "yes")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|\n"),
        Leaf(FitnesseTokenType.LINE_TERMINATOR, "\n")
      ))
    ) {
      parse("|Should I buy it|\n|have money|buy it?|\n|yes|yes|\n\n")
    }
  }

  test("Simple decision table with 'dt' prefix") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.DECISION_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "dt")
            )),
            Leaf(FitnesseTokenType.COLON, ":"),
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "Should"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "I"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "buy"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "it")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.DECISION_INPUT, List(
              Leaf(FitnesseTokenType.WORD, "have"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "money")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.DECISION_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "buy"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "it?")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "yes")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "yes")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|\n"),
        Leaf(FitnesseTokenType.LINE_TERMINATOR, "\n")
      ))
    ) {
      parse("|dt:Should I buy it|\n|have money|buy it?|\n|yes|yes|\n\n")
    }
  }

  test("Simple decision table with 'decision' prefix") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.DECISION_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "decision")
            )),
            Leaf(FitnesseTokenType.COLON, ":"),
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "Should"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "I"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "buy"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "it")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.DECISION_INPUT, List(
              Leaf(FitnesseTokenType.WORD, "have"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "money")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.DECISION_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "buy"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "it?")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "yes")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "yes")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|\n"),
        Leaf(FitnesseTokenType.LINE_TERMINATOR, "\n")
      ))
    ) {
      parse("|decision:Should I buy it|\n|have money|buy it?|\n|yes|yes|\n\n")
    }
  }

  test("Query table") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.QUERY_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "query")
            )),
            Leaf(FitnesseTokenType.COLON, ":"),
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "stuff")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Leaf(FitnesseTokenType.WORD, "param1")
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.QUERY_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "foo"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.QUERY_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "bar"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.QUERY_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "1")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.QUERY_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "2")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("|query:stuff|param1|\n|foo field|bar field|\n|1|2|")
    }
  }

  test("Subset query table") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.QUERY_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "subset"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "query")
            )),
            Leaf(FitnesseTokenType.COLON, ":"),
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "stuff")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Leaf(FitnesseTokenType.WORD, "param1")
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.QUERY_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "foo"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.QUERY_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "bar"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.QUERY_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "1")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.QUERY_OUTPUT, List(
              Leaf(FitnesseTokenType.WORD, "2")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("|subset query:stuff|param1|\n|foo field|bar field|\n|1|2|")
    }
  }

  test("Script tablewith colon separator") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.SCRIPT_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "script")
            )),
            Leaf(FitnesseTokenType.COLON, ":"),
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "stuff")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Leaf(FitnesseTokenType.WORD, "param1")
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.SCRIPT_ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "foo"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "bar"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("|script:stuff|param1|\n|foo field|bar field|")
    }
  }

  test("Script table with cell separator") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.SCRIPT_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "script")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WORD, "stuff")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Leaf(FitnesseTokenType.WORD, "param1")
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.SCRIPT_ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "foo"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "bar"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("|script|stuff|param1|\n|foo field|bar field|")
    }
  }

  test("Script table without fixture class") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.SCRIPT_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "script")
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.SCRIPT_ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "foo"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "bar"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("|script|\n|foo field|bar field|")
    }
  }

  test("Script table with extra white space") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.SCRIPT_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.WHITE_SPACE, "  "),
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "script")
            )),
            Leaf(FitnesseTokenType.WHITE_SPACE, "  "),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.FIXTURE_CLASS, List(
              Leaf(FitnesseTokenType.WHITE_SPACE, "  "),
              Leaf(FitnesseTokenType.WORD, "stuff")
            )),
            Leaf(FitnesseTokenType.WHITE_SPACE, "  ")
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.SCRIPT_ROW, List(
            Leaf(FitnesseTokenType.WHITE_SPACE, "  "),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "foo"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.WHITE_SPACE, "  "),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Leaf(FitnesseTokenType.WHITE_SPACE, "  "),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "bar"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.WHITE_SPACE, "  ")
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("|  script  |  stuff  |\n|  foo field  |  bar field  |")
    }
  }

  test("Scenario table with colon separator") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.SCENARIO_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "scenario")
            )),
            Leaf(FitnesseTokenType.COLON, ":"),
            Node(FitnesseElementType.SCENARIO_NAME, List(
              Node(FitnesseElementType.CELL, List(
                Leaf(FitnesseTokenType.WORD, "stuff")
              )),
              Leaf(FitnesseTokenType.CELL_END, "|"),
              Node(FitnesseElementType.CELL, List(
                Leaf(FitnesseTokenType.WORD, "param1")
              ))
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.SCRIPT_ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "foo"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "bar"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("|scenario:stuff|param1|\n|foo field|bar field|")
    }
  }

  test("Scenario table with cell separator") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.SCENARIO_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "scenario")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.SCENARIO_NAME, List(
              Node(FitnesseElementType.CELL, List(
                Leaf(FitnesseTokenType.WORD, "stuff")
              )),
              Leaf(FitnesseTokenType.CELL_END, "|"),
              Node(FitnesseElementType.CELL, List(
                Leaf(FitnesseTokenType.WORD, "param1")
              ))
            ))
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.SCRIPT_ROW, List(
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "foo"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            )),
            Leaf(FitnesseTokenType.CELL_END, "|"),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "bar"),
              Leaf(FitnesseTokenType.WHITE_SPACE, " "),
              Leaf(FitnesseTokenType.WORD, "field")
            ))
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("|scenario|stuff|param1|\n|foo field|bar field|")
    }
  }

  test("import table") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.IMPORT_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.WHITE_SPACE, " "),
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "import")
            )),
            Leaf(FitnesseTokenType.WHITE_SPACE, " ")
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.WHITE_SPACE, " "),
            Node(FitnesseElementType.CELL,List(
              Leaf(FitnesseTokenType.WORD, "fixtures")
            )),
            Leaf(FitnesseTokenType.WHITE_SPACE, " ")
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("| import |\n| fixtures |")
    }
  }

  test("library table") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.LIBRARY_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.WHITE_SPACE, " "),
            Node(FitnesseElementType.TABLE_TYPE, List(
              Leaf(FitnesseTokenType.WORD, "library")
            )),
            Leaf(FitnesseTokenType.WHITE_SPACE, " ")
          )),
          Leaf(FitnesseTokenType.ROW_END, "|\n|"),
          Node(FitnesseElementType.ROW, List(
            Leaf(FitnesseTokenType.WHITE_SPACE, " "),
            Node(FitnesseElementType.CELL, List(
              Leaf(FitnesseTokenType.WORD, "fixtures")
            )),
            Leaf(FitnesseTokenType.WHITE_SPACE, " ")
          ))
        )),
        Leaf(FitnesseTokenType.TABLE_END, "|")
      ))
    ) {
      parse("| library |\n| fixtures |")
    }
  }

  test("partial table") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
        Node(TableElementType.UNKNOWN_TABLE, List(
          Leaf(FitnesseTokenType.TABLE_START, "|"),
          Node(FitnesseElementType.ROW, List(
          ))
        ))
      ))
    ) {
      parse("|")
    }
  }

  test("partial table, editing second cell") {
    assertResult(
      Node(FitnesseElementType.FILE, List(
//        Node(TableElementType.UNKNOWN_TABLE, List(
//          Leaf(FitnesseTokenType.TABLE_START, "|"),
//          Node(FitnesseElementType.ROW, List(
//            Node(FitnesseElementType.TABLE_TYPE, List(
//              Leaf(FitnesseTokenType.WORD, "script")
//            )),
//            Leaf(FitnesseTokenType.CELL_END, "|")
//          ))
//        ))
        Leaf(FitnesseTokenType.WORD,"|script|"),
        Leaf(FitnesseTokenType.WHITE_SPACE, " "),
        Leaf(FitnesseTokenType.WORD, "foo")
      ))
    ) {
      parse("|script| foo")
    }
  }

}