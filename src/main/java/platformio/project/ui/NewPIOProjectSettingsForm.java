package platformio.project.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.intellij.ui.components.JBScrollPane;
import platformio.services.Board;
import platformio.services.PlatformIOService;

import org.jetbrains.annotations.NotNull;

public class NewPIOProjectSettingsForm {
    public static final String SELECT_BOARD_BUTTON_NAME = "selectBoardButton";
    public static final String ERROR_MESSAGE_LABEL = "errorMessageLabel";
    public static final String ERROR_MESSAGE = "Please install PlatformIO CLI";
    public static final String SELECTED_BOARD_TABLE_NAME = "selectedBoardTable";
    public static final String NAME_COLUMN = "Name";
    public static final String PLATFORM_COLUMN = "Platform";
    public static final String FRAMEWORK_COLUMN = "Framework";
    private final PlatformIOService platformIOService;
    private JPanel mainPanel;
    private JButton selectBoardButton;
    private JScrollPane selectedBoardsScrollPane;
    private JLabel errorLabelMessage;
    private DefaultTableModel selectedBoardsTableModel;
    private Set<Board> selectedBoards = new HashSet<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NewPIOProjectSettingsForm(PlatformIOService platformIOService) {
        this.platformIOService = platformIOService;
        selectBoardButton.addActionListener(e -> {

            final List<Board> boards = platformIOService.loadAllBoards();
            BoardCatalogDialog boardCatalogDialog = new BoardCatalogDialog(boards, selectedBoards);
            boardCatalogDialog.setVisible(true);

            List<Board> selectedBoards = boardCatalogDialog.getSelectedBoards();
            this.selectedBoards = new HashSet<>(selectedBoards);
            clearTable();
            selectedBoards.forEach(board -> selectedBoardsTableModel.addRow(
                    new Object[] {
                            board.getName(),
                            board.getPlatform(),
                            board.getFramework()
                    }));
        });

        executorService.execute(() -> {
            if (!platformIOService.isAvailable()) {
                selectBoardButton.setEnabled(false);
                errorLabelMessage.setText(ERROR_MESSAGE);
                errorLabelMessage.setName(ERROR_MESSAGE_LABEL);
            }
        });
    }

    private void clearTable() {
        int rows = selectedBoardsTableModel.getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            selectedBoardsTableModel.removeRow(i);
        }
    }

    public Collection<Board> getBoards() {
        return selectedBoards;
    }

    @NotNull
    public JComponent getComponent() {
        return mainPanel;
    }

    private void createUIComponents() {
        selectBoardButton = new JButton();
        selectBoardButton.setName(SELECT_BOARD_BUTTON_NAME);

        selectedBoardsTableModel = new DefaultTableModel();

        addColumns();

        JTable selectedBoardTable = new JTable(selectedBoardsTableModel);
        selectedBoardTable.setName(SELECTED_BOARD_TABLE_NAME);
        selectedBoardsScrollPane = new JBScrollPane(selectedBoardTable);
    }

    private void addColumns() {
        selectedBoardsTableModel.addColumn(NAME_COLUMN);
        selectedBoardsTableModel.addColumn(PLATFORM_COLUMN);
        selectedBoardsTableModel.addColumn(FRAMEWORK_COLUMN);
    }
}
