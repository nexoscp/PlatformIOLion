package platformio.project.ui

import platformio.services.Board
import java.awt.Component
import javax.swing.JTable
import javax.swing.RowFilter
import javax.swing.table.TableCellRenderer
import javax.swing.table.TableRowSorter


class BoardTable(boards: List<Board>, selectedBoards: Set<Board>) {
    private val boardTableModel: BoardsTableModel = BoardsTableModel(boards, selectedBoards)

    val table = object : JTable(boardTableModel) {
        override fun prepareRenderer(renderer: TableCellRenderer?, row: Int, column: Int): Component {
            val component = super.prepareRenderer(renderer, row, column)
            val rendererWidth = component.preferredSize.width
            val tableColumn = getColumnModel().getColumn(column)
            tableColumn.preferredWidth = Math.max(rendererWidth + intercellSpacing.width, tableColumn.preferredWidth)
            return component
        }
    }
    private var rowSorter = TableRowSorter(boardTableModel)

    init {
        table.fillsViewportHeight = true
        table.name = BoardsTableModel.BOARD_TABLE_NAME
        table.rowSorter = rowSorter
        table.autoResizeMode = JTable.AUTO_RESIZE_OFF
    }

    fun clearNewlySelectedBoards() = boardTableModel.clearNewlySelectedBoards()

    fun getSelectedBoards(): Collection<Board> = boardTableModel.selectedBoards

    fun filterTable(text: String) {
        if (text.trim { it <= ' ' }.isEmpty()) {
            rowSorter.setRowFilter(null)
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)$text"))
        }
    }
}