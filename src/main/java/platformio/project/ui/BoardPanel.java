package platformio.project.ui;

import com.intellij.icons.AllIcons;
import platformio.services.Board;

import javax.swing.*;
import java.util.List;

public class BoardPanel extends JPanel {
    public static final String NAME_LABEL = "nameLabel_";
    public static final String PLATFORM_LABEL = "platformLabel_";
    public static final String REMOVE_BUTTON = "removeButton_";
    public static final String FRAMEWORKS_LABLE_NAME = "framework_";
    private final Board board;
    private final BoardPanelRemovalListener boardPanelRemovalListener;
    private JLabel nameLabel;
    private JLabel platfromLabel;
    private JPanel mainPanel;
    private JButton removeButton;
    private JLabel frameworkLabel;

    public BoardPanel(Board board, BoardPanelRemovalListener boardPanelRemovalListener) {
        this.board = board;
        this.boardPanelRemovalListener = boardPanelRemovalListener;

        nameLabel.setName(NAME_LABEL + board.getId());
        nameLabel.setText(board.getName());
        platfromLabel.setName(PLATFORM_LABEL + board.getId());
        platfromLabel.setText(board.getPlatform());

        frameworkLabel.setName(FRAMEWORKS_LABLE_NAME + board.getId());
        frameworkLabel.setText(board.getFramework());
        add(mainPanel);
    }

    private void createUIComponents() {
        removeButton = new JButton("");
        removeButton.setName(REMOVE_BUTTON + board.getId());
        removeButton.setIcon(AllIcons.Actions.Cancel);
        removeButton.addActionListener(e -> boardPanelRemovalListener.onRemove(board));
    }

    public interface BoardPanelRemovalListener {
        void onRemove(Board board);
    }
}
