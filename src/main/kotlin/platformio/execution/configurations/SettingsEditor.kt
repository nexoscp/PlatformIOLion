package platformio.execution.configurations

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.ComboBox
import platformio.Command
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class SettingsEditor : SettingsEditor<RunConfiguration>() {
    var panel: EditorComponent? = null

    override fun resetEditorFrom(configuration: RunConfiguration) {
        panel?.readFrom(configuration)
    }

    override fun createEditor(): JComponent {
        val p = EditorComponent()
        panel = p
        return p
    }

    override fun applyEditorTo(configuration: RunConfiguration) {
        panel?.writeTo(configuration)
    }
}

class EditorComponent: JPanel {
    var command: ComboBox<Command>

    constructor() {
        val model = DefaultComboBoxModel(Command.values())
        add(JLabel("PlatformIO is here!"))
        command = ComboBox(model)
        add(command)
    }

    fun readFrom(configuration: RunConfiguration) {
        command.selectedItem = configuration.command
    }

    fun writeTo(configuration: RunConfiguration) {
        configuration.command = command.selectedItem as Command
    }
}

