package com.gshakhn.idea.idea.fitnesse.lang.lexer

class RegularTextLexerSuite extends LexerSuite {
  test("Single word") {
    expectResult(
      List(
        (FitnesseTokenType.WORD, "Hello")
      )) {
      lex("Hello")
    }
  }

  test("Single word with number") {
    expectResult(
      List(
        (FitnesseTokenType.WORD, "Hello1")
      )) {
      lex("Hello1")
    }
  }

  test("Two words") {
    expectResult(
      List(
        (FitnesseTokenType.WORD, "Foo"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "bar")
      )) {
      lex("Foo bar")
    }
  }

  test("Number") {
    expectResult(
      List(
        (FitnesseTokenType.WORD, "123")
      )) {
      lex("123")
    }
  }

  test("CamelCase word where first letter is lower case") {
    expectResult(List(
      (FitnesseTokenType.WORD, "thisIsCamelCase")

    )) {
      lex("thisIsCamelCase")
    }
  }

  test("Word that looks like a WikiWord but has 2 capital letters in a row ") {
    expectResult(
      List(
        (FitnesseTokenType.WORD, "ThisIsNotAWikiWord")
      )) {
      lex("ThisIsNotAWikiWord")
    }
  }

  test("USAforEver is a regular word") {
    expectResult(
      List(
        (FitnesseTokenType.WORD, "USAforEver")
      )) {
      lex("USAforEver")
    }
  }

  test("A sentence") {
    expectResult(
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
