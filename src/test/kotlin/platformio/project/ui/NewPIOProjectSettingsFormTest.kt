package platformio.project.ui

import org.assertj.swing.data.TableCell
import org.assertj.swing.edt.GuiActionRunner
import org.assertj.swing.fixture.DialogFixture
import org.assertj.swing.fixture.FrameFixture
import org.assertj.swing.fixture.JTableFixture
import org.assertj.swing.timing.Timeout
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import platformio.project.ui.BoardCatalogDialog.BOARD_CATALOG_DIALOG_NAME
import platformio.project.ui.NewPIOProjectSettingsForm.SELECT_BOARD_BUTTON_NAME
import platformio.services.Board
import platformio.services.PlatformIOService
import javax.swing.JFrame
import kotlin.test.fail

@RunWith(MockitoJUnitRunner::class)
class NewPIOProjectSettingsFormTest {
    @Mock
    private lateinit var platformIOService: PlatformIOService
    @Mock
    private lateinit var form: NewPIOProjectSettingsForm

    private val frame = JFrame()
    private val window = FrameFixture(GuiActionRunner.execute<JFrame> { frame })

    private val boards = listOf(boardA, boardB, boardC)

    @Before
    fun setUp() {
        `when`(platformIOService.loadAllBoards()).thenReturn(boards)
    }

    private fun showWindow() {
        form = NewPIOProjectSettingsForm(platformIOService)
        frame.add(form.component)
        window.show()
    }

    @After
    fun tearDown() {
        window.cleanUp()
    }

    @Test
    fun picksBoards() {
        showWindow()

        clickOnBoards(boardA, boardC)

        val selectedBoardTable = window.table(NewPIOProjectSettingsForm.SELECTED_BOARD_TABLE_NAME)
        selectedBoardTable.requireRowCount(2)

        verifyColumnNames(selectedBoardTable)

        selectedBoardTable.boardExists(boardA)
        selectedBoardTable.boardExists(boardC)

        Assert.assertThat(form.boards, Matchers.containsInAnyOrder(boardA, boardC))
    }


    @Test
    fun `picks boards, opens dialog again and picks no new boards`() {
        showWindow()

        clickOnBoards(boardA, boardC)

        window.openSelectBoardDialog().button(BoardCatalogDialog.CANCEL_BUTTON_NAME).click()

        val selectedBoardTable = window.table(NewPIOProjectSettingsForm.SELECTED_BOARD_TABLE_NAME)
        selectedBoardTable.requireRowCount(2)

        selectedBoardTable.boardExists(boardA)
        selectedBoardTable.boardExists(boardC)

        Assert.assertThat(form.boards, Matchers.containsInAnyOrder(boardA, boardC))
    }


    @Test
    fun pickOneBoardAndThanAddAnotherOne() {
        showWindow()

        clickOnBoards(boardA)
        clickOnBoards(boardC) { it.table(BoardsTableModel.BOARD_TABLE_NAME).hasBoard(0, boardA, true) }

        val selectedBoardTable = window.table(NewPIOProjectSettingsForm.SELECTED_BOARD_TABLE_NAME)
        selectedBoardTable.requireRowCount(2)

        verifyColumnNames(selectedBoardTable)

        selectedBoardTable.boardExists(boardA)
        selectedBoardTable.boardExists(boardC)
    }

    @Test
    fun pickOneBoardAndThanUnPickIt() {
        showWindow()

        clickOnBoards(boardA)
        clickOnBoards(boardA, boardC)

        val selectedBoardTable = window.table(NewPIOProjectSettingsForm.SELECTED_BOARD_TABLE_NAME)
        selectedBoardTable.requireRowCount(1)

        selectedBoardTable.boardDoesNotExist(boardA)
        selectedBoardTable.boardExists(boardC)
    }

    private fun clickOnBoards(vararg boards: Board, extraAssertions: (DialogFixture) -> Any = {}) {
        val dialog = window.openSelectBoardDialog()
        dialog.requireModal()
        extraAssertions.invoke(dialog)
        boards.forEach { dialog.clickOnBoards(it) }
        dialog.button(BoardCatalogDialog.OK_BUTTON_NAME).click()
    }

    private fun FrameFixture.openSelectBoardDialog(): DialogFixture {
        button(SELECT_BOARD_BUTTON_NAME).click()
        return dialog(BOARD_CATALOG_DIALOG_NAME, Timeout.timeout(500))
    }

    private fun verifyColumnNames(selectedBoardTable: JTableFixture) {
        selectedBoardTable.requireColumnCount(3)
        selectedBoardTable.requireColumnNamed(NewPIOProjectSettingsForm.NAME_COLUMN)
        selectedBoardTable.requireColumnNamed(NewPIOProjectSettingsForm.PLATFORM_COLUMN)
        selectedBoardTable.requireColumnNamed(NewPIOProjectSettingsForm.FRAMEWORK_COLUMN)
    }

    private fun JTableFixture.boardExists(board: Board) {
        for (rowIndex in 0 until rowCount()) {
            if (cell(TableCell.row(rowIndex).column(0)).value() == board.name &&
                    (cell(TableCell.row(rowIndex).column(1)).value() == board.platform) &&
                    cell(TableCell.row(rowIndex).column(2)).value() == board.framework) {
                return
            }
        }
        fail("Cannot find row for board $board")
    }

    private fun JTableFixture.boardDoesNotExist(board: Board) {
        for (rowIndex in 0 until rowCount()) {
            if (cell(TableCell.row(rowIndex).column(0)).value() == board.name &&
                    (cell(TableCell.row(rowIndex).column(1)).value() == board.platform) &&
                    cell(TableCell.row(rowIndex).column(2)).value() == board.framework) {
                fail("Board exists $board")
            }
        }
    }

    private fun DialogFixture.clickOnBoards(board: Board) {
        table(BoardsTableModel.BOARD_TABLE_NAME).cell(TableCell.row(boards.indexOf(board)).column(0)).click()
    }
}
