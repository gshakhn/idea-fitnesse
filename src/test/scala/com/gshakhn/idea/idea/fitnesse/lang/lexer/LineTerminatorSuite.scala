package com.gshakhn.idea.idea.fitnesse.lang.lexer

class LineTerminatorSuite extends LexerSuite {
  test("LF") {
    expect(List((FitnesseElementType.LINE_TERMINATOR, "\n"))) {
      lex("\n")
    }
  }

  test("CR LF") {
    expect(List((FitnesseElementType.LINE_TERMINATOR, "\r\n"))) {
      lex("\r\n")
    }
  }
}
