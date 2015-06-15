package fitnesse.idea.run

import com.intellij.execution.application.ApplicationConfiguration
import com.intellij.execution.configuration.ConfigurationFactoryEx
import com.intellij.openapi.project.Project

class FitnesseRunConfiguration(name: String, project: Project, factory: ConfigurationFactoryEx) extends ApplicationConfiguration(name, project, factory) {

}
