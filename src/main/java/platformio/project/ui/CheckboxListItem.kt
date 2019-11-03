package platformio.project.ui

data class CheckboxListItem(val label: String, var selected: Boolean = false) {

    constructor(label: String) : this(label, false)


    override fun toString(): String {
        return label
    }
}
