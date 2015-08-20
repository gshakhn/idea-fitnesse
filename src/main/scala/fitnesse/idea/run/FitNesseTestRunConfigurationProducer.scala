package fitnesse.idea.run

import java.io.File
import javax.swing.Icon

import com.intellij.execution.JavaRunConfigurationExtensionManager
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.application.ApplicationConfigurationType
import com.intellij.execution.configuration.ConfigurationFactoryEx
import com.intellij.execution.configurations._
import com.intellij.execution.junit.JavaRunConfigurationProducerBase
import com.intellij.openapi.module.{Module, ModuleUtilCore}
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.{VfsUtil, VirtualFile}
import com.intellij.psi.{PsiDirectory, PsiElement}
import fitnesse.idea.FitnesseBundle
import fitnesse.idea.lang.filetype.FitnesseFileType

class FitNesseTestRunConfigurationProducer extends JavaRunConfigurationProducerBase[FitnesseRunConfiguration](FitnesseRunConfigurationType.INSTANCE) {

  override def setupConfigurationFromContext(configuration: FitnesseRunConfiguration, context: ConfigurationContext, sourceElement: Ref[PsiElement]): Boolean =
    wikiPageInfo(configuration.getProject, context) match {
      case None => false
      case Some((wikiPageFile, fitnesseRoot)) =>
        val wikiPageName = makeWikiPageName(fitnesseRoot, wikiPageFile)

        configuration.fitnesseRoot = fitnesseRoot.getName
        configuration.setWorkingDirectory(fitnesseRoot.getParent.getCanonicalPath)
        configuration.wikiPageName = wikiPageName

        configuration.setName(wikiPageName)

        setupConfigurationModule(context, configuration)
        JavaRunConfigurationExtensionManager.getInstance().extendCreatedConfiguration(configuration, context.getLocation)
        true
    }

  override def isConfigurationFromContext(configuration: FitnesseRunConfiguration, context: ConfigurationContext): Boolean =
    wikiPageInfo(configuration.getProject, context) match {
      case None => false
      case Some((wikiPageFile, fitnesseRoot)) =>
        val wikiPageName = makeWikiPageName(fitnesseRoot, wikiPageFile)

        configuration.fitnesseRoot == fitnesseRoot.getName &&
          configuration.getWorkingDirectory == fitnesseRoot.getParent.getCanonicalPath &&
          configuration.wikiPageName == wikiPageName
    }

  def wikiPageInfo(project: Project, context: ConfigurationContext): Option[(VirtualFile, VirtualFile)] =
    findWikiPageFile(context) match {
      case None => None
      case Some(wikiPageFile) => Option(ModuleUtilCore.findModuleForFile(wikiPageFile, project)) match {
        case None => None
        case Some(module) => findFitnesseRoot(module) match {
          case None => None
          case Some(fitnesseRoot) => Some((wikiPageFile, fitnesseRoot))
        }
      }
    }

  def findWikiPageFile(context: ConfigurationContext): Option[VirtualFile] = {
    Option(context.getPsiLocation.getContainingFile) match {
      case None => None
      case Some(containingFile) =>
        val pageFile = containingFile.getVirtualFile
        if (pageFile.isDirectory) {
          Some(pageFile)
        } else {
          Some(pageFile.getParent)
        }
    }
  }

  def findFitnesseRoot(module: Module) = Option(module.getProject.getBaseDir.findChild("FitNesseRoot"))

  def makeWikiPageName(fitnesseRoot: VirtualFile, wikiPageFile: VirtualFile) = {
    FileUtil.getRelativePath(fitnesseRoot.getCanonicalPath, wikiPageFile.getCanonicalPath, File.separatorChar).replace(File.separatorChar, '.')
  }
}

class FitnesseRunConfigurationType extends ConfigurationType {
  val fitnesseRunConfigurationFactory: ConfigurationFactory = new ConfigurationFactory(this) {

    override def getIcon: Icon = FitnesseFileType.FILE_ICON

    override def createTemplateConfiguration(project: Project) = new FitnesseRunConfiguration(getDisplayName(), project, this)
  }

  override def getDisplayName = FitnesseBundle.message("configurationtype.displayname")

  override def getConfigurationTypeDescription = FitnesseBundle.message("configurationtype.description")

  override def getIcon = FitnesseFileType.FILE_ICON

  override def getId = FitnesseRunConfigurationType.ID

  override def getConfigurationFactories: Array[ConfigurationFactory] = Array(fitnesseRunConfigurationFactory)
}

object FitnesseRunConfigurationType {
  val ID = "FitnesseRunConfigurationType"
  
  def INSTANCE = try {
    ConfigurationTypeUtil.findConfigurationType(classOf[FitnesseRunConfigurationType])
  } catch {
    case _: java.lang.IllegalArgumentException => new FitnesseRunConfigurationType()
  }
}