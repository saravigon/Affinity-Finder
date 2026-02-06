package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Presentation class for deleting a question from a form.
 * Shows all questions and allows user to select which one to delete.
 */
public class DeleteQuestionView extends JFrame {

    /**
     * PresentationController reference to presentation controller
    */
    private PresentationController controller;

    /**
     * UFID of the form managed
     * Used to fetch questions and perform deletion
     */
    private int ufid;

    /**
     * Selected question index
     * Null if no question is selected
     */
    private Integer selectedQuestionIndex = null;

    /**
     * Back button
     * It allows to go back to the previous view
     */
    private JButton btnBack;

    /**
     * Delete button
     * It allows to delete the selected question
     */
    private JButton btnDelete;

    /**
     * Constructor to initialize the Delete Question View.
     * @param controller PresentationController reference to presentation controller
     * @param ufid UFID of the form managed
     */
    public DeleteQuestionView(PresentationController controller, int ufid) {
        this.controller = controller;
        this.ufid = ufid;
        initComponents();
    }

    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Delete Question", 700, 600);

        // ================================
        // MAIN PANEL
        // ================================
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 40, 40));
        mainPanel.setBackground(Color.WHITE);

        // ================================
        // TOP PANEL (Back Button)
        // ================================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> dispose());
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ================================
        // CENTER PANEL
        // ================================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // ────────────────────────────────
        // TITLE
        // ────────────────────────────────
        JLabel titleLabel = new JLabel("Select Question to Delete");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // ────────────────────────────────
        // FORM INFO
        // ────────────────────────────────
        String formTitle;
        try {
            formTitle = controller.getController().getForm(ufid).getTitle();
        } catch (Exception e) {
            formTitle = "N/A";
        }

        JLabel formInfoLabel = new JLabel("Form: " + formTitle);
        formInfoLabel.setFont(UIComponents.STANDARD_FONT);
        formInfoLabel.setForeground(Color.DARK_GRAY);
        formInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(formInfoLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ────────────────────────────────
        // QUESTIONS LIST
        // ────────────────────────────────
        try {
            List<String> questions = controller.getController().getForm(ufid).getQuestionsText();
            
            if (questions.isEmpty()) {
                JLabel noQuestionsLabel = new JLabel("No questions available to delete");
                noQuestionsLabel.setFont(UIComponents.SMALL_FONT);
                noQuestionsLabel.setForeground(Color.GRAY);
                noQuestionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                centerPanel.add(noQuestionsLabel);
            } else {
                JLabel instructionLabel = new JLabel("Questions (" + questions.size() + ")");
                instructionLabel.setFont(UIComponents.STANDARD_FONT);
                instructionLabel.setForeground(Color.DARK_GRAY);
                instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                centerPanel.add(instructionLabel);
                
                centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                // Questions Panel (Scrollable)
                JPanel questionsPanel = new JPanel();
                questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
                questionsPanel.setBackground(Color.WHITE);

                for (int i = 0; i < questions.size(); i++) {
                    String questionText = questions.get(i);
                    int questionIndex = i;
                    
                    JPanel questionBox = createQuestionBox(questionIndex, questionText);
                    questionsPanel.add(questionBox);
                    questionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                }

                JScrollPane scrollPane = new JScrollPane(questionsPanel);
                scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
                scrollPane.setMaximumSize(new Dimension(580, 300));
                scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
                scrollPane.setBackground(Color.WHITE);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                centerPanel.add(scrollPane);
            }

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading questions: " + e.getMessage());
            errorLabel.setFont(UIComponents.SMALL_FONT);
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(errorLabel);
        }

        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ────────────────────────────────
        // DELETE BUTTON (CENTERED)
        // ────────────────────────────────
        btnDelete = UIComponents.createStyledButton("Delete Selected Question", 300, 40);
        btnDelete.setForeground(new Color(220, 53, 69)); // Red color for danger
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.addActionListener(e -> handleDeleteQuestion());
        centerPanel.add(btnDelete);

        mainPanel.add(centerPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates a clickable question box
     * @param index Question index
     * @param questionText Question text
     * @return Clickable JPanel
     */
    private JPanel createQuestionBox(int index, String questionText) {
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout());
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 1, true),
            new EmptyBorder(12, 20, 12, 20)
        ));
        box.setMaximumSize(new Dimension(550, 60));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Question number (left)
        JLabel numberLabel = new JLabel("Q" + (index + 1));
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        numberLabel.setForeground(UIComponents.DARK_GREEN);
        box.add(numberLabel, BorderLayout.WEST);

        // Question text (center)
        String displayText = questionText.length() > 60 ? 
            questionText.substring(0, 57) + "..." : questionText;
        JLabel textLabel = new JLabel(displayText);
        textLabel.setFont(UIComponents.TEXT_FONT);
        textLabel.setForeground(Color.DARK_GRAY);
        textLabel.setBorder(new EmptyBorder(0, 15, 0, 0));
        box.add(textLabel, BorderLayout.CENTER);

        // Selection indicator (right, initially invisible)
        JLabel selectedLabel = new JLabel("✓ Selected");
        selectedLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        selectedLabel.setForeground(new Color(220, 53, 69));
        selectedLabel.setVisible(false);
        box.add(selectedLabel, BorderLayout.EAST);

        // Click listener
        box.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean isSelected = false;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!isSelected) {
                    box.setBackground(new Color(245, 250, 245));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!isSelected) {
                    box.setBackground(Color.WHITE);
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Deselect all other boxes
                Container parent = box.getParent();
                for (Component comp : parent.getComponents()) {
                    if (comp instanceof JPanel && comp != box) {
                        comp.setBackground(Color.WHITE);
                        ((JPanel) comp).setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(UIComponents.DARK_GREEN, 1, true),
                            new EmptyBorder(12, 20, 12, 20)
                        ));
                        // Hide selected label
                        for (Component inner : ((JPanel) comp).getComponents()) {
                            if (inner instanceof JLabel && ((JLabel) inner).getText().contains("Selected")) {
                                inner.setVisible(false);
                            }
                        }
                    }
                }

                // Select this box
                isSelected = true;
                box.setBackground(new Color(255, 235, 235)); // Light red
                box.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(220, 53, 69), 2, true),
                    new EmptyBorder(11, 19, 11, 19)
                ));
                selectedLabel.setVisible(true);
                selectedQuestionIndex = index;
            }
        });

        return box;
    }

    /**
     * Handles delete question action
     */
    private void handleDeleteQuestion() {
        if (selectedQuestionIndex == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a question to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete question " + (selectedQuestionIndex + 1) + "?\nThis action cannot be undone.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.getController().deleteQuestion(ufid, selectedQuestionIndex);
                JOptionPane.showMessageDialog(this,
                    "Question deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting question: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}