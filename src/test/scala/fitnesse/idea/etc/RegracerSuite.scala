package fitnesse.idea.etc

import fitnesse.testsystems.slim.tables.Disgracer
import org.scalatest.FunSuite

class RegracerSuite extends FunSuite {

  test("class name regrace") {
    assertResult("class name") {
      Regracer.regrace("ClassName")
    }
  }

  test("class name regrace with capital suffix") {
    assertResult("class name f") {
      Regracer.regrace("ClassNameF")
    }
  }

  test("class name regrace one word") {
    assertResult("Thing") {
      Regracer.regrace("Thing")
    }
  }

  test("method name regrace") {
    assertResult("method name") {
      Regracer.regrace("methodName")
    }
  }

  test("method name regrace with single character words") {
    val s = "I am a coder"
    assertResult(s.toLowerCase) {
      Regracer.regrace(Disgracer.disgraceClassName(s))
    }
    assertResult(s.toLowerCase) {
      Regracer.regrace(Disgracer.disgraceMethodName(s))
    }  }

}
