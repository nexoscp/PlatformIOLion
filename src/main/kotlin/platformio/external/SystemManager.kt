package platformio.external

import com.intellij.execution.configurations.SimpleJavaParameters
import com.intellij.openapi.externalSystem.ExternalSystemManager
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.service.project.ExternalSystemProjectResolver
import com.intellij.openapi.externalSystem.task.ExternalSystemTaskManager
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair
import com.intellij.util.Function
import com.intellij.util.messages.Topic
import platformio.*
import platformio.external.service.project.Resolver
import platformio.external.settings.*

val TOPIC = Topic.create("PlatformIO-specific settings", Listener::class.java)

class SystemManager : ExternalSystemManager<platformio.external.settings.Project, Listener, Settings, Local, Execution> {
    override fun enhanceRemoteProcessing(parameters: SimpleJavaParameters) {
        //Ignore for now
    }

    override fun getProjectResolverClass(): Class<out ExternalSystemProjectResolver<Execution>> {
      return Resolver::class.java
    }

    override fun getSettingsProvider(): Function<Project, Settings> {
        return Function { project -> Settings(TOPIC, project) }
    }

    override fun getExecutionSettingsProvider(): Function<Pair<Project, String>, Execution> {
        return Function { pair -> Execution() } //FIXME what do this pair mean? should bee some arguments Could be Task-Name and Description
    }

    override fun getExternalProjectDescriptor(): FileChooserDescriptor {
        return FileChooserDescriptor(true, true, false, false, false, true)
    }

    override fun getSystemId(): ProjectSystemId {
        return PlatformIO.ID
    }

    override fun getTaskManagerClass(): Class<out ExternalSystemTaskManager<Execution>> {
        return Resolver::class.java
    }

    override fun getLocalSettingsProvider(): Function<Project, Local> {
        return  Function { project -> Local(PlatformIO.ID, project, LocalState()) }
    }
}
