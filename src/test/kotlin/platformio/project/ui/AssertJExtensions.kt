package platformio.project.ui

import org.assertj.swing.data.TableCell
import org.assertj.swing.fixture.JTableFixture
import platformio.services.Board


fun JTableFixture.hasBoard(rowIndex: Int, board: Board, selected: Boolean) {
    cell(TableCell.row(rowIndex).column(0)).requireValue(selected.toString())
    cell(TableCell.row(rowIndex).column(1)).requireValue(board.name)
    cell(TableCell.row(rowIndex).column(2)).requireValue(board.platform)
    cell(TableCell.row(rowIndex).column(3)).requireValue(board.framework)
    cell(TableCell.row(rowIndex).column(4)).requireValue(board.mcu)
    cell(TableCell.row(rowIndex).column(5)).requireValue(board.frequency.toMHz())
    cell(TableCell.row(rowIndex).column(6)).requireValue(board.ram.toKB())
    cell(TableCell.row(rowIndex).column(7)).requireValue(board.flash.toKB())
}