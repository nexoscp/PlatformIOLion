package platformio.execution.configurations

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import java.io.File

class State(environment: ExecutionEnvironment, private val configuration: RunConfiguration): CommandLineState(environment) {
    override fun startProcess(): ProcessHandler {
        val cmd = GeneralCommandLine()
        val project = environment.project
        cmd.workDirectory =  File(project.basePath)
        cmd.exePath = "platformio"
        cmd.addParameter(configuration.command.command)
        if (!cmd.environment.containsKey("TERM")) {
            cmd.environment["TERM"] = "xterm-256color"
        }
        val processHandler = KillableColoredProcessHandler(cmd)
        ProcessTerminatedListener.attach(processHandler, project)
        return processHandler
    }
}

