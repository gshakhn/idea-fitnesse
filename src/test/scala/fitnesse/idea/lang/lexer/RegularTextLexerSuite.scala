package fitnesse.idea.lang.lexer

class RegularTextLexerSuite extends LexerSuite {
  test("Single word") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "Hello")
      )) {
      lex("Hello")
    }
  }

  test("Single word with number") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "Hello1")
      )) {
      lex("Hello1")
    }
  }

  test("Two words") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "Foo"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "bar")
      )) {
      lex("Foo bar")
    }
  }

  test("Number") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "123")
      )) {
      lex("123")
    }
  }

  test("CamelCase word where first letter is lower case") {
    assertResult(List(
      (FitnesseTokenType.WORD, "thisIsCamelCase")

    )) {
      lex("thisIsCamelCase")
    }
  }

  test("Word that looks like a WikiWord but has 2 capital letters in a row ") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "ThisIsNotAWikiWord")
      )) {
      lex("ThisIsNotAWikiWord")
    }
  }

  test("USAforEver is a regular word") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "USAforEver")
      )) {
      lex("USAforEver")
    }
  }

  test("A sentence") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, "This"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "is"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "a"),
        (FitnesseTokenType.WHITE_SPACE, " "),
        (FitnesseTokenType.WORD, "sentence.")
      )) {
      lex("This is a sentence.")
    }
  }
}
