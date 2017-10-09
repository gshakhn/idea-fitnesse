package fitnesse.idea.lexer

class RegularTextLexerSuite extends LexerSuite {
  test("Single word") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, 0, 5, "Hello")
      )) {
      lexWithOffset("Hello")
    }
  }

  test("Two words") {
    assertResult(
      List(
        (FitnesseTokenType.WORD,        0, 3, "Foo"),
        (FitnesseTokenType.WHITE_SPACE, 3, 4, " "),
        (FitnesseTokenType.WORD,        4, 7, "bar")
      )) {
      lexWithOffset("Foo bar")
    }
  }

  test("Number") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, 0, 3, "123")
      )) {
      lexWithOffset("123")
    }
  }

  test("CamelCase word where first letter is lower case") {
    assertResult(List(
      (FitnesseTokenType.WORD, 0, 15, "thisIsCamelCase")

    )) {
      lexWithOffset("thisIsCamelCase")
    }
  }

  test("Word that looks like a WikiWord but has 2 capital letters in a row ") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, 0, 18, "ThisIsNotAWikiWord")
      )) {
      lexWithOffset("ThisIsNotAWikiWord")
    }
  }

  test("USAforEver is a regular word") {
    assertResult(
      List(
        (FitnesseTokenType.WORD, 0, 10, "USAforEver")
      )) {
      lexWithOffset("USAforEver")
    }
  }

  test("A sentence") {
    assertResult(
      List(
        (FitnesseTokenType.WORD,        0, 4, "This"),
        (FitnesseTokenType.WHITE_SPACE, 4, 5, " "),
        (FitnesseTokenType.WORD,        5, 7, "is"),
        (FitnesseTokenType.WHITE_SPACE, 7, 8, " "),
        (FitnesseTokenType.WORD,        8, 9, "a"),
        (FitnesseTokenType.WHITE_SPACE, 9, 10, " "),
        (FitnesseTokenType.WORD,        10, 19, "sentence.")
      )) {
      lexWithOffset("This is a sentence.")
    }
  }

  test("italic text") {
    assertResult(
      List(
        (FitnesseTokenType.ITALIC, 0, 13, "''some text''")
      )) {
      lexWithOffset("''some text''")
    }
  }

  test("bold text") {
    assertResult(
      List(
        (FitnesseTokenType.BOLD, 0, 15, "'''some text'''")
      )) {
      lexWithOffset("'''some text'''")
    }
  }

  test("bold-italic text") {
    assertResult(
      List(
        (FitnesseTokenType.BOLD, 0, 19, "'''''some text'''''")
      )) {
      lexWithOffset("'''''some text'''''")
    }
  }
}
