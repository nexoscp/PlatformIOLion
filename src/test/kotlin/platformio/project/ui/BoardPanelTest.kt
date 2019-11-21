package platformio.project.ui

import org.assertj.swing.edt.GuiActionRunner
import org.assertj.swing.fixture.FrameFixture
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.JFrame

@RunWith(MockitoJUnitRunner::class)
class BoardPanelTest {
    private val frame = JFrame()
    private val window = FrameFixture(GuiActionRunner.execute<JFrame> { frame })

    @Mock
    private lateinit var boardPanelRemovalListener: BoardPanel.BoardPanelRemovalListener

    private lateinit var boardPanel: BoardPanel

    @Before
    fun setUp() {
        boardPanel = BoardPanel(boardA, boardPanelRemovalListener)
        frame.add(boardPanel)
        window.show()
    }

    @After
    fun tearDown() {
        window.cleanUp()
    }

    @Test
    fun boardPanelDisplaysData() {
        window.label(BoardPanel.NAME_LABEL + boardA.id).requireText(boardA.name)
        window.label(BoardPanel.NAME_LABEL + boardA.id).requireText(boardA.name)
        window.button(BoardPanel.REMOVE_BUTTON + boardA.id)
        window.label(BoardPanel.FRAMEWORKS_LABLE_NAME + boardA.id).requireText(boardA.framework)
    }

    @Test
    fun removeButtonClicked() {
        window.button(BoardPanel.REMOVE_BUTTON + boardA.id).click()
        verify(boardPanelRemovalListener).onRemove(boardA)
    }
}