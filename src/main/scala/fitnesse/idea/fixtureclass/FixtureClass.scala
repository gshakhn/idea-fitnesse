package fitnesse.idea.fixtureclass

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi._
import com.intellij.psi.stubs._
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{Row, ScalaFriendlyStubBasedPsiElementBase}
import fitnesse.testsystems.slim.tables.Disgracer.disgraceClassName


trait FixtureClassStub extends StubElement[FixtureClass] {
  def getName: String
}


trait FixtureClass extends StubBasedPsiElement[FixtureClassStub] {
  def fixtureClassName: Option[String]
  def getName: String
  def getReference: FixtureClassReference
}


class FixtureClassStubImpl(parent: StubElement[_ <: PsiElement], name: String) extends StubBase[FixtureClass](parent, FixtureClassElementType.INSTANCE) with FixtureClassStub {
  override def getName: String = name
}


trait FixtureClassImpl extends ScalaFriendlyStubBasedPsiElementBase[FixtureClassStub] with FixtureClass with PsiNamedElement {
  this: StubBasedPsiElementBase[FixtureClassStub] =>

  def getRow = getParent.asInstanceOf[Row]

  def getTable = getRow.getTable

  def fixtureClassName: Option[String] =
    disgraceClassName(getName) match {
      case "" => None
      case className => Some(className)
    }

  override def getReference: FixtureClassReference = new FixtureClassReference(this)

  override def getName = source match {
    case STUB => getStub.getName
    case NODE => getNode.getText.trim
  }

  override def setName(s: String): PsiElement = FixtureClassManipulator.createFixtureClass(getProject, s)
}

object FixtureClassImpl {
  def apply(node: ASTNode) = new StubBasedPsiElementBase[FixtureClassStub](node) with FixtureClassImpl
  def apply(stub: FixtureClassStub) = new StubBasedPsiElementBase[FixtureClassStub](stub, FixtureClassElementType.INSTANCE) with FixtureClassImpl
}

class FixtureClassIndex extends StringStubIndexExtension[FixtureClass] {
  override def getKey: StubIndexKey[String, FixtureClass] = FixtureClassIndex.KEY

  override def getVersion: Int = 3
}


object FixtureClassIndex {
  val KEY: StubIndexKey[String, FixtureClass] = StubIndexKey.createIndexKey("fitnesse.fixtureClass.index")

  val INSTANCE = new FixtureClassIndex
}


class FixtureClassElementType(debugName: String) extends IStubElementType[FixtureClassStub, FixtureClass](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.fixtureClass"

  override def createStub(psi: FixtureClass, parentStub: StubElement[_ <: PsiElement]): FixtureClassStub = new FixtureClassStubImpl(parentStub, psi.getName)

  override def createPsi(stub: FixtureClassStub): FixtureClass = FixtureClassImpl(stub)

  override def indexStub(stub: FixtureClassStub, sink: IndexSink): Unit = {
    val className = disgraceClassName(stub.getName)
    sink.occurrence(FixtureClassIndex.KEY, className)
  }

  override def serialize(t: FixtureClassStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.getName)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): FixtureClassStub = {
    val ref = stubInputStream.readName
    new FixtureClassStubImpl(parentStub, ref.getString)
  }
}


object FixtureClassElementType {
  val INSTANCE: IStubElementType[FixtureClassStub, FixtureClass] = new FixtureClassElementType("FIXTURE_CLASS")
}