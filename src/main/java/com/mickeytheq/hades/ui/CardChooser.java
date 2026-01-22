package com.mickeytheq.hades.ui;

import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.util.MemberUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardChooser extends JPanel {
    private final JTextField textField;

    private final JCardChooser cardChooser;

    public CardChooser(Path startingPath) {
        cardChooser = new JCardChooser();
        cardChooser.setPreferredSize(new Dimension(800, 600));
        cardChooser.setCurrentDirectory(startingPath.toFile());

        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // creates the GUI
        textField = new JTextField(30);
        textField.setEnabled(false);
        JButton button = new JButton("...");
        button.setPreferredSize(new Dimension(30, 20));

        button.addActionListener(this::buttonActionPerformed);

        add(textField);
        add(button);
    }

    public Card getSelectedCard() {
        return cardChooser.getSelectedCard();
    }

    public void setSelectedCard(Card card) {
        textField.setText(card.getId());
    }

    private void buttonActionPerformed(ActionEvent evt) {
        if (cardChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textField.setText(cardChooser.getSelectedCard().getId());

            fireActionListeners("selected-file-changed");
        }
    }

    private final List<ActionListener> actionListeners = new ArrayList<>();

    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    private void fireActionListeners(String command) {
        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);

        for (ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(actionEvent);
        }
    }

    private static class JCardChooser extends JFileChooser {
        private Card selectedCard;

        @Override
        protected void setup(FileSystemView view) {
            super.setup(view);

            setFileSelectionMode(FILES_ONLY);
            setFileFilter(new FileNameExtensionFilter("Hades card files (*.hades)", CardIO.HADES_FILE_EXTENSION));
        }

        @Override
        public void approveSelection() {
            Path selectedPath = getSelectedFile().toPath();

            if (!MemberUtils.isPathHadesFile(selectedPath)) {
                JOptionPane.showMessageDialog(this, "Selected file is not a Hades file (e.g. does not have .hades extension", "Invalid selection", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Optional<ProjectContext> projectContextOpt = StandardProjectContext.findContextForContentPath(getSelectedFile().toPath());

            if (!projectContextOpt.isPresent()) {
                JOptionPane.showMessageDialog(this, "Selected file is not in a Hades project (e.g. is not within an Strange Eons project with a .hades folder in the root)", "Invalid selection", JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedCard = CardIO.readCard(selectedPath, projectContextOpt.get());

            super.approveSelection();
        }

        public Card getSelectedCard() {
            return selectedCard;
        }
    }
}
