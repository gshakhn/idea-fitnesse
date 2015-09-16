package fitnesse.idea.scripttable

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs._
import com.intellij.psi._
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod._
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.{StubBasedPsiElementBase2, Cell, Row, ScalaFriendlyStubBasedPsiElementBase}
import fitnesse.testsystems.slim.tables.Disgracer
import fitnesse.testsystems.slim.tables.Disgracer._

import scala.collection.JavaConversions._


trait ScriptRowStub extends StubElement[ScriptRow] {
  def getName: String
}


trait ScriptRow extends StubBasedPsiElement[ScriptRowStub] with Row with FixtureMethod {
  def getName: String
}


class ScriptRowStubImpl(parent: StubElement[_ <: PsiElement], methodName: String) extends StubBase[ScriptRow](parent, ScriptRowElementType.INSTANCE) with ScriptRowStub {
  override def getName: String = methodName
}


trait ScriptRowImpl extends ScalaFriendlyStubBasedPsiElementBase[ScriptRowStub] with ScriptRow {
  this: StubBasedPsiElementBase2[ScriptRowStub] =>

  val symbolAssignment = "\\$\\w+=".r

  override def fixtureMethodName =
    disgraceMethodName(getName)

  override def parameters = processMethod(constructFixtureParameters)

  override def returnType = getCells.map(_.getText.trim) match {
    case ("check" | "check not" ) :: _ => PsiType.getJavaLangString(getManager, getResolveScope)
    case ( "reject" | "ensure") :: _ => PsiType.BOOLEAN
    case ("show" | symbolAssignment()) :: _ => PsiType.getJavaLangObject(getManager, getResolveScope)
    case method => PsiType.BOOLEAN
  }

  override def getName = source match {
    case STUB => getStub.getName
    case NODE => processMethod(constructFixtureName)
  }

  def processMethod[T]( methodHandler: List[String] => T): T = getCells.map(_.getText.trim) match {
      case ("check" | "check not") :: method => methodHandler(method.dropRight(1))
      case ("start" | "reject" | "ensure" | "show") :: method => methodHandler(method)
      case symbolAssignment() :: method => methodHandler(method)
      case method => methodHandler(method)
    }

  private def constructFixtureName(parts: List[String]) = parts match {
    case Nil => ""
    case head :: _ if head.endsWith(";") => head
    case _ => parts.zipWithIndex
      .filter{ case (_, index) => index % 2 == 0 }
      .map{ case (text, _) => text }
      .mkString(" ")
  }

  private def constructFixtureParameters(parts: List[String]) = parts match {
    case Nil => Nil
    case head :: params if head.endsWith(";") => params.map(Disgracer.disgraceMethodName)
    case _ => parts.zipWithIndex
      .filter{ case (_, index) => index % 2 == 1 }
      .map{ case (text, _) => Disgracer.disgraceMethodName(text) }
  }

  override def getReference = new MethodOrScenarioReference(this)

  override def getCells: List[Cell] = findChildrenByType(FitnesseElementType.CELL).toList

  override def getFixtureClass: Option[FixtureClass] = getTable.getFixtureClass

  override def findInRow[T](clazz: Class[T]): T = findChildByClass(clazz)
}

object ScriptRowImpl {
  def apply(node: ASTNode) = new StubBasedPsiElementBase2[ScriptRowStub](node) with ScriptRowImpl
  def apply(stub: ScriptRowStub) = new StubBasedPsiElementBase2[ScriptRowStub](stub, ScriptRowElementType.INSTANCE) with ScriptRowImpl
}

class ScriptRowElementType(debugName: String) extends IStubElementType[ScriptRowStub, ScriptRow](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.scriptRow"

  override def createStub(psi: ScriptRow, parentStub: StubElement[_ <: PsiElement]): ScriptRowStub = new ScriptRowStubImpl(parentStub, psi.getName)

  override def createPsi(stub: ScriptRowStub): ScriptRow = ScriptRowImpl(stub)

  override def indexStub(stub: ScriptRowStub, sink: IndexSink): Unit = {
    val methodName = disgraceMethodName(stub.getName)
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


object ScriptRowElementType {
  val INSTANCE: IStubElementType[ScriptRowStub, ScriptRow] = new ScriptRowElementType("SCRIPT_ROW")
}