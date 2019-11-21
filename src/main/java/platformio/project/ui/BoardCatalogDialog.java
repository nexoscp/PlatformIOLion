package platformio.project.ui;

import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import platformio.services.Board;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BoardCatalogDialog extends JDialog {
    public static final String BOARD_CATALOG_DIALOG_NAME = "boardCatalogDialog";
    public static final String SEARCH_BOARD_TEXTFIELD_NAME = "searchBoardTextfield";
    public static final String OK_BUTTON_NAME = "okButton";
    public static final String CANCEL_BUTTON_NAME = "cancelButton";

    private BoardTable boardTable;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField searchBoardTextField;
    private JScrollPane boardTableScrollPane;
    private List<Board> boards;
    private Set<Board> previouslySelectedBoards;

    public BoardCatalogDialog(List<Board> boards, Set<Board> previouslySelectedBoards) {
        this.boards = boards;
        this.previouslySelectedBoards = previouslySelectedBoards;

        setContentPane(contentPane);
        setModal(true);
        setName(BOARD_CATALOG_DIALOG_NAME);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.setName(OK_BUTTON_NAME);
        buttonCancel.setName(CANCEL_BUTTON_NAME);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        boardTable.clearNewlySelectedBoards();
        dispose();
    }

    private void createUIComponents() {
        searchBoardTextField = new JTextField();
        searchBoardTextField.setName(SEARCH_BOARD_TEXTFIELD_NAME);
        searchBoardTextField.getDocument().addDocumentListener(new SearchBoardDocumentListener());
        this.boardTable = new BoardTable(boards, previouslySelectedBoards);
        boardTableScrollPane = new JBScrollPane(boardTable.getTable());
        boardTableScrollPane.setPreferredSize(new Dimension(950, 400));

    }

    @NotNull
    public List<Board> getSelectedBoards() {
        return new ArrayList<>(boardTable.getSelectedBoards());
    }

    private class SearchBoardDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent arg0) {
        }

        @Override
        public void insertUpdate(DocumentEvent arg0) {
            boardTable.filterTable(searchBoardTextField.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent arg0) {
            boardTable.filterTable(searchBoardTextField.getText());
        }
    }
}
