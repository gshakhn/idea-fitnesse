package com.gshakhn.idea.idea.fitnesse.lang.lexer

class RegularTextSuite extends LexerSuite {
  test("Single word") {
    expect(
      List(
        (FitnesseElementType.REGULAR_TEXT, "H"),
        (FitnesseElementType.REGULAR_TEXT, "e"),
        (FitnesseElementType.REGULAR_TEXT, "l"),
        (FitnesseElementType.REGULAR_TEXT, "l"),
        (FitnesseElementType.REGULAR_TEXT, "o")
      )) {
      lex("Hello")
    }
  }

  test("Single word with number") {
    expect(
      List(
        (FitnesseElementType.REGULAR_TEXT, "H"),
        (FitnesseElementType.REGULAR_TEXT, "e"),
        (FitnesseElementType.REGULAR_TEXT, "l"),
        (FitnesseElementType.REGULAR_TEXT, "l"),
        (FitnesseElementType.REGULAR_TEXT, "o"),
        (FitnesseElementType.REGULAR_TEXT, "1")
      )) {
      lex("Hello1")
    }
  }

  test("Two words") {
    expect(
      List(
        (FitnesseElementType.REGULAR_TEXT, "F"),
        (FitnesseElementType.REGULAR_TEXT, "o"),
        (FitnesseElementType.REGULAR_TEXT, "o"),
        (FitnesseElementType.REGULAR_TEXT, " "),
        (FitnesseElementType.REGULAR_TEXT, "b"),
        (FitnesseElementType.REGULAR_TEXT, "a"),
        (FitnesseElementType.REGULAR_TEXT, "r")
      )) {
      lex("Foo bar")
    }
  }

  test("Number") {
    expect(
      List(
        (FitnesseElementType.REGULAR_TEXT, "1"),
        (FitnesseElementType.REGULAR_TEXT, "2"),
        (FitnesseElementType.REGULAR_TEXT, "3")
      )) {
      lex("123")
    }
  }

  test("CamelCase word where first letter is lower case") {
    expect(List(
      (FitnesseElementType.REGULAR_TEXT, "t"),
      (FitnesseElementType.REGULAR_TEXT, "h"),
      (FitnesseElementType.REGULAR_TEXT, "i"),
      (FitnesseElementType.REGULAR_TEXT, "s"),
      (FitnesseElementType.REGULAR_TEXT, "I"),
      (FitnesseElementType.REGULAR_TEXT, "s"),
      (FitnesseElementType.REGULAR_TEXT, "C"),
      (FitnesseElementType.REGULAR_TEXT, "a"),
      (FitnesseElementType.REGULAR_TEXT, "m"),
      (FitnesseElementType.REGULAR_TEXT, "e"),
      (FitnesseElementType.REGULAR_TEXT, "l"),
      (FitnesseElementType.REGULAR_TEXT, "C"),
      (FitnesseElementType.REGULAR_TEXT, "a"),
      (FitnesseElementType.REGULAR_TEXT, "s"),
      (FitnesseElementType.REGULAR_TEXT, "e")

    )) {
      lex("thisIsCamelCase")
    }
  }
}
