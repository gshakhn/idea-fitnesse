package com.gshakhn.idea.idea.fitnesse.lang.lexer

class LineTerminatorLexerSuite extends LexerSuite {
  test("LF") {
    assertResult(List((FitnesseTokenType.LINE_TERMINATOR, "\n"))) {
      lex("\n")
    }
  }

  test("CR LF") {
    assertResult(List((FitnesseTokenType.LINE_TERMINATOR, "\r\n"))) {
      lex("\r\n")
    }
  }
}
