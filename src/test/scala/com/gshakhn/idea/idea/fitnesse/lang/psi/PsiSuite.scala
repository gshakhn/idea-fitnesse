package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.lang.filetype.FitnesseFileType
import com.gshakhn.idea.idea.fitnesse.lang.parser.ParserSuite
import com.intellij.mock.{MockFileTypeManager, MockFileIndexFacade}
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.roots.{ProjectFileIndex, FileIndexFacade, ProjectRootManager}
import com.intellij.openapi.roots.impl.{DirectoryIndexImpl, DirectoryIndex, ProjectFileIndexImpl, ProjectRootManagerImpl}
import com.intellij.psi.impl.PsiShortNamesCacheImpl
import com.intellij.psi.search.{ProjectScopeBuilder, ProjectScopeBuilderImpl, PsiShortNamesCache}
import org.mockito.Mockito
import org.scalatest.mock.MockitoSugar

trait PsiSuite extends ParserSuite with MockitoSugar {

  var myPsiShortNamesCache: PsiShortNamesCache = mock[PsiShortNamesCache]

  override protected def beforeAll() {
    super.beforeAll()

    myProject.getPicoContainer.registerComponentInstance(classOf[PsiShortNamesCache].getName, myPsiShortNamesCache)
    myProject.getPicoContainer.registerComponentInstance(classOf[ProjectScopeBuilder].getName, new ProjectScopeBuilderImpl(myProject))
  }

  override protected def afterAll() {
    myProject.getPicoContainer.unregisterComponent(classOf[PsiShortNamesCache].getName)
    myProject.getPicoContainer.unregisterComponent(classOf[ProjectScopeBuilder].getName)

    super.afterAll()
  }
}