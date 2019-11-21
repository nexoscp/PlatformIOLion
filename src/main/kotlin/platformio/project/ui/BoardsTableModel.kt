package platformio.project.ui

import platformio.services.Board

import javax.swing.table.AbstractTableModel

class BoardsTableModel(private val boards: List<Board>, private val previouslySelectedBoards: Set<Board> = emptySet()) : AbstractTableModel() {
    val selectedBoards: MutableSet<Board> = HashSet(previouslySelectedBoards)

    enum class Column(val columnName: String,
                      val rowObjectFactory: (Board) -> Any
    ) {
        CHECK_BOX_COLUMN("", { false }),
        NAME_COLUMN("Name", { board -> board.name }),
        PLATFORM_COLUMN("Platform", { board -> board.platform }),
        FRAMEWORK_COLUMN("Framework", { board -> board.framework }),
        MCU_COLUMN("MCU", { board -> board.mcu }),
        FREQUENCY_COLUMN("Frequency", { board -> board.frequency.toMHz() }),
        RAM_COLUMN("RAM", { board -> board.ram.toKB() }),
        FLASH_COLUMN("Flash", { board -> board.flash.toKB() }),
    }

    companion object {
        const val BOARD_TABLE_NAME = "boardTable"
    }

    override fun getRowCount(): Int {
        return boards.size
    }

    override fun getColumnCount(): Int {
        return Column.values().size
    }

    override fun getColumnName(column: Int): String {
        return Column.values()[column].columnName
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return columnIndex == 0
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return if (columnIndex == 0) java.lang.Boolean::class.java else String::class.java
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        if (columnIndex == 0) {
            return selectedBoards.contains(boards[rowIndex])
        }

        return Column.values()[columnIndex].rowObjectFactory.invoke(boards[rowIndex])
    }

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        if (aValue is Boolean) {
            if (aValue) {
                selectedBoards.add(boards[rowIndex])
            } else {
                selectedBoards.remove(boards[rowIndex])
            }
        }
    }

    fun clearNewlySelectedBoards() {
        selectedBoards.retainAll(previouslySelectedBoards)
    }
}
