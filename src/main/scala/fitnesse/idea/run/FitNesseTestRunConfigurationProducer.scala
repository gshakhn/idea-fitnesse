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

  override def setupConfigurationFromContext(configuration: FitnesseRunConfiguration, context: ConfigurationContext, sourceElement: Ref[PsiElement]): Boolean = {
    val project = configuration.getProject

    val wikiPageFile: VirtualFile = findWikiPageFile(context)
    if (wikiPageFile == null) return false

    val module: Module = ModuleUtilCore.findModuleForFile(wikiPageFile, project)
    if (module == null) return false

    val fitnesseRoot = findFitnesseRoot (module)
    if (fitnesseRoot == null) return false

    val wikiPageName = makeWikiPageName(fitnesseRoot, wikiPageFile)

    configuration.fitnesseRoot = fitnesseRoot.getName
    configuration.setWorkingDirectory(fitnesseRoot.getParent.getCanonicalPath)
    println(s"wiki page name is: ${wikiPageName}" )
    configuration.wikiPageName = wikiPageName

    configuration.setName(wikiPageName)

    setupConfigurationModule(context, configuration)
    JavaRunConfigurationExtensionManager.getInstance().extendCreatedConfiguration(configuration, context.getLocation)

    true
  }

  override def isConfigurationFromContext(configuration: FitnesseRunConfiguration, context: ConfigurationContext): Boolean = true

  def findWikiPageFile(context: ConfigurationContext): VirtualFile = {
    val pageFile = context.getPsiLocation.getContainingFile.getVirtualFile
    if (pageFile.isDirectory) {
      pageFile
    } else {
      pageFile.getParent
    }
  }

  def findFitnesseRoot(module: Module) = module.getProject.getBaseDir.findChild("FitNesseRoot")

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