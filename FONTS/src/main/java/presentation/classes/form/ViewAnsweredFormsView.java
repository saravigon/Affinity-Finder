package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;
import domain.classes.Answer;
import domain.classes.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.util.List;

/**
 * Presentation class for viewing answered forms with user's responses.
 * Displays form title, description, questions with answers, and options to edit or delete answers.
 */
public class ViewAnsweredFormsView extends JFrame {
    /**
     * Reference to PresentationController
     */
    private PresentationController controller;
    /**
     * ID of the form to view
     */
    private int formId;
    /**
     * ID of the user viewing the form
     */
    private int userId;

    /**
     * Main panel containing all components
     */
    private JPanel mainPanel;
    /**
     * Header card panel with form title and description
     */
    private JPanel headerCard;
    /**
     * Content panel displaying questions and answers
     */
    private JPanel contentPanel;

    /**
     * Back button to return to previous view
     */
    private JButton btnBack;
    /**
     * List of user's answers
     */
    private List<String> userAnswers;

    /**
     * Constructor for ViewAnsweredFormsView
     * @param controller PresentationController reference
     * @param formId ID of the form to view
     */
    public ViewAnsweredFormsView(PresentationController controller, int formId) {
        this.controller = controller;
        this.formId = formId;
        this.userId = controller.getController().getCurrentProfile().getUUID();
        initComponents();
    }

    /**
     * Initializes all components of the view
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "VIEW ANSWERED FORM", 750, 700);

        // Main panel with gradient background
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Top panel with back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new SelectFormView(controller, SelectFormView.FormType.ANSWERED, SelectFormView.FormAction.VIEW_ANSWERED, () -> {
                new FormMenuView(controller).setVisible(true);
            }).setVisible(true);
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Header card (title + description)
        headerCard = createHeaderCard();
        mainPanel.add(headerCard);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action buttons panel
        JPanel actionsPanel = createActionsPanel();
        mainPanel.add(actionsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Content panel (scrollable)
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        loadAnsweredForm();

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(670, 400));
        scrollPane.setMaximumSize(new Dimension(670, 400));
        
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates the header card with form title and description
     */
    private JPanel createHeaderCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 252, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 2, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(670, 150));

        try {
            String title = controller.getController().getForm(formId).getTitle();
            String description = controller.getController().getForm(formId).getDescription();

            // Title
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(UIComponents.DARK_GREEN);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(titleLabel);
            
            card.add(Box.createRigidArea(new Dimension(0, 10)));

            // Description
            JTextArea descArea = new JTextArea(description.isEmpty() ? "No description provided" : description);
            descArea.setFont(UIComponents.ITALIC_FONT);
            descArea.setForeground(Color.DARK_GRAY);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setEditable(false);
            descArea.setOpaque(false);
            descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
            descArea.setMaximumSize(new Dimension(620, 80));
            card.add(descArea);

            card.add(Box.createRigidArea(new Dimension(0, 10)));

            // "Your Answers" label
            JLabel answersLabel = new JLabel("Your Answers");
            answersLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 14));
            answersLabel.setForeground(new Color(76, 175, 80));
            answersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(answersLabel);

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading form information");
            errorLabel.setFont(UIComponents.TEXT_FONT);
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(errorLabel);
        }

        return card;
    }

    /**
     * Creates the actions panel with Edit and Delete buttons
     */
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(670, 50));

        // Edit button
        JButton btnEdit = UIComponents.createStyledButton("Edit Answers", 200, 40);
        btnEdit.addActionListener(e -> handleEditAnswers());
        panel.add(btnEdit);

        // Delete button
        JButton btnDelete = new JButton("Delete Answers");
        btnDelete.setFont(UIComponents.STANDARD_FONT);
        btnDelete.setPreferredSize(new Dimension(200, 40));
        btnDelete.setMinimumSize(new Dimension(200, 40));
        btnDelete.setMaximumSize(new Dimension(200, 40));
        btnDelete.setBackground(new Color(220, 53, 69)); // Red color
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setOpaque(true); // Important for Mac
        btnDelete.setBorderPainted(true); // Changed to true for Mac compatibility
        btnDelete.setBorder(BorderFactory.createLineBorder(new Color(200, 35, 51), 1, true));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> handleDeleteAnswers());
        
        // Hover effect for delete button
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDelete.setBackground(new Color(200, 35, 51));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDelete.setBackground(new Color(220, 53, 69));
            }
        });
        
        panel.add(btnDelete);

        return panel;
    }

    /**
     * Loads and displays the answered form with user's responses
     */
    private void loadAnsweredForm() {
        try {
            // Get user's answers
            Answer aux = controller.getController().getUserAnswers(formId, userId);
            
            if (aux == null) return; // Seguridad por si no hay respuestas

            userAnswers = UIComponents.answerToStringList(aux);
            
            // Get form questions
            List<Question> questions = controller.getController().getForm(formId).getQuestions();

            // Questions section title
            JLabel questionsTitle = new JLabel("Questions & Answers (" + questions.size() + ")");
            questionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            questionsTitle.setForeground(Color.DARK_GRAY);
            questionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(questionsTitle);
            
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            if (questions.isEmpty()) {
                JLabel emptyLabel = new JLabel("This form has no questions.");
                emptyLabel.setFont(UIComponents.ITALIC_FONT);
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(emptyLabel);
            } else {
                for (int i = 0; i < questions.size(); i++) {
                    String answer = i < userAnswers.size() ? userAnswers.get(i) : "No answer provided";
                    JPanel questionCard = createQuestionAnswerCard(i + 1, questions.get(i), answer);
                    contentPanel.add(questionCard);
                    contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
                }
            }

            contentPanel.add(Box.createVerticalGlue());

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading your answers: " + e.getMessage());
            errorLabel.setFont(UIComponents.TEXT_FONT);
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(errorLabel);
        }
    }

    /**
     * Creates a question-answer card
     */
    private JPanel createQuestionAnswerCard(int number, Question question, String answer) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(248, 249, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Question header (number + question text)
        JPanel questionHeader = new JPanel(new BorderLayout(15, 0));
        questionHeader.setOpaque(false);
        questionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel numberLabel = new JLabel("Q" + number);
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        numberLabel.setForeground(UIComponents.DARK_GREEN);
        questionHeader.add(numberLabel, BorderLayout.WEST);

        JTextArea questionText = new JTextArea(question.getQuestionText());
        questionText.setFont(UIComponents.TEXT_FONT);
        questionText.setForeground(Color.DARK_GRAY);
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.setEditable(false);
        questionText.setBackground(new Color(248, 249, 250));
        questionText.setBorder(null);
        questionHeader.add(questionText, BorderLayout.CENTER);

        card.add(questionHeader);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Answer section
        JPanel answerPanel = new JPanel(new BorderLayout(10, 0));
        answerPanel.setOpaque(false);
        answerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel answerLabel = new JLabel("Your answer:");
        answerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        answerLabel.setForeground(new Color(76, 175, 80));
        answerPanel.add(answerLabel, BorderLayout.WEST);

        JTextArea answerText = new JTextArea(answer);
        answerText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        answerText.setForeground(new Color(33, 37, 41));
        answerText.setLineWrap(true);
        answerText.setWrapStyleWord(true);
        answerText.setEditable(false);
        answerText.setBackground(Color.WHITE);
        answerText.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        answerPanel.add(answerText, BorderLayout.CENTER);

        card.add(answerPanel);

        return card;
    }

    /**
     * Handles the edit answers action
     */
    private void handleEditAnswers() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Do you want to edit your answers for this form?",
            "Edit Answers",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Open AnswerFormView in edit mode
                Runnable goBack = () -> {
                    new ViewAnsweredFormsView(controller, formId).setVisible(true);
                };
                new AnswerFormView(controller, formId, true, goBack).setVisible(true);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error opening edit view: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles the delete answers action
     */
    private void handleDeleteAnswers() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete all your answers for this form?\nThis action cannot be undone.",
            "Delete Answers",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete user's answers
                controller.getController().deleteAnsweredForm(formId);
                
                JOptionPane.showMessageDialog(this,
                    "Your answers have been deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                new FormMenuView(controller).setVisible(true);
                dispose();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting answers: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}