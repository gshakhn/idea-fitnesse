package com.gshakhn.idea.idea.fitnesse.lang.lexer

import org.scalatest.FunSuite
import com.intellij.lexer.Lexer

class FitnesseLexerTest extends FunSuite {

  test("WikiWord") {
    doTest("WikiWord", List(("Fitnesse:WIKI_WORD", "WikiWord")))
  }

  private def doTest(text: String, expectedTokens: List[Tuple2[String, String]], lexer: Lexer = new FitnesseLexer) {
    lexer.start(text)

    var idx: Int = 0
    while (lexer.getTokenType != null) {
      if (idx >= expectedTokens.length) fail("Too many tokens")

      val expectedToken: Tuple2[String, String] = expectedTokens(idx)
      idx = idx + 1

      val tokenName: String = lexer.getTokenType.toString
      val tokenText: String = lexer.getBufferSequence.subSequence(lexer.getTokenStart, lexer.getTokenEnd).toString

      expect(expectedToken._1) { tokenName}
      expect(expectedToken._2) { tokenText}
      lexer.advance()
    }

    if (idx < expectedTokens.length) fail("Not enough tokens")
  }
}