package fitnesse.idea.lang.psi

import java.util

import com.intellij.codeInsight.FileModificationService
import com.intellij.mock.MockPsiDocumentManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.impl.smartPointers.SmartPointerManagerImpl
import com.intellij.psi._
import com.intellij.psi.search.{GlobalSearchScope, ProjectScopeBuilder, ProjectScopeBuilderImpl, PsiShortNamesCache}
import com.intellij.psi.stubs.StubIndex
import fitnesse.idea.fixtureclass.FixtureClassReference
import fitnesse.idea.lang.parser.ParserSuite
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

}

object PsiSuite extends MockitoSugar {
  val myStaticStubIndex: StubIndex = mock[StubIndex]
}