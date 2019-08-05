package platformio.external.settings

import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemLocalSettings
import com.intellij.openapi.project.Project

class Local(externalSystemId: ProjectSystemId, project: Project, state: LocalState) : AbstractExternalSystemLocalSettings<LocalState>( externalSystemId, project, state)
