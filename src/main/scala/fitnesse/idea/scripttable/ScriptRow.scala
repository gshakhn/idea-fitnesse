package fitnesse.idea.scripttable

import com.intellij.lang.ASTNode
import com.intellij.psi._
import com.intellij.psi.stubs._
import fitnesse.idea.etc.Regracer
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod._
import fitnesse.idea.filetype.FitnesseLanguage
import fitnesse.idea.parser.FitnesseElementType
import fitnesse.idea.psi.FitnesseElementFactory._
import fitnesse.idea.psi.{ScalaFriendlyStubBasedPsiElementBase, StubBasedPsiElementBase2}
import fitnesse.idea.table.{Cell, Row}
import fitnesse.testsystems.slim.tables.Disgracer
import fitnesse.testsystems.slim.tables.Disgracer._

import scala.collection.JavaConversions._


trait ScriptRowStub extends StubElement[ScriptRow] {
  def name: String
  def parameters: List[String]
}


trait ScriptRow extends StubBasedPsiElement[ScriptRowStub] with Row with FixtureMethod {
  def name: String
}


class ScriptRowStubImpl(parent: StubElement[_ <: PsiElement], methodName: String, _parameters: List[String]) extends StubBase[ScriptRow](parent, ScriptRowElementType.INSTANCE) with ScriptRowStub {
  override def name: String = methodName
  override def parameters: List[String] = _parameters
}


trait ScriptRowImpl extends ScalaFriendlyStubBasedPsiElementBase[ScriptRowStub] with ScriptRow {
  this: StubBasedPsiElementBase2[ScriptRowStub] =>

  val symbolAssignment = "\\$\\w+=".r

  override def fixtureMethodName =
    disgraceMethodName(name)

  override def parameters = source match {
    case STUB => getStub.parameters
    case NODE => processMethod(constructFixtureParameters)
  }

  override def returnType = cells.map(_.getText.trim) match {
    case ("check" | "check not" ) :: _ => PsiType.getJavaLangString(getManager, getResolveScope)
    case ( "reject" | "ensure") :: _ => PsiType.BOOLEAN
    case ("show" | symbolAssignment()) :: _ => PsiType.getJavaLangObject(getManager, getResolveScope)
    case method => PsiType.BOOLEAN
  }

  override def name = source match {
    case STUB => getStub.name
    case NODE => processMethod(constructFixtureName)
  }

  def processMethod[T]( methodHandler: List[String] => T): T = cells.map(_.getText.trim) match {
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

  override def cells: List[Cell] = findChildrenByType(FitnesseElementType.CELL).toList

  override def fixtureClass: Option[FixtureClass] = table.fixtureClass

  override def findInRow[T](clazz: Class[T]): T = findChildByClass(clazz)

  override def getName: String = name

  // Update ASTNode instead?
  override def setName(newName: String): PsiElement = {
    val newElement = ScriptRowElementType.createScriptRow(this, Regracer.regrace(newName))
    this.replace(newElement)
  }
}

object ScriptRowImpl {
  def apply(node: ASTNode) = new StubBasedPsiElementBase2[ScriptRowStub](node) with ScriptRowImpl
  def apply(stub: ScriptRowStub) = new StubBasedPsiElementBase2[ScriptRowStub](stub, ScriptRowElementType.INSTANCE) with ScriptRowImpl
}

class ScriptRowElementType(debugName: String) extends IStubElementType[ScriptRowStub, ScriptRow](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.scriptRow"

  override def createStub(psi: ScriptRow, parentStub: StubElement[_ <: PsiElement]): ScriptRowStub = new ScriptRowStubImpl(parentStub, psi.name, psi.parameters)

  override def createPsi(stub: ScriptRowStub): ScriptRow = ScriptRowImpl(stub)

  override def indexStub(stub: ScriptRowStub, sink: IndexSink): Unit = {
    val methodName = disgraceMethodName(stub.name)
    sink.occurrence(FixtureMethodIndex.KEY, methodName)
  }

  override def serialize(t: ScriptRowStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.name)
    stubOutputStream.writeName(t.parameters.mkString("|"))

  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): ScriptRowStub = {
    val ref = stubInputStream.readName
    val paramRef = stubInputStream.readName
    new ScriptRowStubImpl(parentStub, ref.getString, paramRef.getString.split("\\|").toList)
  }
}


object ScriptRowElementType {

  def interleave(methodNames: List[String], parameters: List[String]): List[String] = {
    val lists: List[List[String]] = List(methodNames, parameters)
    lists.map(_.map(Some(_)).padTo(lists.map(_.length).max, None)).transpose.flatten.flatten
  }


  def chopMethodName(scriptRow: ScriptRow, methodName: String): List[String] = {
    val impl = scriptRow.asInstanceOf[ScriptRowImpl]
    val rowNameParts = impl.processMethod(texts => texts)
    if (rowNameParts.get(0).endsWith(";")) {
      List(methodName + ";")
    } else {
      val nParts = (rowNameParts.size + 1) / 2
      // How to do some sort of best effort replacement?
      val methodNameParts = methodName.split(" ", nParts)
      if (methodNameParts.size < nParts) {
        List(methodName + ";")
      } else {
        methodNameParts.toList
      }
    }
  }

  def createScriptRow(scriptRow: ScriptRow, methodName: String) = {
    val text = "|script|\n|" + interleave(chopMethodName(scriptRow, methodName), scriptRow.parameters).mkString("|") + "|"
    val file = createFile(scriptRow.getProject, text)
    file.getTables(0).rows(1).asInstanceOf[ScriptRow]
  }

  val INSTANCE: IStubElementType[ScriptRowStub, ScriptRow] = new ScriptRowElementType("SCRIPT_ROW")
}