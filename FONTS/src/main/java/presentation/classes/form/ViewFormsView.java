package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Presentation class for viewing form details.
 * Displays form title, description, creator, and questions in a beautiful card-based layout.
 */
public class ViewFormsView extends JFrame {

    /**
     * PresentationController reference
     */
    private PresentationController controller;
    /**
     * ID of the form to view
     */
    private int formId;
    /**
     * Main panel containing all components
     */
    private JPanel mainPanel;
    /**
     * Header card panel with form title and description
     */
    private JPanel headerCard;
    /**
     * Content panel displaying questions
     */
    private JPanel contentPanel;
    /**
     * Back button to return to previous view
     */
    private JButton btnBack;
    /**
     * Export button to export the form as JSON
     */
    private JButton btnExport;


    /**
     * Constructor
     * @param controller PresentationController reference
     * @param formId ID of the form to view
     */
    public ViewFormsView(PresentationController controller, int formId) {
        this.controller = controller;
        this.formId = formId;
        initComponents();
    }

    /**
     * Initializes all components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "VIEW FORM", 750, 700);

        // Main panel with gradient background
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new SelectFormView(controller, SelectFormView.FormType.EXISTING, SelectFormView.FormAction.VIEW_FORM, () -> {
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

        // Content panel (scrollable)
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        loadFormDetails();

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(670, 450));
        scrollPane.setMaximumSize(new Dimension(670, 450));
        
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Export button
        btnExport = UIComponents.createStyledButton("Export Form", 200, 40);
        btnExport.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExport.addActionListener(e -> {
            try {
                // Open file chooser
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Export Form as JSON");
                fileChooser.setSelectedFile(new java.io.File(controller.getController().getForm(formId).getTitle() + ".json"));
                
                // Set file filter to JSON
                javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("JSON files (*.json)", "json");
                fileChooser.setFileFilter(filter);
                
                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    // Ensure .json extension
                    if (!filePath.endsWith(".json")) {
                        filePath += ".json";
                    }
                    controller.getController().exportForm(formId, filePath);
                    JOptionPane.showMessageDialog(this, "Form exported successfully to:\n" + filePath, "Export", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting form: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(btnExport);
        
        // Set scroll to top
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));

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
     * Loads and displays form details (creator + questions)
     */
    private void loadFormDetails() {
        try {
            int creatorId = controller.getController().getForm(formId).getCreatorUUID();
            String creatorName = controller.getController().getProfileByID(creatorId).getUsername();
            List<String> questions = controller.getController().getForm(formId).getQuestionsNoType();

            // Creator section
            JLabel creatorLabel = new JLabel("Created by: " + creatorName);
            creatorLabel.setFont(UIComponents.STANDARD_FONT);
            creatorLabel.setForeground(UIComponents.DARK_GREEN);
            creatorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(creatorLabel);
            
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            // Divider
            JSeparator separator = new JSeparator();
            separator.setForeground(new Color(200, 200, 200));
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            separator.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(separator);
            
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            // Questions section
            JLabel questionsTitle = new JLabel("Questions (" + questions.size() + ")");
            questionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            questionsTitle.setForeground(Color.DARK_GRAY);
            questionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(questionsTitle);
            
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            if (questions.isEmpty()) {
                JLabel emptyLabel = new JLabel("This form has no questions yet.");
                emptyLabel.setFont(UIComponents.ITALIC_FONT);
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(emptyLabel);
            } else {
                for (int i = 0; i < questions.size(); i++) {
                    JPanel questionCard = createQuestionCard(i + 1, questions.get(i));
                    contentPanel.add(questionCard);
                    contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
                }
            }

            contentPanel.add(Box.createVerticalGlue());

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading form details: " + e.getMessage());
            errorLabel.setFont(UIComponents.TEXT_FONT);
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(errorLabel);
        }
    }

    /**
     * Creates a question card
     */
    private JPanel createQuestionCard(int number, String questionText) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(new Color(248, 249, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Question number
        JLabel numberLabel = new JLabel("Q" + number);
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        numberLabel.setForeground(UIComponents.DARK_GREEN);
        numberLabel.setVerticalAlignment(SwingConstants.TOP);
        card.add(numberLabel, BorderLayout.WEST);

        // Question text
        JTextArea textArea = new JTextArea(questionText);
        textArea.setFont(UIComponents.TEXT_FONT);
        textArea.setForeground(Color.DARK_GRAY);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(new Color(248, 249, 250));
        textArea.setBorder(null);
        card.add(textArea, BorderLayout.CENTER);

        return card;
    }
}