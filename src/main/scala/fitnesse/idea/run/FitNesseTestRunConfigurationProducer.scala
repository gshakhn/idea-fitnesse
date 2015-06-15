package fitnesse.idea.run

import javax.swing.Icon

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.application.ApplicationConfigurationType
import com.intellij.execution.configuration.ConfigurationFactoryEx
import com.intellij.execution.configurations._
import com.intellij.execution.junit.JavaRunConfigurationProducerBase
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.{PsiDirectory, PsiElement}
import fitnesse.idea.lang.filetype.FitnesseFileType

class FitNesseTestRunConfigurationProducer extends JavaRunConfigurationProducerBase[FitnesseRunConfiguration](FitnesseRunConfigurationType.INSTANCE) {

  override def setupConfigurationFromContext(configuration: FitnesseRunConfiguration, context: ConfigurationContext, sourceElement: Ref[PsiElement]): Boolean = {
    val virtualFile: VirtualFile = getFileToRun(context) match {
      case Some(vf) => vf
      case None => return false
    }

    val project = configuration.getProject
    val element = context.getPsiLocation
    if (element == null) return false

    val module = ModuleUtilCore.findModuleForFile(virtualFile, project)
    if (module == null) return false


    true
  }

  override def isConfigurationFromContext(configuration: FitnesseRunConfiguration, context: ConfigurationContext): Boolean = true

  protected def getFileToRun(context: ConfigurationContext): Option[VirtualFile] = {
    Option(context.getPsiLocation) match {
      case Some(dir: PsiDirectory) => Some(dir.getVirtualFile)
      case _ => None
    }
  }

  /*
    @Override
  protected boolean setupConfigurationFromContext(CucumberJavaRunConfiguration configuration,
                                                  ConfigurationContext context,
                                                  Ref<PsiElement> sourceElement) {
    final VirtualFile virtualFile = getFileToRun(context);
    if (virtualFile == null) {
      return false;
    }

    final Project project = configuration.getProject();
    final PsiElement element = context.getPsiLocation();

    if (element == null) {
      return false;
    }

    final Module module = ModuleUtilCore.findModuleForFile(virtualFile, project);
    if (module == null) return false;

    String mainClassName = null;
    String formatterOptions = null;
    final Location location = context.getLocation();
    if (location != null) {
      if (LocationUtil.isJarAttached(location, CUCUMBER_1_0_MAIN_CLASS, new PsiDirectory[0])) {
        mainClassName = CUCUMBER_1_0_MAIN_CLASS;
      } else if (LocationUtil.isJarAttached(location, CUCUMBER_1_1_MAIN_CLASS, new PsiDirectory[0])) {
        mainClassName = CUCUMBER_1_1_MAIN_CLASS;
      }

      if (LocationUtil.isJarAttached(location, CUCUMBER_1_2_PLUGIN_CLASS, new PsiDirectory[0]))  {
        formatterOptions = FORMATTER_OPTIONS_1_2;
      } else {
        formatterOptions = FORMATTER_OPTIONS;
      }
    }
    if (mainClassName == null) {
      return false;
    }

    final VirtualFile file = getFileToRun(context);
    if (file == null) {
      return false;
    }
    if (StringUtil.isEmpty(configuration.getGlue())) {
      final NullableComputable<String> glue = getStepsGlue(element);
      configuration.setGlue(glue);
    }
    configuration.setNameFilter(getNameFilter(context));
    configuration.setFilePath(file.getPath());
    configuration.setProgramParameters(formatterOptions);
    if (StringUtil.isEmpty(configuration.MAIN_CLASS_NAME)) {
      configuration.MAIN_CLASS_NAME = mainClassName;
    }

    if (configuration.getNameFilter() != null && configuration.getNameFilter().length() > 0) {
      final String newProgramParameters = configuration.getProgramParameters() + " --name \"" + configuration.getNameFilter() + "\"";
      configuration.setProgramParameters(newProgramParameters);
    }

    configuration.myGeneratedName = getConfigurationName(context);
    configuration.setGeneratedName();

    setupConfigurationModule(context, configuration);
    JavaRunConfigurationExtensionManager.getInstance().extendCreatedConfiguration(configuration, location);
    return true;
  }

  @Override
  public boolean isConfigurationFromContext(CucumberJavaRunConfiguration runConfiguration, ConfigurationContext context) {
    final Location location = JavaExecutionUtil.stepIntoSingleClass(context.getLocation());

    final VirtualFile fileToRun = getFileToRun(context);
    if (fileToRun == null) {
      return false;
    }

    if (!fileToRun.getPath().equals(runConfiguration.getFilePath())) {
      return false;
    }

    if (!Comparing.strEqual(getNameFilter(context), runConfiguration.getNameFilter())) {
      return false;
    }

    final Module configurationModule = runConfiguration.getConfigurationModule().getModule();
    if (!Comparing.equal(location.getModule(), configurationModule)) {
      return false;
    }

    return true;
  }
  */
}

object FitNesseTestRunConfigurationProducer {
  // Create a runner, based on the jUnit runner
  val FITNESSE_MAIN_CLASS = "fitnesse.idea.run.FitNesseRunner"
  val FITNESSE_OPTS = "-c"
  // TODO: something that calls back to IntelliJ
}

class FitnesseRunConfigurationType extends ApplicationConfigurationType {
  val fitnesseRunConfigurationFactory: ConfigurationFactory = new ConfigurationFactoryEx(this) {

    override def getIcon: Icon = FitnesseFileType.FILE_ICON

    override def createTemplateConfiguration(project: Project) = new FitnesseRunConfiguration(getDisplayName(), project, this)

    override def onNewConfigurationCreated(configuration: RunConfiguration) = configuration.asInstanceOf[ModuleBasedConfiguration[RunConfigurationModule]].onNewConfigurationCreated()
  }

  override def getDisplayName = "FitNesse"

  override def getConfigurationTypeDescription = "FitNesse"

  override def getIcon = FitnesseFileType.FILE_ICON

  override def getId = FitnesseRunConfigurationType.ID

  override def getConfigurationFactories: Array[ConfigurationFactory] = Array(fitnesseRunConfigurationFactory)
}

object FitnesseRunConfigurationType {
  val ID = "FitnesseRunConfigurationType"
  
  def INSTANCE = ConfigurationTypeUtil.findConfigurationType(classOf[FitnesseRunConfigurationType])
}