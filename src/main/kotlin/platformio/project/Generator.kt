package platformio.project

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.platform.DirectoryProjectGenerator
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.platform.WebProjectGenerator
import platformio.PlatformIO
import platformio.toolwindow.console.ID
import java.io.File
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JLabel

class Generator : DirectoryProjectGenerator<Settings> {
    override fun generateProject(project: Project, baseDir: VirtualFile, settings: Settings, module: Module) {
        ApplicationManager.getApplication().runWriteAction {
            baseDir.createChildDirectory(this, ".idea")
            val init = GeneralCommandLine("platformio", "init", "--ide", "clion")
            init.workDirectory = File(baseDir.path)
            val processHandler = OSProcessHandler(init)

            processHandler.addProcessListener(object: ProcessListener {
                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {}

                override fun processTerminated(event: ProcessEvent) {
                    VirtualFileManager.getInstance().asyncRefresh(null)
                }

                override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {}

                override fun startNotified(event: ProcessEvent) {}
            })

            val console = TextConsoleBuilderFactory
                    .getInstance()
                    .createBuilder(project)
                    .console
            console.attachToProcess(processHandler)
            val contentManager = ToolWindowManager
                    .getInstance(project)
                    .getToolWindow(ID)
                    .contentManager
            val content  = contentManager.factory.createContent( console.component, "platformio init", false)
            contentManager.addContent(content)
            processHandler.startNotify()
        }
    }

    override fun getName() = PlatformIO.name

    override fun getLogo(): Icon? = PlatformIO.icon

    override fun validate(baseDirPath: String): ValidationResult = ValidationResult.OK

    override fun createPeer(): Peer = Peer()
}

class Peer : ProjectGeneratorPeer<Settings> {
    override fun validate(): ValidationInfo? {
        return null
    }

    override fun getSettings(): Settings {
        return Settings()
    }

    override fun addSettingsStateListener(listener: WebProjectGenerator.SettingsStateListener) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildUI(settingsStep: SettingsStep) {
    }

    override fun isBackgroundJobRunning(): Boolean {
        return true //TODO run platformio for list of frameworks and platforms
    }

    override fun getComponent(): JComponent {
        return JLabel("PIOL")
    }
}
