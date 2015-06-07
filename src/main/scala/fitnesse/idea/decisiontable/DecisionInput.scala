package fitnesse.idea.decisiontable

import com.intellij.lang.ASTNode
import com.intellij.psi._
import com.intellij.psi.stubs._
import fitnesse.idea.fixturemethod.{FixtureMethodIndex, MethodReferences}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{Row, ScalaFriendlyStubBasedPsiElementBase}
import fitnesse.testsystems.slim.tables.Disgracer._


trait DecisionInputStub extends StubElement[DecisionInput] {
  def fixtureMethodName: String
}


trait DecisionInput extends StubBasedPsiElement[DecisionInputStub] with MethodReferences {
  def fixtureMethodName: String
  def getName: String
}


class DecisionInputStubImpl(parent: StubElement[_ <: PsiElement], methodName: String) extends StubBase[DecisionInput](parent, DecisionInputElementTypeHolder.INSTANCE) with DecisionInputStub {
  override def fixtureMethodName: String = methodName
}


class DecisionInputImpl extends ScalaFriendlyStubBasedPsiElementBase[DecisionInputStub] with DecisionInput {

  def this(node: ASTNode) = { this(); init(node) }
  def this(stub: DecisionInputStub) = { this(); init(stub) }

  def getRow = getParent.asInstanceOf[Row]

  def getFixtureClass = getRow.getTable.getFixtureClass

  def fixtureClassName: Option[String] = getRow.getTable.getFixtureClass match {
    case Some(cls) => cls.fixtureClassName
    case None => None
  }

  override def fixtureMethodName =
    disgraceMethodName("set " + getName)

  override def getName =
    if (getStub != null)
      getStub.fixtureMethodName
    else
      getNode.getText

  override def createReference(psiMethod: PsiMethod) = new DecisionInputReference(psiMethod, this)
}



class DecisionInputElementType(debugName: String) extends IStubElementType[DecisionInputStub, DecisionInput](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.decisionInput"

  override def createStub(psi: DecisionInput, parentStub: StubElement[_ <: PsiElement]): DecisionInputStub = new DecisionInputStubImpl(parentStub, psi.getName)

  override def createPsi(stub: DecisionInputStub): DecisionInput = new DecisionInputImpl(stub)

  override def indexStub(stub: DecisionInputStub, sink: IndexSink): Unit = {
    val methodName = disgraceMethodName("set " + stub.fixtureMethodName)
    println("Add to index: " + methodName)
    sink.occurrence(FixtureMethodIndex.KEY, methodName)
  }

  override def serialize(t: DecisionInputStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.fixtureMethodName)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): DecisionInputStub = {
    val ref = stubInputStream.readName
    new DecisionInputStubImpl(parentStub, ref.getString)
  }
}


object DecisionInputElementTypeHolder {
  val INSTANCE: IStubElementType[DecisionInputStub, DecisionInput] = new DecisionInputElementType("DECISION_INPUT")
}