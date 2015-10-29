package fitnesse.idea.scenariotable

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs._
import com.intellij.psi.{PsiElement, StubBasedPsiElement}
import fitnesse.idea.filetype.FitnesseLanguage
import fitnesse.idea.parser.FitnesseElementType
import fitnesse.idea.psi.{ScalaFriendlyStubBasedPsiElementBase, StubBasedPsiElementBase2}
import fitnesse.idea.table.Cell
import fitnesse.testsystems.slim.tables.Disgracer._

import scala.collection.JavaConversions._


trait ScenarioNameStub extends StubElement[ScenarioName] {
  def name: String
  def arguments: List[String]
}


trait ScenarioName extends StubBasedPsiElement[ScenarioNameStub] {
  /**
   * Get a "normalized" (camel case) version of the scenario name
   * @return
   */
  def scenarioName: String
  def name: String
  def arguments: List[String]
}


class ScenarioNameStubImpl(parent: StubElement[_ <: PsiElement], _name: String, _arguments: List[String]) extends StubBase[ScenarioName](parent, ScenarioNameElementType.INSTANCE) with ScenarioNameStub {
  override def name: String = _name
  override def arguments: List[String] = _arguments
}


trait ScenarioNameImpl extends ScalaFriendlyStubBasedPsiElementBase[ScenarioNameStub] with ScenarioName {
  this: StubBasedPsiElementBase2[ScenarioNameStub] =>

  override def scenarioName =
    disgraceClassName(name)

  override def arguments = source match {
    case STUB => getStub.arguments
    case NODE => cells match {
      case name :: args :: Nil if name.getText.contains("_") =>
        args.getText.split(",").map(_.trim).toList
      case cells => cells.zipWithIndex
        .filter { case (_, index) => index % 2 == 1 }
        .map { case (cell, _) => cell.getText.trim }
    }
  }

  override def name = source match {
    case STUB => getStub.name
    case NODE =>
      cells.zipWithIndex
        .filter { case (_, index) => index % 2 == 0 }
        .map { case (cell, _) => cell.getText.trim } match {
          case Nil => ""
          case h :: _ if h.contains("_") => h.replaceAll("\\s*_\\s*", " ")
          case t => t.mkString(" ")
        }
  }

  def cells: List[Cell] = findChildrenByType(FitnesseElementType.CELL).toList
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

  override def createStub(psi: ScenarioName, parentStub: StubElement[_ <: PsiElement]): ScenarioNameStub = new ScenarioNameStubImpl(parentStub, psi.name, psi.arguments)

  override def createPsi(stub: ScenarioNameStub): ScenarioName = ScenarioNameImpl(stub)

  override def indexStub(stub: ScenarioNameStub, sink: IndexSink): Unit = {
    sink.occurrence(ScenarioNameIndex.KEY, disgraceClassName(stub.name))
    sink.occurrence(ScenarioNameIndex.KEY, disgraceMethodName(stub.name))
  }

  override def serialize(t: ScenarioNameStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.name)
    stubOutputStream.writeName(t.arguments.mkString("|"))
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): ScenarioNameStub = {
    val nameRef = stubInputStream.readName
    val argsRef = stubInputStream.readName
    new ScenarioNameStubImpl(parentStub, nameRef.getString, argsRef.getString.split("\\|").toList)
  }
}


object ScenarioNameElementType {
  val INSTANCE: IStubElementType[ScenarioNameStub, ScenarioName] = new ScenarioNameElementType("SCENARIO_NAME")
}