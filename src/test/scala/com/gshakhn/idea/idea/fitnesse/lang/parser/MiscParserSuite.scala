package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType

class MiscParserSuite extends ParserSuite {

  test("Period followed by word") {
    expectResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.PERIOD, "."),
        Leaf(FitnesseTokenType.WORD, "Hello")
      ))
    ) {
      parse(".Hello")
    }
  }

  test("> followed by word") {
    expectResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.GT, ">"),
        Leaf(FitnesseTokenType.WORD, "Hello")
      ))
    ) {
      parse(">Hello")
    }
  }

  test("< followed by word") {
    expectResult(
      Node(FitnesseElementType.FILE, List(
        Leaf(FitnesseTokenType.LT, "<"),
        Leaf(FitnesseTokenType.WORD, "Hello")
      ))
    ) {
      parse("<Hello")
    }
  }
}
