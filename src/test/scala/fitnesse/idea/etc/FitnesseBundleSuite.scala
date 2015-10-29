package fitnesse.idea.etc

import org.scalatest.FunSuite

class FitnesseBundleSuite extends FunSuite {

  test("bundle is loaded") {
    assertResult("FitNesse") {
      FitnesseBundle.message("plugin.name")
    }
  }
}
