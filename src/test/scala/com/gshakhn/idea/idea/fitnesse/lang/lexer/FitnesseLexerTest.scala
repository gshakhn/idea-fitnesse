package com.gshakhn.idea.idea.fitnesse.lang.lexer

import org.scalatest.FunSuite
import com.intellij.lexer.Lexer

class FitnesseLexerTest extends FunSuite {

  test("Regular Wiki Word") {
    doTest("WikiWord", List(("Fitnesse:WIKI_WORD", "WikiWord")))
  }

  test("Wiki Word with numbers") {
    doTest("WikiWord123", List(("Fitnesse:WIKI_WORD", "WikiWord123")))
  }

  test("Longer Wiki Word") {
    doTest("LongerWikiWord", List(("Fitnesse:WIKI_WORD", "LongerWikiWord")))
  }

  test("Single word") {
    doTest("Hello",
      List(("Fitnesse:REGULAR_TEXT", "H"),
        ("Fitnesse:REGULAR_TEXT", "e"),
        ("Fitnesse:REGULAR_TEXT", "l"),
        ("Fitnesse:REGULAR_TEXT", "l"),
        ("Fitnesse:REGULAR_TEXT", "o")
      ))
  }

  test("Single word with number") {
    doTest("Hello1",
      List(("Fitnesse:REGULAR_TEXT", "H"),
        ("Fitnesse:REGULAR_TEXT", "e"),
        ("Fitnesse:REGULAR_TEXT", "l"),
        ("Fitnesse:REGULAR_TEXT", "l"),
        ("Fitnesse:REGULAR_TEXT", "o"),
        ("Fitnesse:REGULAR_TEXT", "1")
      ))
  }

  test("Two words") {
    doTest("Foo bar",
      List(("Fitnesse:REGULAR_TEXT", "F"),
        ("Fitnesse:REGULAR_TEXT", "o"),
        ("Fitnesse:REGULAR_TEXT", "o"),
        ("Fitnesse:REGULAR_TEXT", " "),
        ("Fitnesse:REGULAR_TEXT", "b"),
        ("Fitnesse:REGULAR_TEXT", "a"),
        ("Fitnesse:REGULAR_TEXT", "r")
      ))
  }

  test("Number") {
    doTest("123",
      List(("Fitnesse:REGULAR_TEXT", "1"),
        ("Fitnesse:REGULAR_TEXT", "2"),
        ("Fitnesse:REGULAR_TEXT", "3")
      ))
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

      expect(expectedToken._1) {
        tokenName
      }
      expect(expectedToken._2) {
        tokenText
      }
      lexer.advance()
    }

    if (idx < expectedTokens.length) fail("Not enough tokens")
  }
}