package fitnesse.idea.fixtureclass

import com.intellij.lang.ASTNode
import com.intellij.psi._
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.stubs._
import fitnesse.idea.fixturemethod.MethodReference
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{Row, ScalaFriendlyStubBasedPsiElementBase}
import fitnesse.testsystems.slim.tables.Disgracer.disgraceClassName


trait FixtureClassStub extends StubElement[FixtureClass] {
  def getName: String
}


trait FixtureClass extends StubBasedPsiElement[FixtureClassStub] {
  def fixtureClassName: Option[String]
  def getName: String
}


class FixtureClassStubImpl(parent: StubElement[_ <: PsiElement], name: String) extends StubBase[FixtureClass](parent, FixtureClassElementTypeHolder.INSTANCE) with FixtureClassStub {
  override def getName: String = name
}


class FixtureClassImpl extends ScalaFriendlyStubBasedPsiElementBase[FixtureClassStub] with FixtureClass with PsiNamedElement {

  def this(node: ASTNode) = { this(); init(node) }
  def this(stub: FixtureClassStub) = { this(); init(stub) }

  def getRow = getParent.asInstanceOf[Row]

  def fixtureClassName: Option[String] =
    disgraceClassName(getName) match {
      case "" => None
      case className => Some(className)
    }

  private def isQualifiedName: Boolean = {
    fixtureClassName match {
      case Some(name) =>
        val dotIndex: Int = name.indexOf(".")
        dotIndex != -1 && dotIndex != name.length - 1
      case None => false
    }
  }

  private def shortName: Option[String] = {
    fixtureClassName match {
      case Some(name) => name.split('.').toList.reverse match {
        case "" :: n :: _ => Some(n)
        case n :: _ => Some(n)
        case _ => Some(name)
      }
      case None => None
    }
  }

  protected def getReferencedClasses: Seq[PsiReference] = {
    def createReference(psiClass: PsiClass): FixtureClassReference = new FixtureClassReference(psiClass, this)

    fixtureClassName match {
      case Some(className) if isQualifiedName =>
          JavaPsiFacade.getInstance(getProject).findClasses(className, GlobalSearchScope.projectScope(getProject)).map(createReference)
      case Some(className) =>
          PsiShortNamesCache.getInstance(getProject).getClassesByName(shortName.get, GlobalSearchScope.projectScope(getProject)).map(createReference)
      case None => Seq()
    }
  }

  override def getReferences = {
    getReferencedClasses.toArray
  }

  override def getName = source match {
    case STUB => getStub.getName
    case NODE => getNode.getText
  }

  override def setName(s: String): PsiElement = FixtureClassManipulator.createFixtureClass(getProject, s)
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

  override def createPsi(stub: FixtureClassStub): FixtureClass = new FixtureClassImpl(stub)

  override def indexStub(stub: FixtureClassStub, sink: IndexSink): Unit = {
    val className = disgraceClassName(stub.getName)
    sink.occurrence(FixtureClassIndex.KEY, className);
  }

  override def serialize(t: FixtureClassStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.getName)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): FixtureClassStub = {
    val ref = stubInputStream.readName
    new FixtureClassStubImpl(parentStub, ref.getString)
  }
}


object FixtureClassElementTypeHolder {
  val INSTANCE: IStubElementType[FixtureClassStub, FixtureClass] = new FixtureClassElementType("FIXTURE_CLASS")
}