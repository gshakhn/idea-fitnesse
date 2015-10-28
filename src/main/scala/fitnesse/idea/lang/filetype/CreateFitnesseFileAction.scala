package fitnesse.idea.lang.filetype

import com.intellij.ide.actions.{CreateFileFromTemplateDialog, CreateFromTemplateAction}
import com.intellij.openapi.project.{DumbAware, Project}
import com.intellij.psi.{PsiDirectory, PsiFile}

class CreateFitnesseFileAction extends CreateFromTemplateAction[PsiFile]("FitNesse File", "Creates a FitNesse test/suite/static page", FitnesseFileType.FILE_ICON) with DumbAware {

  override def buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder): Unit = {
    builder.setTitle("New FitNesse page")
      .addKind("Test page", FitnesseFileType.FILE_ICON, "TestPage")
      .addKind("Suite page", FitnesseFileType.FILE_ICON, "SuitePage")
      .addKind("Static page", FitnesseFileType.FILE_ICON, "StaticPage")
  }

  override def getActionName(directory: PsiDirectory, newName: String, templateName: String): String =
  {
    s"Create FitNesse page $newName"
  }

  override def createFile(name: String, templateName: String, psiDirectory: PsiDirectory): PsiFile = {
    val pageDir = psiDirectory.createSubdirectory(name)
    val contentFile = pageDir.createFile("content.txt")
    val propertiesFile = pageDir.createFile("properties.xml")
    propertiesFile.getVirtualFile.setBinaryContent(createPropertiesFileContent(templateName).getBytes("UTF-8"))

    contentFile
  }

  def createPropertiesFileContent(templateName: String): String = {
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
      case "TestPage" => "<Test/>\n"
      case "SuitePage" => "<Suite/>\n"
      case _ => ""
    })
  }
}
