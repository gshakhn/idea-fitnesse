package com.gshakhn.idea.idea.fitnesse.lang.lexer

class RegularTextSuite extends LexerSuite {
  test("Single word") {
    expect(
      List(
        (FitnesseElementType.WORD, "Hello")
      )) {
      lex("Hello")
    }
  }

  test("Single word with number") {
    expect(
      List(
        (FitnesseElementType.WORD, "Hello1")
      )) {
      lex("Hello1")
    }
  }

  test("Two words") {
    expect(
      List(
        (FitnesseElementType.WORD, "Foo"),
        (FitnesseElementType.WHITE_SPACE, " "),
        (FitnesseElementType.WORD, "bar")
      )) {
      lex("Foo bar")
    }
  }

  test("Number") {
    expect(
      List(
        (FitnesseElementType.WORD, "123")
      )) {
      lex("123")
    }
  }

  test("CamelCase word where first letter is lower case") {
    expect(List(
      (FitnesseElementType.WORD, "thisIsCamelCase")

    )) {
      lex("thisIsCamelCase")
    }
  }

  test("Word that looks like a WikiWord but has 2 capital letters in a row ") {
    expect(
      List(
        (FitnesseElementType.WORD, "ThisIsNotAWikiWord")
      )) {
      lex("ThisIsNotAWikiWord")
    }
  }

  test("USAforEver is a regular word") {
    expect(
      List(
        (FitnesseElementType.WORD, "USAforEver")
      )) {
      lex("USAforEver")
    }
  }
}
