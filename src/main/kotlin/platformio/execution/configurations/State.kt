package platformio.execution.configurations

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.util.ExternalSystemApiUtil
import com.intellij.openapi.externalSystem.util.ExternalSystemApiUtil.findAll
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import platformio.PlatformIO
import platformio.external.model.EntityData
import java.io.File

class State(enviroment: ExecutionEnvironment, val configuration: RunConfiguration): CommandLineState(enviroment) {
    override fun startProcess(): ProcessHandler {
        return createCommandLine()
    }

    private fun createCommandLine(): ProcessHandler {
        val cmd = GeneralCommandLine()
        val project = environment.project
        findExternalData(project)
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

    /**
     * get {@see EntityData}
     */
    private fun findExternalData(project: Project): Collection<DataNode<EntityData>> {
        val m = ModuleManager.getInstance(project).modules
        m.forEach { n -> {
          //  val dataNode = ExternalSystemApiUtil.findModuleData(project, PlatformIO.ID, "/")
        } }
        // https://github.com/JetBrains/intellij-community/blob/659cdd44f0f61c03b8438a70e9b7d221c38d0fd1/plugins/gradle/java/testSources/importing/GradleCreateProjectTestCase.kt#L181
        val dataNode = ExternalSystemApiUtil.findProjectData(project, PlatformIO.ID, project.basePath!!)
        return findAll(dataNode as DataNode<*>, PlatformIO.KEY)
    }
}

