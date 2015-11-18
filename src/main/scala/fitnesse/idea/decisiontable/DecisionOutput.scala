package fitnesse.idea.decisiontable

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi._
import com.intellij.psi.stubs._
import fitnesse.idea.fixturemethod.{FixtureMethod, FixtureMethodIndex}
import fitnesse.idea.filetype.FitnesseLanguage
import fitnesse.idea.psi.ScalaFriendlyStubBasedPsiElementBase
import fitnesse.idea.table.Cell
import fitnesse.testsystems.slim.tables.Disgracer._


trait DecisionOutputStub extends StubElement[DecisionOutput] {
  def name: String
}


trait DecisionOutput extends StubBasedPsiElement[DecisionOutputStub] with Cell with FixtureMethod {
  def name: String
}


class DecisionOutputStubImpl(parent: StubElement[_ <: PsiElement], _name: String) extends StubBase[DecisionOutput](parent, DecisionOutputElementType.INSTANCE) with DecisionOutputStub {
  override def name: String = _name
}


trait DecisionOutputImpl extends ScalaFriendlyStubBasedPsiElementBase[DecisionOutputStub] with DecisionOutput {
  this: StubBasedPsiElementBase[DecisionOutputStub] =>

  override def fixtureMethodName =
    disgraceMethodName(name)

  override def parameters = Nil

  override def returnType = PsiType.getJavaLangString(getManager, getResolveScope)

  override def name = source match {
    case STUB => getStub.name
    case NODE => getNode.getText
  }

  override def getReference = new DecisionOutputReference(this)

  override def getName: String = name

  // Update ASTNode instead?
  override def setName(newName: String): PsiElement = {
    //    val newElement = FixtureClassElementType.createFixtureClass(getProject, newName)
    //    this.replace(newElement)
    //    newElement
    this
  }
}

object DecisionOutputImpl {
  def apply(node: ASTNode) = new StubBasedPsiElementBase[DecisionOutputStub](node) with DecisionOutputImpl
  def apply(stub: DecisionOutputStub) = new StubBasedPsiElementBase[DecisionOutputStub](stub, DecisionOutputElementType.INSTANCE) with DecisionOutputImpl
}


class DecisionOutputElementType(debugName: String) extends IStubElementType[DecisionOutputStub, DecisionOutput](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.decisionOutput"

  override def createStub(psi: DecisionOutput, parentStub: StubElement[_ <: PsiElement]): DecisionOutputStub = new DecisionOutputStubImpl(parentStub, psi.name)

  override def createPsi(stub: DecisionOutputStub): DecisionOutput = DecisionOutputImpl(stub)

  override def indexStub(stub: DecisionOutputStub, sink: IndexSink): Unit = {
    val methodName = disgraceMethodName(stub.name)
    sink.occurrence(FixtureMethodIndex.KEY, methodName)
  }

  override def serialize(t: DecisionOutputStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.name)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): DecisionOutputStub = {
    val ref = stubInputStream.readName
    new DecisionOutputStubImpl(parentStub, ref.getString)
  }
}


object DecisionOutputElementType {
  val INSTANCE: IStubElementType[DecisionOutputStub, DecisionOutput] = new DecisionOutputElementType("DECISION_OUTPUT")
}