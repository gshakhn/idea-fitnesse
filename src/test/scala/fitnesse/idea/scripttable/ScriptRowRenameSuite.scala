package fitnesse.idea.scripttable

import fitnesse.idea.psi.PsiSuite

class ScriptRowRenameSuite extends PsiSuite {

  def scriptRow(s: String): ScriptRow = {
    createTable("| script:MyScriptTable|\n" + s).rows(1).asInstanceOf[ScriptRow]
  }

  test("split method names") {
    assertResult(List("name")) {
      ScriptRowElementType.chopMethodName(scriptRow("| method name |"), "name")
    }

    assertResult(List("method name")) {
      ScriptRowElementType.chopMethodName(scriptRow("| method name |"), "method name")
    }

    assertResult(List("new;")) {
      ScriptRowElementType.chopMethodName(scriptRow("| method | arg | name |"), "new")
    }

    assertResult(List("new", "thing")) {
      ScriptRowElementType.chopMethodName(scriptRow("| method | arg | name |"), "new thing")
    }

    assertResult(List("my", "new thing")) {
      ScriptRowElementType.chopMethodName(scriptRow("| method | arg | name |"), "my new thing")
    }

    assertResult(List("my new thing;")) {
      ScriptRowElementType.chopMethodName(scriptRow("| method; | arg | name |"), "my new thing")
    }
  }

  test("interleave") {
    assertResult(List("method name")) {
      ScriptRowElementType.interleave(List("method name"), List.empty)
    }

    assertResult(List("method", "arg", "name")) {
      ScriptRowElementType.interleave(List("method", "name"), List("arg"))
    }

    assertResult(List("method", "arg1", "name", "arg2")) {
      ScriptRowElementType.interleave(List("method", "name"), List("arg1", "arg2"))
    }
  }

  test("rename simple script row") {
    val row = scriptRow("| method name |")
    val newRow: ScriptRow = ScriptRowElementType.createScriptRow(row, "new name")

    assertResult("new name") {
      newRow.name
    }
    assertResult(List.empty) {
      newRow.parameters
    }

    assertResult("new name") {
      newRow.getText
    }
  }

  test("rename script row with argument") {
    val row = scriptRow("| method name | arg |")
    val newRow: ScriptRow = ScriptRowElementType.createScriptRow(row, "new name")

    assertResult("new name") {
      newRow.name
    }
    assertResult(List("arg")) {
      newRow.parameters
    }
    assertResult("new name|arg") {
      newRow.getText
    }
  }

  test("rename script row with split name and argument") {
    val row = scriptRow("| method | arg | name |")
    val newRow: ScriptRow = ScriptRowElementType.createScriptRow(row, "new name")

    assertResult("new name") {
      newRow.name
    }
    assertResult(List("arg")) {
      newRow.parameters
    }
    assertResult("new|arg|name") {
      newRow.getText
    }
  }

  test("rename script row with semi-colon name and argument") {
    val row = scriptRow("| method; | arg | arg2 |")
    val newRow: ScriptRow = ScriptRowElementType.createScriptRow(row, "new name")

    assertResult("new name;") {
      newRow.name
    }
    assertResult(List("arg", "arg2")) {
      newRow.parameters
    }
    assertResult("new name;|arg|arg2") {
      newRow.getText
    }
  }

}
