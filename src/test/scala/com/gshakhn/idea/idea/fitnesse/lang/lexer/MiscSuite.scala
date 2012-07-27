package com.gshakhn.idea.idea.fitnesse.lang.lexer

class MiscSuite extends LexerSuite {
  test("Regular text followed by Wiki Word") {
    doTest("Some text and WikiWord",
      List(
        ("Fitnesse:REGULAR_TEXT", "S"),
        ("Fitnesse:REGULAR_TEXT", "o"),
        ("Fitnesse:REGULAR_TEXT", "m"),
        ("Fitnesse:REGULAR_TEXT", "e"),
        ("Fitnesse:REGULAR_TEXT", " "),
        ("Fitnesse:REGULAR_TEXT", "t"),
        ("Fitnesse:REGULAR_TEXT", "e"),
        ("Fitnesse:REGULAR_TEXT", "x"),
        ("Fitnesse:REGULAR_TEXT", "t"),
        ("Fitnesse:REGULAR_TEXT", " "),
        ("Fitnesse:REGULAR_TEXT", "a"),
        ("Fitnesse:REGULAR_TEXT", "n"),
        ("Fitnesse:REGULAR_TEXT", "d"),
        ("Fitnesse:REGULAR_TEXT", " "),
        ("Fitnesse:WIKI_WORD", "WikiWord")
      ))
  }
}
