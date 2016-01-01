package fitnesse.idea.decisiontable

import java.beans.Introspector.decapitalize

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.openapi.project.Project
import com.intellij.psi._
import com.intellij.psi.stubs._
import fitnesse.idea.etc.Regracer
import fitnesse.idea.filetype.FitnesseLanguage
import fitnesse.idea.fixturemethod.{ReturnType, FixtureMethod, FixtureMethodIndex, MethodOrScenarioArgumentReference}
import fitnesse.idea.psi.FitnesseElementFactory._
import fitnesse.idea.psi.ScalaFriendlyStubBasedPsiElementBase
import fitnesse.idea.table.Cell
import fitnesse.testsystems.slim.tables.Disgracer._


trait DecisionInputStub extends StubElement[DecisionInput] {
  def name: String
}


trait DecisionInput extends StubBasedPsiElement[DecisionInputStub] with Cell with FixtureMethod {
  def name: String
}


class DecisionInputStubImpl(parent: StubElement[_ <: PsiElement], _name: String) extends StubBase[DecisionInput](parent, DecisionInputElementType.INSTANCE) with DecisionInputStub {
  def name: String = _name
}


trait DecisionInputImpl extends ScalaFriendlyStubBasedPsiElementBase[DecisionInputStub] with DecisionInput {
  this: StubBasedPsiElementBase[DecisionInputStub] =>

  override def fixtureMethodName =
    disgraceMethodName("set " + name)

  override def parameters = disgraceMethodName(name) :: Nil

  def returnType = ReturnType.Void

  override def name = source match {
    case STUB => getStub.name
    case NODE => getNode.getText
  }

  override def getReference = new MethodOrScenarioArgumentReference(this)

  override def getName: String = name

  override def setName(newName: String): PsiElement = {
    val newElement = DecisionInputElementType createDecisionInput(getProject, DecisionInputElementType.regrace(newName))
    this.replace(newElement)
  }


}

object DecisionInputImpl {
  def apply(node: ASTNode) = new StubBasedPsiElementBase[DecisionInputStub](node) with DecisionInputImpl
  def apply(stub: DecisionInputStub) = new StubBasedPsiElementBase[DecisionInputStub](stub, DecisionInputElementType.INSTANCE) with DecisionInputImpl
}

class DecisionInputElementType(debugName: String) extends IStubElementType[DecisionInputStub, DecisionInput](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.decisionInput"

  override def createStub(psi: DecisionInput, parentStub: StubElement[_ <: PsiElement]): DecisionInputStub = {
    new DecisionInputStubImpl(parentStub, psi.name)
  }

  override def createPsi(stub: DecisionInputStub): DecisionInput = DecisionInputImpl(stub)

  override def indexStub(stub: DecisionInputStub, sink: IndexSink): Unit = {
    val methodName = disgraceMethodName("set " + stub.name)
    sink.occurrence(FixtureMethodIndex.KEY, methodName)
  }

  override def serialize(t: DecisionInputStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.name)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): DecisionInputStub = {
    val ref = stubInputStream.readName
    new DecisionInputStubImpl(parentStub, ref.getString)
  }
}


object DecisionInputElementType {
  val INSTANCE: IStubElementType[DecisionInputStub, DecisionInput] = new DecisionInputElementType("DECISION_INPUT")

  def regrace(newElementName: String) = {
    if (newElementName.startsWith("set")) {
      Regracer.regrace(decapitalize(newElementName.substring(3)))
    } else {
      Regracer.regrace(newElementName)
    }

  }
  def createDecisionInput(project : Project, methodName : String) = {
    val text = "|foo|\n|" + methodName + "|"
    // Why parse text as a file and retrieve the fixtureClass from there?
    val file = createFile(project, text)
    file.getTables(0).rows(1).findInRow(classOf[DecisionInput])
  }
}