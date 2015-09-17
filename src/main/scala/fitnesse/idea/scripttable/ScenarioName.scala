package fitnesse.idea.scripttable

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs._
import com.intellij.psi.{PsiElement, StubBasedPsiElement}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.{Cell, ScalaFriendlyStubBasedPsiElementBase, StubBasedPsiElementBase2}
import fitnesse.testsystems.slim.tables.Disgracer._

import scala.collection.JavaConversions._


trait ScenarioNameStub extends StubElement[ScenarioName] {
  def getName: String
  def getArguments: List[String]
}


trait ScenarioName extends StubBasedPsiElement[ScenarioNameStub] {
  /**
   * Get a "normalized" (camel case) version of the scenario name
   * @return
   */
  def scenarioName: String
  def getName: String
  def getArguments: List[String]
}


class ScenarioNameStubImpl(parent: StubElement[_ <: PsiElement], name: String, arguments: List[String]) extends StubBase[ScenarioName](parent, ScenarioNameElementType.INSTANCE) with ScenarioNameStub {
  override def getName: String = name
  override def getArguments: List[String] = arguments
}


trait ScenarioNameImpl extends ScalaFriendlyStubBasedPsiElementBase[ScenarioNameStub] with ScenarioName {
  this: StubBasedPsiElementBase2[ScenarioNameStub] =>

  override def scenarioName =
    disgraceClassName(getName)

  override def getArguments = source match {
    case STUB => getStub.getArguments
    case NODE =>
      getCells.zipWithIndex
        .filter{ case (_, index) => index % 2 == 1 }
        .map{ case (cell, _) => cell.getText.trim }
  }

  override def getName = source match {
    case STUB => getStub.getName
    case NODE =>
      getCells.zipWithIndex
        .filter{ case (_, index) => index % 2 == 0 }
        .map{ case (cell, _) => cell.getText.trim } match {
          case Nil => ""
          case h :: _ if h.contains("_") => h.replaceAll("\\s*_\\s*", " ")
          case t => t.mkString(" ")
        }
  }

  def getCells: List[Cell] = findChildrenByType(FitnesseElementType.CELL).toList
}

object ScenarioNameImpl {
  def apply(node: ASTNode) = new StubBasedPsiElementBase2[ScenarioNameStub](node) with ScenarioNameImpl
  def apply(stub: ScenarioNameStub) = new StubBasedPsiElementBase2[ScenarioNameStub](stub, ScenarioNameElementType.INSTANCE) with ScenarioNameImpl
}


class ScenarioNameIndex extends StringStubIndexExtension[ScenarioName] {
  override def getKey: StubIndexKey[String, ScenarioName] = ScenarioNameIndex.KEY

  override def getVersion: Int = 3
}


object ScenarioNameIndex {
  val KEY: StubIndexKey[String, ScenarioName] = StubIndexKey.createIndexKey("fitnesse.scenarioName.index")

  val INSTANCE = new ScenarioNameIndex
}


class ScenarioNameElementType(debugName: String) extends IStubElementType[ScenarioNameStub, ScenarioName](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.scenarioName"

  override def createStub(psi: ScenarioName, parentStub: StubElement[_ <: PsiElement]): ScenarioNameStub = new ScenarioNameStubImpl(parentStub, psi.getName, psi.getArguments)

  override def createPsi(stub: ScenarioNameStub): ScenarioName = ScenarioNameImpl(stub)

  override def indexStub(stub: ScenarioNameStub, sink: IndexSink): Unit = {
    sink.occurrence(ScenarioNameIndex.KEY, disgraceClassName(stub.getName))
    sink.occurrence(ScenarioNameIndex.KEY, disgraceMethodName(stub.getName))
  }

  override def serialize(t: ScenarioNameStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.getName)
    stubOutputStream.writeName(t.getArguments.mkString("|"))
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): ScenarioNameStub = {
    val nameRef = stubInputStream.readName
    val argsRef = stubInputStream.readName
    new ScenarioNameStubImpl(parentStub, nameRef.getString, argsRef.getString.split("|").toList)
  }
}


object ScenarioNameElementType {
  val INSTANCE: IStubElementType[ScenarioNameStub, ScenarioName] = new ScenarioNameElementType("SCENARIO_NAME")
}