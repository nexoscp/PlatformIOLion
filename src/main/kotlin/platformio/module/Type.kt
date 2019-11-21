package platformio.module

import com.intellij.openapi.module.ModuleType
import platformio.PlatformIO
import javax.swing.Icon

/**
 *  @see https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000055950-how-can-you-add-items-to-CLion-s-New-Project-window
*/
class Type : ModuleType<Builder>(PlatformIO.ID.id) {
    override fun createModuleBuilder(): Builder {
        return Builder(this)
    }

    override fun getName(): String {
        return PlatformIO.name
    }

    override fun getDescription(): String {
        return "PlatformIO module"
    }

    override fun getNodeIcon(isOpened: Boolean): Icon {
        return PlatformIO.icon
    }
}
