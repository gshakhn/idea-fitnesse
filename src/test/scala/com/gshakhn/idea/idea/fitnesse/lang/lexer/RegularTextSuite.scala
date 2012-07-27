package com.gshakhn.idea.idea.fitnesse.lang.lexer

class RegularTextSuite extends LexerSuite {
  test("Single word") {
    doTest("Hello",
      List(
        ("Fitnesse:REGULAR_TEXT", "H"),
        ("Fitnesse:REGULAR_TEXT", "e"),
        ("Fitnesse:REGULAR_TEXT", "l"),
        ("Fitnesse:REGULAR_TEXT", "l"),
        ("Fitnesse:REGULAR_TEXT", "o")
      ))
  }

  test("Single word with number") {
    doTest("Hello1",
      List(
        ("Fitnesse:REGULAR_TEXT", "H"),
        ("Fitnesse:REGULAR_TEXT", "e"),
        ("Fitnesse:REGULAR_TEXT", "l"),
        ("Fitnesse:REGULAR_TEXT", "l"),
        ("Fitnesse:REGULAR_TEXT", "o"),
        ("Fitnesse:REGULAR_TEXT", "1")
      ))
  }

  test("Two words") {
    doTest("Foo bar",
      List(
        ("Fitnesse:REGULAR_TEXT", "F"),
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
      List(
        ("Fitnesse:REGULAR_TEXT", "1"),
        ("Fitnesse:REGULAR_TEXT", "2"),
        ("Fitnesse:REGULAR_TEXT", "3")
      ))
  }
}
