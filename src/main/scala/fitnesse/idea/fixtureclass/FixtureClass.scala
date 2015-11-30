package fitnesse.idea.fixtureclass

import javax.swing.Icon

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.project.Project
import com.intellij.psi._
import com.intellij.psi.stubs._
import com.intellij.util.IncorrectOperationException
import fitnesse.idea.etc.Regracer
import fitnesse.idea.filetype.{FitnesseFileType, FitnesseLanguage}
import fitnesse.idea.psi.ScalaFriendlyStubBasedPsiElementBase
import fitnesse.idea.table.Row
import fitnesse.testsystems.slim.tables.Disgracer.disgraceClassName
import fitnesse.idea.psi.FitnesseElementFactory.createFile


trait FixtureClassStub extends StubElement[FixtureClass] {
  def name: String
}


trait FixtureClass extends StubBasedPsiElement[FixtureClassStub] with PsiNamedElement {
  def fixtureClassName: Option[String]
  def name: String
  def getReference: FixtureClassReference
}


class FixtureClassStubImpl(parent: StubElement[_ <: PsiElement], _name: String) extends StubBase[FixtureClass](parent, FixtureClassElementType.INSTANCE) with FixtureClassStub {
  override def name: String = _name
}


trait FixtureClassImpl extends ScalaFriendlyStubBasedPsiElementBase[FixtureClassStub] with FixtureClass with PsiNamedElement {
  this: StubBasedPsiElementBase[FixtureClassStub] =>

  def row = getParent.asInstanceOf[Row]

  def table = row.table

  def fixtureClassName: Option[String] =
    disgraceClassName(name) match {
      case "" => None
      case className => Some(className)
    }

  override def getReference: FixtureClassReference = new FixtureClassReference(this)

  override def name = source match {
    case STUB => getStub.name
    case NODE => getNode.getText.trim
  }

  override def getName: String = name

  override def setName(newName: String): PsiElement = {
    // Update ASTNode instead?
    //    FixtureClassElementType.createFixtureClass(getProject, newName)
    //    val dummyFixtureClass = FixtureClassElementType.createFixtureClass(getProject, newName)
    //    this.getNode.getTreeParent.replaceChild(this.getNode, dummyFixtureClass.getNode)
    //    this.setNode(dummyFixtureClass.getNode)
    //    this
    val newElement = FixtureClassElementType.createFixtureClass(getProject, newName)
    this.replace(newElement)
  }

  override def getPresentation: ItemPresentation = new ItemPresentation {
    override def getIcon(unused: Boolean): Icon = FitnesseFileType.FILE_ICON

    override def getLocationString: String = getContainingFile.getPresentation.getLocationString

    override def getPresentableText: String = name
  }
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

  override def createStub(psi: FixtureClass, parentStub: StubElement[_ <: PsiElement]): FixtureClassStub = new FixtureClassStubImpl(parentStub, psi.name)

  override def createPsi(stub: FixtureClassStub): FixtureClass = FixtureClassImpl(stub)

  override def indexStub(stub: FixtureClassStub, sink: IndexSink): Unit = {
    val className = disgraceClassName(stub.name)
    sink.occurrence(FixtureClassIndex.KEY, className)
  }

  override def serialize(t: FixtureClassStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.name)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): FixtureClassStub = {
    val ref = stubInputStream.readName
    new FixtureClassStubImpl(parentStub, ref.getString)
  }
}


object FixtureClassElementType {
  val INSTANCE: IStubElementType[FixtureClassStub, FixtureClass] = new FixtureClassElementType("FIXTURE_CLASS")

  def createFixtureClass(project : Project, className : String) = {
    val text = "|" + Regracer.regrace(className) + "|"
    // Why parse text as a file and retrieve the fixtureClass from there?
    val file = createFile(project, text)
    file.getTables(0).fixtureClass match {
      case Some(fixtureClass) => fixtureClass
      case None => throw new IncorrectOperationException(s"No fixture class could be derived for '${className}'")
    }
  }

}