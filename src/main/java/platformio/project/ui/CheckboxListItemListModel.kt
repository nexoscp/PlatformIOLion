package platformio.project.ui

import javax.swing.AbstractListModel

class CheckboxListItemListModel(private val items: List<CheckboxListItem>) : AbstractListModel<CheckboxListItem>() {

    override fun getSize(): Int {
        return items.size
    }

    override fun getElementAt(index: Int): CheckboxListItem? {
        return items[index]
    }

    fun getSelectedItems(): List<String> {
        return items.filter { it.selected }.map { it.label }
    }

}
