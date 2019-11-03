package platformio.project.ui

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.junit.Assert.assertThat
import org.junit.Test

class CheckboxListItemListModelTest {

    private val itemA = CheckboxListItem("a")
    private val itemB = CheckboxListItem("b", selected = true)

    @Test
    fun elementAccess() {
        val listModel = CheckboxListItemListModel(listOf(itemA, itemB))

        assertThat(listModel.size, `is`(2))
        assertThat(listModel.getElementAt(0), `is`(itemA))
        assertThat(listModel.getElementAt(1), `is`(itemB))
    }

    @Test
    fun selectedItems() {
        val listModel = CheckboxListItemListModel(listOf(itemA, itemB))
        assertThat(listModel.getSelectedItems(), contains(itemB.label))
    }
}