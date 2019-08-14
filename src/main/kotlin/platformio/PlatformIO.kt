package platformio

import com.intellij.icons.AllIcons
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.externalSystem.model.Key
import com.intellij.openapi.externalSystem.model.ProjectKeys
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import platformio.external.model.EntityData
import platformio.filetype.FileType
import javax.swing.Icon

object PlatformIO {
    const val name = "PlatformIO"
    val fileType = FileType()
    val ID = ProjectSystemId(name)
    const val INI = "platformio.ini"
    val log = Logger.getInstance(name)
    val icon: Icon = AllIcons.General.Information
    val KEY = Key.create(EntityData::class.java, ProjectKeys.PROJECT.processingWeight + 1)
}
