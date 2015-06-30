package fitnesse.idea.run

import com.intellij.execution._
import com.intellij.execution.application.ApplicationConfiguration
import com.intellij.execution.configuration.ConfigurationFactoryEx
import com.intellij.execution.configurations._
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.{ExecutionEnvironment, ProgramRunner}
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.util.{JavaParametersUtil, ProgramParametersUtil}
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.{SettingsEditor, SettingsEditorGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.PathUtil
import fitnesse.idea.rt.IntelliJFormatter
import fitnesseMain.FitNesseMain

import scala.beans.BeanProperty

// How to persist this run configuration???
class FitnesseRunConfiguration(name: String, project: Project, factory: ConfigurationFactoryEx) extends ApplicationConfiguration(name, project, factory) {

  val testFrameworkName: String = "FitNesse"

  @BeanProperty
  var wikiPageName: String = null

  @BeanProperty
  var fitnesseRoot: String = "FitNesseRoot"


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
    ProgramParametersUtil.checkWorkingDirectoryExist(this, this.getProject, configurationModule.getModule)
    JavaRunConfigurationExtensionManager.checkConfigurationIsValid(this)
  }

  private def getFitNesseMainPath: String = {
    PathUtil.getJarPathForClass(classOf[FitNesseMain])
  }

  private def getFormatterPath: String = {
    PathUtil.getJarPathForClass(classOf[IntelliJFormatter])
  }
}
