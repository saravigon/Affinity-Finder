package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Presentation class for modifying a question in a form.
 * Shows all questions and allows user to select which one to modify.
 * Only the question text can be modified.
 */
public class ModifyQuestionView extends JFrame {

    /**
     * PresentationController reference to presentation controller
     */
    private PresentationController controller;

    /**
     * UFID of the form managed
     */
    private int ufid;

    /**
     * Selected question index
     */
    private Integer selectedQuestionIndex = null;

    /**
     * Text field for new question text
     */
    private JTextField fieldQuestionText;

    /**
     * Back button
     * It allows to go back to the previous view
     */
    private JButton btnBack;
    /**
     * Modify button
     * It allows to modify the selected question
     */
    private JButton btnModify;

    /**
     * Constructor to initialize the Modify Question View.
     * @param controller PresentationController reference to presentation controller
     * @param ufid UFID of the form managed
     */
    public ModifyQuestionView(PresentationController controller, int ufid) {
        this.controller = controller;
        this.ufid = ufid;
        initComponents();
    }

    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Modify Question", 700, 650);

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
        btnBack.addActionListener(e -> {
            new ManageForm(controller, ufid).setVisible(true);
            dispose();
        });
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
        JLabel titleLabel = new JLabel("Modify Question Text");
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

        // selector for questions
        JLabel step1Label = new JLabel("Step 1: Select Question to Modify");
        step1Label.setFont(UIComponents.STANDARD_FONT);
        step1Label.setForeground(Color.DARK_GRAY);
        step1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(step1Label);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        try {
            List<String> questions = controller.getController().getForm(ufid).getQuestionsNoType();
            
            if (questions.isEmpty()) {
                JLabel noQuestionsLabel = new JLabel("No questions available to modify");
                noQuestionsLabel.setFont(UIComponents.SMALL_FONT);
                noQuestionsLabel.setForeground(Color.GRAY);
                noQuestionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                centerPanel.add(noQuestionsLabel);
            } else {
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
                scrollPane.setMaximumSize(new Dimension(580, 250));
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

        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // modify question text panel
        JLabel step2Label = new JLabel("Step 2: Enter New Question Text");
        step2Label.setFont(UIComponents.STANDARD_FONT);
        step2Label.setForeground(Color.DARK_GRAY);
        step2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(step2Label);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Form panel for input
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(248, 249, 250));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        formPanel.setMaximumSize(new Dimension(550, 100));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Question Text
        JLabel lblQuestionText = new JLabel("New Question Text:");
        lblQuestionText.setFont(UIComponents.TEXT_FONT);
        lblQuestionText.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblQuestionText);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        fieldQuestionText = new JTextField();
        fieldQuestionText.setFont(UIComponents.TEXT_FONT);
        fieldQuestionText.setMaximumSize(new Dimension(500, 30));
        fieldQuestionText.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(fieldQuestionText);

        centerPanel.add(formPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // modify button
        btnModify = UIComponents.createStyledButton("Modify Question", 300, 40);
        btnModify.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnModify.addActionListener(e -> handleModifyQuestion());
        centerPanel.add(btnModify);

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
        selectedLabel.setForeground(UIComponents.DARK_GREEN);
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
                box.setBackground(new Color(240, 255, 240)); // Light green
                box.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(UIComponents.DARK_GREEN, 2, true),
                    new EmptyBorder(11, 19, 11, 19)
                ));
                selectedLabel.setVisible(true);
                selectedQuestionIndex = index;

                // Load current question data into field
                loadQuestionData(index);
            }
        });

        return box;
    }

    /**
     * Loads the selected question text into the form field
     * @param index Question index
     */
    private void loadQuestionData(int index) {
        try {
            // Get current question text
            List<String> questions = controller.getController().getForm(ufid).getQuestionsNoType();
            if (index < questions.size()) {
                fieldQuestionText.setText(questions.get(index));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading question data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles modify question action
     */
    private void handleModifyQuestion() {
        if (selectedQuestionIndex == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a question to modify.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String questionText = fieldQuestionText.getText().trim();

        if (questionText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter question text.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Only modify the question text
            controller.getController().modifyQuestion(ufid, selectedQuestionIndex, questionText);

            JOptionPane.showMessageDialog(this,
                "Question text modified successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            new ManageForm(controller, ufid).setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error modifying question: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}