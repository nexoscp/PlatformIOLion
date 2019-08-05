package platformio.execution.configurations

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import platformio.Command
import platformio.PlatformIO
import javax.swing.Icon

class RunConfiguration(val _project: Project, val _factory: Factory) : RunConfiguration {
    private var _name:String = ""
    var command: Command = Command.TEST

    override fun getName(): String {
        return _name
    }

    override fun getProject(): Project {
        return _project
    }

    override fun setName(value: String) {
        _name = value
    }

    override fun getConfigurationEditor(): SettingsEditor {
        return SettingsEditor()
    }

    override fun clone(): platformio.execution.configurations.RunConfiguration {
        return RunConfiguration(_project, factory)
    }

    override fun getState(executor: Executor, enviroment: ExecutionEnvironment): RunProfileState? {
        return State(enviroment, this)
    }

    override fun getIcon(): Icon {
        return PlatformIO.icon
    }

     override fun getFactory(): Factory {
        return _factory
    }
}
