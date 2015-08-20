package fitnesse.idea.lang

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

  test("method name regrace") {
    assertResult("method name") {
      Regracer.regrace("methodName")
    }
  }

}
