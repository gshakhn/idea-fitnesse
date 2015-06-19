package fitnesse.idea.run

import java.io.File

import com.intellij.execution.configurations._
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.{ProgramRunner, ExecutionEnvironment}
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.util.JavaParametersUtil
import com.intellij.execution._
import com.intellij.execution.application.ApplicationConfiguration
import com.intellij.execution.configuration.ConfigurationFactoryEx
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.PathUtil
import fitnesseMain.FitNesseMain

class FitnesseRunConfiguration(name: String, project: Project, factory: ConfigurationFactoryEx) extends ApplicationConfiguration(name, project, factory) {

  val testFrameworkName: String = "FitNesse"

  @throws(classOf[ExecutionException])
  override def getState(executor: Executor, env: ExecutionEnvironment): RunProfileState = {
    new JavaCommandLineState(env) {

      @throws(classOf[ExecutionException])
      protected override def createJavaParameters: JavaParameters = {
        val params: JavaParameters = new JavaParameters

        val module: Module = getConfigurationModule.getModule
        if (module == null) throw new ExecutionException("Module is not specified")

        val classPathType: Int = JavaParameters.JDK_AND_CLASSES_AND_TESTS

//        val jreHome: String = if (FitnesseRunConfiguration.this.ALTERNATIVE_JRE_PATH_ENABLED) ALTERNATIVE_JRE_PATH else null
//        JavaParametersUtil.configureModule(module, params, classPathType, jreHome)
//        JavaParametersUtil.configureConfiguration(params, FitnesseRunConfiguration.this)
        params.configureByModule(module, classPathType, JavaParameters.getModuleJdk(module))

        val path: String = getSMRunnerPath
        params.getClassPath.add(getSMRunnerPath)

        params.setMainClass("fitnesseMain.FitNesseMain")

//        var f: File = new File(myFilePath)
//        if (!f.isDirectory) {
//          f = f.getParentFile
//        }
//        params.getVMParametersList.addParametersString("-Dorg.jetbrains.run.directory=\"" + f.getAbsolutePath + "\"")

        params.getProgramParametersList.addParametersString("-d")
        params.getProgramParametersList.addParametersString(".")
        params.getProgramParametersList.addParametersString("-c")
        params.getProgramParametersList.addParametersString(MAIN_CLASS_NAME + "?suite&format=text")

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

  private def getSMRunnerPath: String = {
    PathUtil.getJarPathForClass(classOf[FitNesseMain])
  }
}
