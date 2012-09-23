package com.gshakhn.idea.idea.fitnesse.lang.parser

class TableParserSuite extends ParserSuite {
  test("Simple table") {
    expect(null) {
      parse("|A|\n|B|\n")
    }
  }
}