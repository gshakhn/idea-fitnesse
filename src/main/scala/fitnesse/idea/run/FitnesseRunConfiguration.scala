package fitnesse.idea.run

import java.io.File
import java.util.Properties

import com.intellij.execution._
import com.intellij.execution.application.ApplicationConfiguration
import com.intellij.execution.configurations._
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.{ExecutionEnvironment, ProgramRunner}
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.util.{JavaParametersUtil, ProgramParametersConfigurator, ProgramParametersUtil}
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.{SettingsEditor, SettingsEditorGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.util.{InvalidDataException, JDOMExternalizer, WriteExternalException}
import com.intellij.util.PathUtil
import fitnesse.ConfigurationParameter._
import fitnesse.components.ComponentFactory
import fitnesse.idea.FitnesseBundle
import fitnesse.idea.rt.IntelliJFormatter
import fitnesse.wiki.fs.FileSystemPageFactory
import fitnesse.wiki.{PathParser, SystemVariableSource}
import fitnesse.{ConfigurationParameter, ContextConfigurator}
import fitnesseMain.FitNesseMain
import org.jdom.Element

import scala.beans.BeanProperty

// How to persist this run configuration???
class FitnesseRunConfiguration(testFrameworkName: String, project: Project, factory: ConfigurationFactory) extends ApplicationConfiguration(testFrameworkName, project, factory) {

  @BeanProperty
  var wikiPageName: String = null

  @BeanProperty
  var fitnesseRoot: String = "FitNesseRoot"

  @BeanProperty
  var configFile: String = null

  override def getConfigurationEditor(): SettingsEditor[_ <: RunConfiguration] = {
    val group: SettingsEditorGroup[FitnesseRunConfiguration] = new SettingsEditorGroup[FitnesseRunConfiguration]
    group.addEditor(ExecutionBundle.message("run.configuration.configuration.tab.title"), new FitnesseApplicationConfigurable(getProject))
    group
  }

  @throws(classOf[ExecutionException])
  override def getState(executor: Executor, env: ExecutionEnvironment): RunProfileState = {
    new JavaCommandLineState(env) {

      @throws(classOf[ExecutionException])
      protected override def createJavaParameters: JavaParameters = {
        val params: JavaParameters = new JavaParameters

        val module: Module = getConfigurationModule.getModule
        if (module == null) throw new ExecutionException("Module is not specified")

        val classPathType: Int = JavaParameters.JDK_AND_CLASSES_AND_TESTS

        val jreHome: String = if (FitnesseRunConfiguration.this.ALTERNATIVE_JRE_PATH_ENABLED) ALTERNATIVE_JRE_PATH else null
        JavaParametersUtil.configureModule(module, params, classPathType, jreHome)
        JavaParametersUtil.configureConfiguration(params, FitnesseRunConfiguration.this)
//        params.configureByModule(module, classPathType, JavaParameters.getModuleJdk(module))

        params.getClassPath.add(getFitNesseMainPath)
        params.getClassPath.add(getFormatterPath)

        params.setMainClass("fitnesseMain.FitNesseMain")

//        var f: File = new File(myFilePath)
//        if (!f.isDirectory) {
//          f = f.getParentFile
//        }
        params.getVMParametersList.addParametersString("-DFormatters=\"" + classOf[IntelliJFormatter].getName + "\"")

        params.getProgramParametersList.addParametersString("-o")
        params.getProgramParametersList.addParametersString("-d")
        if (StringUtil.isEmptyOrSpaces(getWorkingDirectory)) {
          params.getProgramParametersList.addParametersString(getProject.getBasePath)
        } else {
          params.getProgramParametersList.addParametersString(getWorkingDirectory)
        }
        params.getProgramParametersList.addParametersString("-r")
        params.getProgramParametersList.addParametersString(fitnesseRoot)
        params.getProgramParametersList.addParametersString("-c")
        params.getProgramParametersList.addParametersString(wikiPageName + "?suite&nohistory&debug&format=text")

        for (ext <- Extensions.getExtensions(RunConfigurationExtension.EP_NAME)) {
          ext.updateJavaParameters(FitnesseRunConfiguration.this, params, getRunnerSettings)
        }

        params
      }

      @throws(classOf[ExecutionException])
      private def createConsole(executor: Executor, processHandler: ProcessHandler): ConsoleView = {
        val consoleProperties: SMTRunnerConsoleProperties = new SMTRunnerConsoleProperties(FitnesseRunConfiguration.this, testFrameworkName, executor)
        SMTestRunnerConnectionUtil.createAndAttachConsole(testFrameworkName, processHandler, consoleProperties, getEnvironment)
      }

      @throws(classOf[ExecutionException])
      override def execute(executor: Executor, runner: ProgramRunner[_ <: RunnerSettings]): ExecutionResult = {
        val processHandler: ProcessHandler = startProcess
        val console: ConsoleView = createConsole(executor, processHandler)
        new DefaultExecutionResult(console, processHandler, createActions(console, processHandler, executor):_*)
      }
    }
  }

  @throws[RuntimeConfigurationException]
  override def checkConfiguration() = {
    JavaParametersUtil.checkAlternativeJRE(this)
    val configurationModule = this.getConfigurationModule
    ProgramParametersUtil.checkWorkingDirectoryExist(this, getProject, configurationModule.getModule)

    // Check if fitnesseRoot can be found in working dir
    val workingDir: String = new ProgramParametersConfigurator().getWorkingDir(this, getProject, configurationModule.getModule)
    if (!new File(workingDir, fitnesseRoot).exists()) {
      throw new RuntimeConfigurationWarning(FitnesseBundle.message("run.configuration.fitnesseRoot.notfound", workingDir, fitnesseRoot))
    }

    // TODO: Check if plugin config file exists

    // Check if test suite exists under FitnesseRoot
    val properties = new Properties
    properties.putAll(System.getProperties)
    properties.putAll(ConfigurationParameter.loadProperties(new File(workingDir, Option(configFile).getOrElse(ContextConfigurator.DEFAULT_CONFIG_FILE))))
    val componentFactory = new ComponentFactory(properties)

    val wikiPageFactory = componentFactory.createComponent(WIKI_PAGE_FACTORY_CLASS, classOf[FileSystemPageFactory])
    val rootPage = wikiPageFactory.makePage(new File(workingDir, fitnesseRoot), fitnesseRoot, null, new SystemVariableSource());

    if (!rootPage.getPageCrawler.pageExists(PathParser.parse(wikiPageName))) {
      throw new RuntimeConfigurationWarning(FitnesseBundle.message("run.configuration.wikiPageName.notfound", wikiPageName))
    }

    JavaRunConfigurationExtensionManager.checkConfigurationIsValid(this)
  }

  private def getFitNesseMainPath: String = {
    PathUtil.getJarPathForClass(classOf[FitNesseMain])
  }

  private def getFormatterPath: String = {
    PathUtil.getJarPathForClass(classOf[IntelliJFormatter])
  }

  @throws[InvalidDataException]
  override def readExternal(element: Element) = {
    super.readExternal(element)
    wikiPageName = JDOMExternalizer.readString(element, "wikiPageName")
    fitnesseRoot = JDOMExternalizer.readString(element, "fitnesseRoot")
  }

  @throws[WriteExternalException]
  override def writeExternal(element: Element) {
    super.writeExternal(element)
    JDOMExternalizer.write(element, "wikiPageName", wikiPageName)
    JDOMExternalizer.write(element, "fitnesseRoot", fitnesseRoot)
  }

}
