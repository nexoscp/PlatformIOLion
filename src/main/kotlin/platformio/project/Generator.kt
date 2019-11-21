package platformio.project

import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.CustomStepProjectGenerator
import com.intellij.ide.util.projectWizard.ProjectSettingsStepBase
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.externalSystem.model.project.settings.ConfigurationData
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel
import com.intellij.platform.DirectoryProjectGenerator
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.platform.WebProjectGenerator
import platformio.PlatformIO
import platformio.project.ui.NewPIOProjectSettingsForm
import platformio.services.PlatformIOService
import javax.swing.Icon
import javax.swing.JComponent

class Generator : DirectoryProjectGenerator<Settings>, CustomStepProjectGenerator<ConfigurationData> {
    companion object {
        val log = Logger.getInstance(Generator::class.java)
    }

    override fun generateProject(project: Project, baseDir: VirtualFile, settings: Settings, module: Module) {
        ApplicationManager.getApplication().runWriteAction {
            val platformIOService = ServiceManager.getService(PlatformIOService::class.java)
            baseDir.createChildDirectory(this, ".idea")
            settings.boards.forEach { platformIOService.addBoardConfiguration(it) }
            platformIOService.initCLionProject()
        }
    }

    override fun getName() = PlatformIO.name

    override fun getLogo(): Icon? = PlatformIO.icon

    override fun validate(baseDirPath: String): ValidationResult = ValidationResult.OK

    override fun createPeer(): Peer = Peer()

    override fun createStep(projectGenerator: DirectoryProjectGenerator<ConfigurationData>?, callback: AbstractNewProjectStep.AbstractCallback<ConfigurationData>?): AbstractActionWithPanel {
        return ProjectSettingsStepBase<ConfigurationData>(projectGenerator, callback)
    }
}

class Peer : ProjectGeneratorPeer<Settings> {
    val form = NewPIOProjectSettingsForm(ServiceManager.getService(PlatformIOService::class.java))

    override fun validate(): ValidationInfo? {
        return null
    }

    override fun getSettings(): Settings {
        return Settings(form.boards)
    }

    override fun addSettingsStateListener(listener: WebProjectGenerator.SettingsStateListener) {
    }

    override fun buildUI(settingsStep: SettingsStep) {
    }

    override fun isBackgroundJobRunning(): Boolean {
        return false
    }

    override fun getComponent(): JComponent {
        return form.component
    }
}
