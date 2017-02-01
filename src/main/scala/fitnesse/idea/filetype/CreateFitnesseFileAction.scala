package fitnesse.idea.filetype

import com.intellij.ide.actions.{CreateFileFromTemplateDialog, CreateFromTemplateAction}
import com.intellij.openapi.project.{DumbAware, Project}
import com.intellij.openapi.ui.InputValidator
import com.intellij.psi.{PsiDirectory, PsiFile}
import fitnesse.wiki.PathParser

class CreateFitnesseFileAction extends CreateFromTemplateAction[PsiFile]("FitNesse Page", "Creates a FitNesse test/suite/static page", FitnesseFileType.FILE_ICON) with DumbAware {

  override def buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder): Unit = {
    builder.setTitle("New FitNesse page")
      .addKind("Test page", FitnesseFileType.FILE_ICON, "TestPage")
      .addKind("Suite page", FitnesseFileType.FILE_ICON, "SuitePage")
      .addKind("Static page", FitnesseFileType.FILE_ICON, "StaticPage")
      .addKind("Test page (old style)", FitnesseFileType.FILE_ICON, "OldStyleTestPage")
      .addKind("Suite page (old style)", FitnesseFileType.FILE_ICON, "OldStyleSuitePage")
      .addKind("Static page (old style)", FitnesseFileType.FILE_ICON, "OldStyleStaticPage")
      .setValidator(new InputValidator {
        override def checkInput(s: String): Boolean = PathParser.isSingleWikiWord(s)
        override def canClose(s: String): Boolean = checkInput(s)
      })
  }

  override def getActionName(directory: PsiDirectory, newName: String, templateName: String): String =
  {
    s"Create FitNesse page $newName"
  }

  override def createFile(name: String, templateName: String, psiDirectory: PsiDirectory): PsiFile = {
    if (templateName.startsWith("OldStyle")) {
      createOldStyleFile(name, templateName, psiDirectory)
    } else {
      createSinglePageFile(name, templateName, psiDirectory)
    }
  }


  def createSinglePageFile(name: String, templateName: String, psiDirectory: PsiDirectory): PsiFile = {
    if ("SuitePage".equals(templateName)) {
      psiDirectory.createSubdirectory(name)
    }
    val wikiFile = psiDirectory.createFile(s"${name}.wiki")
    wikiFile.getVirtualFile.setBinaryContent(createWikiFileContent(templateName).getBytes("UTF-8"))
    wikiFile
  }

  def createWikiFileContent(templateName: String) = templateName match {
    case "TestPage" =>
      """---
        |Test
        |---
        |""".stripMargin
    case "SuitePage" =>
      """---
        |Suite
        |---
        |!contents -R2 -g -p -f -h
        |""".stripMargin
    case "StaticPage" => ""
  }

  def createOldStyleFile(name: String, templateName: String, psiDirectory: PsiDirectory): PsiFile = {
    val pageDir = psiDirectory.createSubdirectory(name)
    val contentFile = pageDir.createFile("content.txt")
    val propertiesFile = pageDir.createFile("properties.xml")
    propertiesFile.getVirtualFile.setBinaryContent(createPropertiesFileContent(templateName).getBytes("UTF-8"))

    contentFile
  }

  def createPropertiesFileContent(templateName: String): String =
    """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
      |<properties>
      |<Edit/>
      |<Files/>
      |<Help/>
      |<Properties/>
      |<RecentChanges/>
      |<Refactor/>
      |<Search/>
      |<Suites/>
      |@@<Versions/>
      |<WhereUsed/>
      |</properties>
      |""".stripMargin.replace("@@", templateName match {
      case "OldStyleTestPage" => "<Test/>\n"
      case "OldStyleSuitePage" => "<Suite/>\n"
      case _ => ""
    })
}
