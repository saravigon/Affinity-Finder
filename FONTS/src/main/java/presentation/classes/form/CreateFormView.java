package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Presentation class for creating a new form (Title and Description).
 * Modern UI with gradient background and styled components.
 */
public class CreateFormView extends JFrame {
    /**
     * Attribute: reference to PresentationController
     */
    private PresentationController controller;

    // Componentes del Formulario
    /**
     * Attribute: title input field
     */
    private JTextField titleField;
    /**
     * Attribute: description text area
     */
    private JTextArea descArea;
    /**
     * Attribute: next button
     */
    private JButton btnNext;
    /**
     * Attribute: back button
     */
    private JButton btnBack;
    /**
     * Attribute: import button to import from JSON a form
     */
    private JButton btnImport;

    /**
     * Constructor
     * @param c PresentationController reference
     */
    public CreateFormView(PresentationController c) {
        this.controller = c;
        initComponents();
    }

    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "CREATE NEW FORM", 700, 700);

        // Main panel with gradient background
        JPanel mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new FormMenuView(controller).setVisible(true);
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Title
        JLabel titleLabel = new JLabel("Create New Form");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Step 1: Enter form details");
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Content card
        JPanel contentCard = createContentCard();
        mainPanel.add(contentCard);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        mainPanel.add(buttonsPanel);
        
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates the main content card with form fields
     */
    private JPanel createContentCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(550, 300));

        // Title field
        JLabel lblTitle = new JLabel("Form Title *");
        lblTitle.setFont(UIComponents.STANDARD_FONT);
        lblTitle.setForeground(Color.DARK_GRAY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        titleField = new JTextField();
        titleField.setFont(UIComponents.TEXT_FONT);
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        card.add(titleField);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Description field
        JLabel lblDesc = new JLabel("Description");
        lblDesc.setFont(UIComponents.STANDARD_FONT);
        lblDesc.setForeground(Color.DARK_GRAY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblDesc);
        
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        descArea = new JTextArea();
        descArea.setFont(UIComponents.TEXT_FONT);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(new EmptyBorder(8, 10, 8, 10));
        
        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(490, 100));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        card.add(scrollPane);

        return card;
    }

    /**
     * Creates the buttons panel (Import and Next)
     */
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(550, 50));

        // Import button
        btnImport = new JButton("Import from JSON");
        btnImport.setFont(UIComponents.STANDARD_FONT);
        btnImport.setPreferredSize(new Dimension(200, 45));
        btnImport.setBackground(new Color(108, 117, 125)); // Gray color
        btnImport.setForeground(Color.WHITE);
        btnImport.setFocusPainted(false);
        btnImport.setBorderPainted(false);
        btnImport.setOpaque(true);
        btnImport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImport.addActionListener(e -> {
            Runnable goBackToCreate = () -> new CreateFormView(controller).setVisible(true);
            new ImportFormView(controller, goBackToCreate).setVisible(true);
            dispose();
        });
        
        // Hover effect for import button
        btnImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnImport.setBackground(new Color(90, 98, 104));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnImport.setBackground(new Color(108, 117, 125));
            }
        });
        
        panel.add(btnImport);

        // Next button
        btnNext = UIComponents.createStyledButton("Next →", 200, 45);
        btnNext.addActionListener(e -> handleNextStep());
        panel.add(btnNext);

        return panel;
    }

    /**
     * Validación y paso a la siguiente pantalla
     */
    private void handleNextStep() {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();

        // Validation
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a title for your form.",
                "Missing Title",
                JOptionPane.WARNING_MESSAGE);
            titleField.requestFocus();
            return;
        }

        try {
            controller.getController().createNewForm(title, desc);
            
            JOptionPane.showMessageDialog(this,
                "Form created successfully!\nNow add some questions.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            int id = controller.getController().getFormbyTitle(title).getUFID();

            // Definimos qué pasa si vuelven atrás: Reabrir CreateFormView
            Runnable goBackToCreate = () -> {
                new CreateFormView(controller).setVisible(true);
            };

            new AddQuestionsView(controller, id, goBackToCreate).setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error creating form:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}