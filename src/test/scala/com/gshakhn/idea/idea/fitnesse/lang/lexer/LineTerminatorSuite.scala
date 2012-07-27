package com.gshakhn.idea.idea.fitnesse.lang.lexer

class LineTerminatorSuite extends LexerSuite {
  test("LF") {
    doTest("\n", List(("Fitnesse:LINE_TERMINATOR", "\n")))
  }

  test("CR LF") {
    doTest("\r\n", List(("Fitnesse:LINE_TERMINATOR", "\r\n")))
  }
}
