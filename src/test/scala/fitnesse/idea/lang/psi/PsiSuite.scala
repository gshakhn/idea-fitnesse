package fitnesse.idea.lang.psi

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File}
import java.util

import com.intellij.codeInsight.FileModificationService
import com.intellij.lang.FileASTNode
import com.intellij.mock.{MockPsiDocumentManager, MockResolveScopeManager}
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi._
import com.intellij.psi.impl.ResolveScopeManager
import com.intellij.psi.impl.smartPointers.SmartPointerManagerImpl
import com.intellij.psi.search.{GlobalSearchScope, ProjectScopeBuilder, ProjectScopeBuilderImpl, PsiShortNamesCache}
import com.intellij.psi.stubs._
import com.intellij.util.io.PersistentStringEnumerator
import fitnesse.idea.fixtureclass.FixtureClassReference
import fitnesse.idea.lang.parser.{FitnesseElementType, ParserSuite}
import org.mockito.Matchers.{any, eq => m_eq}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

trait PsiSuite extends ParserSuite with MockitoSugar {

  val myPsiShortNamesCache: PsiShortNamesCache = mock[PsiShortNamesCache]
  val myJavaPsiFacade: JavaPsiFacade = mock[JavaPsiFacade]
  val myStubIndex: StubIndex = PsiSuite.myStaticStubIndex

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    app.getPicoContainer.registerComponentInstance(classOf[StubIndex].getName, myStubIndex)
    app.getPicoContainer.registerComponentInstance(classOf[FileModificationService].getName, new FileModificationService() {
      override def prepareVirtualFilesForWrite(project: Project, collection: util.Collection[VirtualFile]): Boolean = true
      override def preparePsiElementsForWrite(collection: util.Collection[_ <: PsiElement]): Boolean = true
      override def prepareFileForWrite(psiFile: PsiFile): Boolean = true
      override def preparePsiElementForWrite(psiElement: PsiElement): Boolean = true
    })

    myProject.getPicoContainer.registerComponentInstance(classOf[PsiShortNamesCache].getName, myPsiShortNamesCache)
    myProject.getPicoContainer.registerComponentInstance(classOf[JavaPsiFacade].getName, myJavaPsiFacade)
    myProject.getPicoContainer.registerComponentInstance(classOf[ProjectScopeBuilder].getName, new ProjectScopeBuilderImpl(myProject))
    myProject.getPicoContainer.registerComponentInstance(classOf[SmartPointerManager].getName, new SmartPointerManagerImpl(myProject))
    myProject.getPicoContainer.registerComponentInstance(classOf[PsiDocumentManager].getName, new MockPsiDocumentManager())
    myProject.getPicoContainer.registerComponentInstance(classOf[ResolveScopeManager].getName, new MockResolveScopeManager(myProject))

    FixtureClassReference.scopeForTesting = Option(mock[GlobalSearchScope])
  }

  override protected def afterAll(): Unit = {
    myProject.getPicoContainer.unregisterComponent(classOf[ProjectScopeBuilder].getName)
    myProject.getPicoContainer.unregisterComponent(classOf[PsiShortNamesCache].getName)
    app.getPicoContainer.unregisterComponent(classOf[StubIndex].getName)

    super.afterAll()
  }

  def createTable(s: String): Table = {
    val psiFile = FitnesseElementFactory.createFile(myProject, s)
    psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
  }

  def psiClassType(className: String): PsiClassType = {
    val psiElementFactory: PsiElementFactory = mock[PsiElementFactory]
    val classType = mock[PsiClassType]
    when(myJavaPsiFacade.getElementFactory).thenReturn(psiElementFactory)
    when(psiElementFactory.createTypeByFQClassName(m_eq(className), any(classOf[GlobalSearchScope]))).thenReturn(classType)
    classType
  }

  def createFileAndSerializeAndDeserialize(content: String): Stub = {
    val file = FitnesseElementFactory.createFile(myProject, content)
    val fileNode: FileASTNode = file.getNode
    assert(fileNode.isParsed)
    val indexFile: File = File.createTempFile("idea", "fitnesse")
    val persistentStringEnumerator = new PersistentStringEnumerator(indexFile)
    val stubSerializationHelper = new StubSerializationHelper(persistentStringEnumerator)

    stubSerializationHelper.assignId(PsiFileStubImpl.TYPE)
    stubSerializationHelper.assignId(FitnesseElementType.DECISION_INPUT)
    stubSerializationHelper.assignId(FitnesseElementType.DECISION_OUTPUT)
    stubSerializationHelper.assignId(FitnesseElementType.FIXTURE_CLASS)

    val outputStream = new ByteArrayOutputStream()
    stubSerializationHelper.serialize(file.calcStubTree().getRoot, outputStream)

    val deserialized = stubSerializationHelper.deserialize(new ByteArrayInputStream(outputStream.toByteArray))
    deserialized
  }
}

object PsiSuite extends MockitoSugar {
  val myStaticStubIndex: StubIndex = mock[StubIndex]
}