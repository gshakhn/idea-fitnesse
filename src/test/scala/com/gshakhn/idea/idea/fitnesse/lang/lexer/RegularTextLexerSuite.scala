package com.gshakhn.idea.idea.fitnesse.lang.lexer

class RegularTextLexerSuite extends LexerSuite {
  test("Single word") {
    expect(
      List(
        (FitnesseTokenType.WORD, "Hello")
      )) {
      lex("Hello")
    }
  }

  test("Single word with number") {
    expect(
      List(
        (FitnesseTokenType.WORD, "Hello1")
      )) {
      lex("Hello1")
    }
  }

  test("Two words") {
    expect(
      List(
        (FitnesseTokenType.WORD, "Foo"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "bar")
      )) {
      lex("Foo bar")
    }
  }

  test("Number") {
    expect(
      List(
        (FitnesseTokenType.WORD, "123")
      )) {
      lex("123")
    }
  }

  test("CamelCase word where first letter is lower case") {
    expect(List(
      (FitnesseTokenType.WORD, "thisIsCamelCase")

    )) {
      lex("thisIsCamelCase")
    }
  }

  test("Word that looks like a WikiWord but has 2 capital letters in a row ") {
    expect(
      List(
        (FitnesseTokenType.WORD, "ThisIsNotAWikiWord")
      )) {
      lex("ThisIsNotAWikiWord")
    }
  }

  test("USAforEver is a regular word") {
    expect(
      List(
        (FitnesseTokenType.WORD, "USAforEver")
      )) {
      lex("USAforEver")
    }
  }

  test("A sentence") {
    expect(
      List(
        (FitnesseTokenType.WORD, "This"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "is"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "a"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "sentence"),
        (FitnesseTokenType.PERIOD, ".")
      )) {
      lex("This is a sentence.")
    }
  }
}
