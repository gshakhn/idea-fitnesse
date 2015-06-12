package fitnesse.idea.scripttable

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs._
import com.intellij.psi.{PsiMethod, PsiReference, PsiElement, StubBasedPsiElement}
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod.{MethodReference, ScenarioReferences, FixtureMethodIndex, MethodReferences}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.{Cell, Row, ScalaFriendlyStubBasedPsiElementBase}
import fitnesse.testsystems.slim.tables.Disgracer._

import scala.collection.JavaConversions._


trait ScriptRowStub extends StubElement[ScriptRow] {
  def getName: String
}


trait ScriptRow extends StubBasedPsiElement[ScriptRowStub] with Row {
  def fixtureMethodName: String
  def getName: String
}


class ScriptRowStubImpl(parent: StubElement[_ <: PsiElement], methodName: String) extends StubBase[ScriptRow](parent, ScriptRowElementTypeHolder.INSTANCE) with ScriptRowStub {
  override def getName: String = methodName
}


class ScriptRowImpl extends ScalaFriendlyStubBasedPsiElementBase[ScriptRowStub] with ScriptRow with MethodReferences with ScenarioReferences {

  def this(node: ASTNode) = { this(); init(node) }
  def this(stub: ScriptRowStub) = { this(); init(stub) }

  override def fixtureMethodName =
    disgraceMethodName(getName)

  override def getName = source match {
    case STUB => getStub.getName
    case NODE =>
      val texts = getCells.map(_.getText.trim)

      def constructFixtureName(parts: List[String]) = {
        if (parts.isEmpty)
          ""
        else if (parts.head.endsWith(";"))
          parts.head
        else
          parts.view.zipWithIndex.filter(_._2 % 2 == 0).map(_._1).mkString(" ")
      }

      texts match {
        case ("check" | "check not") :: method => constructFixtureName(method.dropRight(1))
        case ("script" | "start" | "reject" | "ensure" | "show" | "note") :: method => constructFixtureName(method)
        case method => constructFixtureName(method)
      }
  }

  // TODO: prefer scenarios over fixture methods, once we know the parent page path.
  override def getReferences: Array[PsiReference] = (getReferencedScenarios ++ getReferencedMethods).toArray

  private def createReference(psiMethod: PsiMethod): MethodReference = new MethodReference(psiMethod, this)

  override def getCells: List[Cell] = findChildrenByType(FitnesseElementType.CELL).toList

  override def getFixtureClass: Option[FixtureClass] = getTable.getFixtureClass

  override def findChildByClass[T](clazz: Class[T]): T = super.findChildByClass(clazz)
}



class ScriptRowElementType(debugName: String) extends IStubElementType[ScriptRowStub, ScriptRow](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.scriptRow"

  override def createStub(psi: ScriptRow, parentStub: StubElement[_ <: PsiElement]): ScriptRowStub = new ScriptRowStubImpl(parentStub, psi.getName)

  override def createPsi(stub: ScriptRowStub): ScriptRow = new ScriptRowImpl(stub)

  override def indexStub(stub: ScriptRowStub, sink: IndexSink): Unit = {
    val methodName = disgraceMethodName(stub.getName)
    println("Add to index: " + methodName)
    sink.occurrence(FixtureMethodIndex.KEY, methodName)
  }

  override def serialize(t: ScriptRowStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.getName)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): ScriptRowStub = {
    val ref = stubInputStream.readName
    new ScriptRowStubImpl(parentStub, ref.getString)
  }
}


object ScriptRowElementTypeHolder {
  val INSTANCE: IStubElementType[ScriptRowStub, ScriptRow] = new ScriptRowElementType("SCRIPT_ROW")
}