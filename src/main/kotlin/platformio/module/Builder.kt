package platformio.module

/**
 *  @see https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000055950-how-can-you-add-items-to-CLion-s-New-Project-window
 */
class Builder(private val type:Type) : com.intellij.ide.util.projectWizard.ModuleBuilder() {
    override fun getModuleType():Type {
        return type
    }
}
