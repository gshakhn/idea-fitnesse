package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.stubs._
import com.intellij.psi._
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.manipulator.FixtureClassManipulator
import fitnesse.idea.lang.reference.FixtureClassReference
import fitnesse.testsystems.slim.tables.Disgracer.disgraceClassName


trait FixtureClassStub extends StubElement[FixtureClass] {
  def fixtureClassName: String
}


trait FixtureClass extends StubBasedPsiElement[FixtureClassStub] {
  def fixtureClassName: Option[String]
  def getReferencedClasses: Seq[PsiClass]
}


class FixtureClassStubImpl(parent: StubElement[_ <: PsiElement], className: String) extends StubBase[FixtureClass](parent, FixtureClassElementTypeHolder.INSTANCE) with FixtureClassStub {
  override def fixtureClassName: String = className
}


class FixtureClassImpl extends ScalaFriendlyStubBasedPsiElementBase[FixtureClassStub] with FixtureClass with PsiNamedElement {

  def this(node: ASTNode) = { this(); init(node) }
  def this(stub: FixtureClassStub) = { this(); init(stub) }

  def getRow = getParent.asInstanceOf[Row]

  def fixtureClassName: Option[String] =
    (if (getStub != null)
      disgraceClassName(getStub.fixtureClassName)
    else
      disgraceClassName(getNode.getText)) match {
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

  def getReferencedClasses: Seq[PsiClass] = {
    fixtureClassName match {
      case Some(className) if isQualifiedName =>
          JavaPsiFacade.getInstance(getProject).findClasses(className, GlobalSearchScope.projectScope(getProject))
      case Some(className) =>
          PsiShortNamesCache.getInstance(getProject).getClassesByName(shortName.get, GlobalSearchScope.projectScope(getProject))
      case None => Seq()
    }
  }

  override def getReferences = {
    getReferencedClasses.map(new FixtureClassReference(_, this)).toArray
  }

//  override def getName = fixtureClassName.get

  override def setName(s: String): PsiElement = FixtureClassManipulator.createFixtureClass(getProject, s)
}


class FixtureClassIndex extends StringStubIndexExtension[FixtureClass] {
  override def getKey: StubIndexKey[String, FixtureClass] = FixtureClassIndex.KEY
}


object FixtureClassIndex {
  val KEY: StubIndexKey[String, FixtureClass] = StubIndexKey.createIndexKey("fitnesse.fixtureClass.index")

  val INSTANCE = new FixtureClassIndex
}


class FixtureClassElementType(debugName: String) extends IStubElementType[FixtureClassStub, FixtureClass](debugName, FitnesseLanguage.INSTANCE) {
  override def getExternalId: String = "fitnesse.fixtureClass"

  override def createStub(psi: FixtureClass, parentStub: StubElement[_ <: PsiElement]): FixtureClassStub = new FixtureClassStubImpl(parentStub, psi.fixtureClassName.getOrElse(""))

  override def createPsi(stub: FixtureClassStub): FixtureClass = new FixtureClassImpl(stub)

  override def indexStub(stub: FixtureClassStub, sink: IndexSink): Unit = {
    val className: String = disgraceClassName(stub.fixtureClassName)
    sink.occurrence(FixtureClassIndex.KEY, className);
  }

  override def serialize(t: FixtureClassStub, stubOutputStream: StubOutputStream): Unit = {
    stubOutputStream.writeName(t.fixtureClassName)
  }

  override def deserialize(stubInputStream: StubInputStream, parentStub: StubElement[_ <: PsiElement]): FixtureClassStub = {
    val ref = stubInputStream.readName
    return new FixtureClassStubImpl(parentStub, ref.getString)
  }
}


object FixtureClassElementTypeHolder {
  val INSTANCE: IStubElementType[FixtureClassStub, FixtureClass] = new FixtureClassElementType("FIXTURE_CLASS")
}