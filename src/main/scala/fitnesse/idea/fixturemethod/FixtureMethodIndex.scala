package fitnesse.idea.fixturemethod

import com.intellij.psi.stubs.{StubIndexKey, StringStubIndexExtension}


class FixtureMethodIndex extends StringStubIndexExtension[MethodReferences] {
  override def getKey: StubIndexKey[String, MethodReferences] = FixtureMethodIndex.KEY

  override def getVersion: Int = 7
}


object FixtureMethodIndex {
  val KEY: StubIndexKey[String, MethodReferences] = StubIndexKey.createIndexKey("fitnesse.fixtureMethod.index")

  val INSTANCE = new FixtureMethodIndex
}
