package platformio.external.settings

import com.intellij.openapi.externalSystem.settings.ExternalProjectSettings

class Project : ExternalProjectSettings() {
    override fun clone(): ExternalProjectSettings {
        return Project()
    }
}
