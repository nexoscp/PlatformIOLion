package platformio.execution.configurations

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import platformio.PlatformIO
import javax.swing.Icon

class Type : ConfigurationType {
    override fun getIcon(): Icon {
        return PlatformIO.icon
    }

    override fun getConfigurationTypeDescription(): String {
        return PlatformIO.name
    }

    override fun getId(): String {
        return PlatformIO.name
    }

    override fun getDisplayName(): String {
        return PlatformIO.name
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(Factory(this))
    }
}
