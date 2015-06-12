package fitnesse.idea.decisiontable

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs._
import com.intellij.psi.{PsiElement, StubBasedPsiElement}
import fitnesse.idea.fixturemethod.{FixtureMethodIndex, MethodReferences}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{Cell, ScalaFriendlyStubBasedPsiElementBase}
import fitnesse.testsystems.slim.tables.Disgracer._


trait DecisionOutputStub extends StubElement[DecisionOutput] {
  def getName: String
}


trait DecisionOutput extends StubBasedPsiElement[DecisionOutputStub] with Cell with MethodReferences {
  def fixtureMethodName: String
  def getName: String
}


class DecisionOutputStubImpl(parent: StubElement[_ <: PsiElement], name: String) extends StubBase[DecisionOutput](parent, DecisionOutputElementTypeHolder.INSTANCE) with DecisionOutputStub {
  override def getName: String = name
}


class DecisionOutputImpl extends ScalaFriendlyStubBasedPsiElementBase[DecisionOutputStub] with DecisionOutput {

  def this(node: ASTNode) = { this(); init(node) }
  def this(stub: DecisionOutputStub) = { this(); init(stub) }

  override def fixtureMethodName =
    disgraceMethodName(getName)

  override def getName = source match {
    case STUB => getStub.getName
    case NODE => getNode.getText
  }

//  override def createReference(psiMethod: PsiMethod) = new DecisionOutputReference(psiMethod, this)
}



class DecisionOutputElementType(debugName: String) extends IStubElementType[DecisionOutputStub, DecisionOutput](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.decisionOutput"

  override def createStub(psi: DecisionOutput, parentStub: StubElement[_ <: PsiElement]): DecisionOutputStub = new DecisionOutputStubImpl(parentStub, psi.getName)

  override def createPsi(stub: DecisionOutputStub): DecisionOutput = new DecisionOutputImpl(stub)

  override def indexStub(stub: DecisionOutputStub, sink: IndexSink): Unit = {
    val methodName = disgraceMethodName(stub.getName)
    println("Add to index: " + methodName)
    sink.occurrence(FixtureMethodIndex.KEY, methodName)
  }

  override def serialize(t: DecisionOutputStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.getName)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): DecisionOutputStub = {
    val ref = stubInputStream.readName
    new DecisionOutputStubImpl(parentStub, ref.getString)
  }
}


object DecisionOutputElementTypeHolder {
  val INSTANCE: IStubElementType[DecisionOutputStub, DecisionOutput] = new DecisionOutputElementType("DECISION_OUTPUT")
}