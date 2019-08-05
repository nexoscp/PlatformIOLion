package platformio.external.settings

import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemSettings
import com.intellij.openapi.externalSystem.settings.ExternalSystemSettingsListener
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic
import platformio.PlatformIO

class Settings(topic: Topic<Listener>, project: Project) : AbstractExternalSystemSettings<Settings, platformio.external.settings.Project, Listener>(topic, project) {
    private val listener = mutableListOf<ExternalSystemSettingsListener<platformio.external.settings.Project>>()
    override fun checkSettings(p0: platformio.external.settings.Project, p1: platformio.external.settings.Project) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun copyExtraSettingsFrom(p0: Settings) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe(l: ExternalSystemSettingsListener<platformio.external.settings.Project>) {
        PlatformIO.log.info("***** 1 listener")
         listener.add(l)
    }
}
