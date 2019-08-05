package platformio.execution.configurations

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.project.Project

class Factory(type: Type) : ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return RunConfiguration(project, this)
    }
}
