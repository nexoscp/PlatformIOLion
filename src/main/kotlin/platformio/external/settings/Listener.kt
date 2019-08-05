package platformio.external.settings

import com.intellij.openapi.externalSystem.settings.ExternalSystemSettingsListener
import platformio.PlatformIO

class Listener : ExternalSystemSettingsListener<Project> {
    override fun onBulkChangeEnd() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProjectRenamed(p0: String, p1: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBulkChangeStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProjectsLinked(p0: MutableCollection<Project>) {
        PlatformIO.log.info("onProjectsLinked")
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProjectsUnlinked(p0: MutableSet<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUseAutoImportChange(p0: Boolean, p1: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
