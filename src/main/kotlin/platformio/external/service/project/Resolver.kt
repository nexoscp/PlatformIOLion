package platformio.external.service.project

import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.ProjectKeys
import com.intellij.openapi.externalSystem.model.project.ProjectData
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener
import com.intellij.openapi.externalSystem.service.project.ExternalSystemProjectResolver
import com.intellij.openapi.externalSystem.task.ExternalSystemTaskManager
import platformio.PlatformIO
import platformio.external.model.EntityData
import platformio.external.settings.Execution

class Resolver : ExternalSystemProjectResolver<Execution>, ExternalSystemTaskManager<Execution> {

    /**
     * @see https://android.googlesource.com/platform/tools/idea/+/snapshot-master/plugins/gradle/src/org/jetbrains/plugins/gradle/service/project/GradleProjectResolver.java#213
     */
    override fun resolveProjectInfo(taskId: ExternalSystemTaskId, p1: String, p2: Boolean, p3: Execution?, p4: ExternalSystemTaskNotificationListener): DataNode<ProjectData>? {
        PlatformIO.log.info("resolveProjectInfo $taskId")
        val data = ProjectData(PlatformIO.ID, PlatformIO.name, "", "")
        val node = DataNode(ProjectKeys.PROJECT, data, null)
        node.createChild(PlatformIO.KEY, EntityData(PlatformIO.ID))
        return node
    }

    override fun cancelTask(p0: ExternalSystemTaskId, p1: ExternalSystemTaskNotificationListener): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
