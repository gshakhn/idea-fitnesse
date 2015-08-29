package fitnesse.idea.decisiontable

import com.intellij.lang.ASTNode
import com.intellij.psi._
import com.intellij.psi.stubs._
import fitnesse.idea.fixturemethod.{FixtureMethod, FixtureMethodIndex}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{Cell, ScalaFriendlyStubBasedPsiElementBase}
import fitnesse.testsystems.slim.tables.Disgracer._


trait DecisionInputStub extends StubElement[DecisionInput] {
  def getName: String
}


trait DecisionInput extends StubBasedPsiElement[DecisionInputStub] with Cell with FixtureMethod {
  def fixtureMethodName: String
  def getName: String
}


class DecisionInputStubImpl(parent: StubElement[_ <: PsiElement], name: String) extends StubBase[DecisionInput](parent, DecisionInputElementTypeHolder.INSTANCE) with DecisionInputStub {
  override def getName: String = name
}


class DecisionInputImpl extends ScalaFriendlyStubBasedPsiElementBase[DecisionInputStub] with DecisionInput {

  def this(node: ASTNode) = { this(); init(node) }
  def this(stub: DecisionInputStub) = { this(); init(stub) }

  override def fixtureMethodName =
    disgraceMethodName("set " + getName)

  override def getName = source match {
    case STUB => getStub.getName
    case NODE => getNode.getText
  }

  override def getReference = new DecisionInputReference(this)
}



class DecisionInputElementType(debugName: String) extends IStubElementType[DecisionInputStub, DecisionInput](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.decisionInput"

  override def createStub(psi: DecisionInput, parentStub: StubElement[_ <: PsiElement]): DecisionInputStub = new DecisionInputStubImpl(parentStub, psi.getName)

  override def createPsi(stub: DecisionInputStub): DecisionInput = new DecisionInputImpl(stub)

  override def indexStub(stub: DecisionInputStub, sink: IndexSink): Unit = {
    val methodName = disgraceMethodName("set " + stub.getName)
    sink.occurrence(FixtureMethodIndex.KEY, methodName)
  }

  override def serialize(t: DecisionInputStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.getName)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): DecisionInputStub = {
    val ref = stubInputStream.readName
    new DecisionInputStubImpl(parentStub, ref.getString)
  }
}


object DecisionInputElementTypeHolder {
  val INSTANCE: IStubElementType[DecisionInputStub, DecisionInput] = new DecisionInputElementType("DECISION_INPUT")
}