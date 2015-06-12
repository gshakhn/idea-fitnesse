package fitnesse.idea.scripttable

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs._
import com.intellij.psi.{PsiElement, StubBasedPsiElement}
import fitnesse.idea.fixturemethod.FixtureMethodIndex
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.{Cell, ScalaFriendlyStubBasedPsiElementBase}
import fitnesse.testsystems.slim.tables.Disgracer._

import scala.collection.JavaConversions._


trait ScenarioNameStub extends StubElement[ScenarioName] {
  def getName: String
}


trait ScenarioName extends StubBasedPsiElement[ScenarioNameStub] {
  /**
   * Get a "normalized" (camel case) version of the scenario name
   * @return
   */
  def scenarioName: String
  def getName: String
}


class ScenarioNameStubImpl(parent: StubElement[_ <: PsiElement], methodName: String) extends StubBase[ScenarioName](parent, ScenarioNameElementTypeHolder.INSTANCE) with ScenarioNameStub {
  override def getName: String = methodName
}


class ScenarioNameImpl extends ScalaFriendlyStubBasedPsiElementBase[ScenarioNameStub] with ScenarioName {

  def this(node: ASTNode) = { this(); init(node) }
  def this(stub: ScenarioNameStub) = { this(); init(stub) }

  override def scenarioName =
    disgraceClassName(getName)

  override def getName = dispatch match {
    case STUB => getStub.getName
    case NODE =>
      val snippets = getCells.zipWithIndex.filter(_._2 % 2 == 0).map(_._1)
      val texts = snippets.map(_.getText.trim)

      println("getName: " + texts)
      texts match {
        case Nil => ""
        case h :: _ if h.contains("_") => h.replaceAll("\\s*_\\s*", " ")
        case t => t.mkString(" ")
      }
  }

  def getCells: List[Cell] = findChildrenByType(FitnesseElementType.CELL).toList
}


class ScenarioNameIndex extends StringStubIndexExtension[ScenarioName] {
  override def getKey: StubIndexKey[String, ScenarioName] = ScenarioNameIndex.KEY

  override def getVersion: Int = 2
}


object ScenarioNameIndex {
  val KEY: StubIndexKey[String, ScenarioName] = StubIndexKey.createIndexKey("fitnesse.scenarioName.index")

  val INSTANCE = new ScenarioNameIndex
}


class ScenarioNameElementType(debugName: String) extends IStubElementType[ScenarioNameStub, ScenarioName](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.scenarioName"

  override def createStub(psi: ScenarioName, parentStub: StubElement[_ <: PsiElement]): ScenarioNameStub = new ScenarioNameStubImpl(parentStub, psi.getName)

  override def createPsi(stub: ScenarioNameStub): ScenarioName = new ScenarioNameImpl(stub)

  override def indexStub(stub: ScenarioNameStub, sink: IndexSink): Unit = {
    val methodName = disgraceClassName(stub.getName)
    println("Add to scenario index: " + methodName)
    sink.occurrence(ScenarioNameIndex.KEY, methodName)
  }

  override def serialize(t: ScenarioNameStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.getName)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): ScenarioNameStub = {
    val ref = stubInputStream.readName
    new ScenarioNameStubImpl(parentStub, ref.getString)
  }
}


object ScenarioNameElementTypeHolder {
  val INSTANCE: IStubElementType[ScenarioNameStub, ScenarioName] = new ScenarioNameElementType("SCENARIO_NAME")
}