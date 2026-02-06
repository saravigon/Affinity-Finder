package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FormMenuView extends JFrame{

    /**
     * Presentation controller reference.
     */
    private PresentationController controller;
    /**
     * Main panel
     */
    private JPanel mainPanel;
    /**
     * Panel for the header (Back button)
     */
    private JPanel topPanel;  
    /**
     * Center panel (Info + Modify/Delete buttons) 
     */  
    private JPanel buttonsPanel; 
    /**
     * Back button
     * To go back to the main menu
    */
    private JButton btnBack;
    /**
     * Button to create a new form
     */
    private JButton btnCreate;

    /**
     * Button to select an existing form to manage
     */
    private JButton btnManage;
    
    /**
     * Button to view existing forms
     */
    private JButton btnViewF;
    /**
     * Button to view existing forms
     */
    private JButton btnViewAF;

    /**
     * Constructor
     * @param controller PresentationController reference
     */
    public FormMenuView(PresentationController controller) {
        this.controller = controller;
        initComponents();
    }
    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "FORMS MANAGEMENT", 700, 700);

        // --------------------------------
        // 1. MAIN PANEL WITH GRADIENT
        // --------------------------------

        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // --------------------------------
        // 2. HEADER (Back Button)
        // --------------------------------

        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new MainMenuView(controller).setVisible(true);
            dispose();
        });
        
        topPanel.add(btnBack);
        mainPanel.add(topPanel);

        // --------------------------------
        // 3. TITLE SECTION
        // --------------------------------

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(10, 50, 20, 50));

        JLabel titleLabel = new JLabel("Forms Management");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Create, manage, and view forms");
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titlePanel.add(subtitleLabel);
        
        mainPanel.add(titlePanel);

        // --------------------------------
        // 4. BUTTONS PANEL
        // --------------------------------

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(10, 80, 40, 80));

        btnCreate = UIComponents.createStyledButton("Create Form", 450, 50);
        btnManage = UIComponents.createStyledButton("Manage My Forms", 450, 50);
        btnViewF = UIComponents.createStyledButton("View Existing Forms", 450, 50);
        btnViewAF = UIComponents.createStyledButton("View My Answered Forms", 450, 50);

        btnCreate.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnManage.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnViewF.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnViewAF.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(btnCreate);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnManage);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnViewF);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnViewAF);

        btnCreate.addActionListener(e -> {
            new CreateFormView(controller).setVisible(true);
            dispose();
        });

        btnManage.addActionListener(e -> {
            new SelectFormView(controller, SelectFormView.FormType.CREATED, SelectFormView.FormAction.MANAGE, () -> {
                new FormMenuView(controller).setVisible(true);
            }).setVisible(true);
            dispose();
        });

        btnViewF.addActionListener(e -> {
            new SelectFormView(controller, SelectFormView.FormType.EXISTING, SelectFormView.FormAction.VIEW_FORM, () -> {
                new FormMenuView(controller).setVisible(true);
            }).setVisible(true);
            dispose();
        });

        btnViewAF.addActionListener(e -> {
            new SelectFormView(controller, SelectFormView.FormType.ANSWERED, SelectFormView.FormAction.VIEW_ANSWERED, () -> {
                new FormMenuView(controller).setVisible(true);
            }).setVisible(true);
            dispose();
        });
        
        // Add panels to main panel
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }
}