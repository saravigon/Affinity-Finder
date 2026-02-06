package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.affinity.AffinityMenuView;
import presentation.classes.*;
import presentation.classes.admin.ImportManyAnswersView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Generic form selection view.
 * 
 * This view displays forms as interactive cards based on the type specified.
 * Can show answered forms, created forms, or existing forms.
 */
public class SelectFormView extends JFrame {
    
    /**
     * Form type to display
     */
    public enum FormType {
        ANSWERED("Answered Form", "Click on a form to view its affinity group", "View →"),
        CREATED("Created Form", "Click on a form to manage it", "Manage →"),
        EXISTING("Existing Form", "Click on a form to answer it", "Answer →");
        
        private final String title;
        private final String subtitle;
        private final String actionLabel;
        
        FormType(String title, String subtitle, String actionLabel) {
            this.title = title;
            this.subtitle = subtitle;
            this.actionLabel = actionLabel;
        }
        
        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public String getActionLabel() { return actionLabel; }
    }

    /**
     * Action to perform when clicking a form card
     */
    public enum FormAction {
        VIEW_AFFINITY,      // View affinity group (for answered forms)
        MANAGE,             // Manage form (for created forms)
        ANSWER,             // Answer form
        VIEW_FORM,          // View form details (read-only)
        VIEW_ANSWERED,      // View answered forms
        VIEW_ADMIN          // View form statistics
    }
    
    /**
     * Presentation controller reference
     */
    private PresentationController controller;

    /**
     * Type of forms to display
     */
    private FormType formType;

    /**
     * Action to perform on form click
     */
    private FormAction formAction;

    /**
     * Action to perform when going back
     */
    private Runnable goBackAction;

    /**
     * Main panel
     */
    private JPanel mainPanel;

    /**
     * Panel for the header (Back button)
     */
    private JPanel topPanel;  

    /**
     * Center panel (Form selection)
     */  
    private JPanel centerPanel; 

    /**
     * Back button
     */
    private JButton btnBack;

    /**
     * List to store form IDs corresponding to forms displayed
     */
    private ArrayList<Integer> formIds;

    
    // ---------------------------------------------
    // CONSTRUCTORS
    // ---------------------------------------------

    /**
     * Constructor with explicit action
     * @param controller Presentation controller
     * @param formType Type of forms to display
     * @param formAction Action to perform when clicking a form
     * @param goBackAction Action to perform when going back
     */
    public SelectFormView(PresentationController controller, FormType formType, FormAction formAction, Runnable goBackAction) {
        this.controller = controller;
        this.formType = formType;
        this.formAction = formAction;
        this.goBackAction = goBackAction;
        initComponents();
    }

    /**
     * Constructor with default action based on form type
     * @param controller Presentation controller
     * @param formType Type of forms to display
     * @param goBackAction Action to perform when going back
     */
    public SelectFormView(PresentationController controller, FormType formType, Runnable goBackAction) {
        this.controller = controller;
        this.formType = formType;
        this.goBackAction = goBackAction;
        
        // Set default action based on form type
        this.formAction = switch (formType) {
            case ANSWERED -> FormAction.VIEW_AFFINITY;
            case CREATED -> FormAction.MANAGE;
            case EXISTING -> FormAction.ANSWER;
        };
        
        initComponents();
    }

    /**
     * Initialize GUI components
     */
    private void initComponents() {
        // Update subtitle and action label based on form action
        String subtitle = getSubtitleForAction();
        String actionLabel = getActionLabelForAction();
        
        UIComponents.configureWindow(this, formType.getTitle().toUpperCase(), 700, 700);

        // 1. Main Panel with Gradient Background
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 2. Top Panel (Back button)
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            goBackAction.run();
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);

        // 3. Center Panel (Form selection with cards)
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 50, 30, 50));

        // Title
        JLabel titleLabel = new JLabel("Select " + getArticle() + " " + formType.getTitle());
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitleLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Get forms based on type
        ArrayList<FormCardInfo> formsData = getFormsData();

        // Display forms as cards
        if (formsData.isEmpty()) {
            JLabel noFormsLabel = new JLabel(getEmptyMessage());
            noFormsLabel.setFont(UIComponents.ITALIC_FONT);
            noFormsLabel.setForeground(Color.GRAY);
            noFormsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(noFormsLabel);
        } else {
            // Create scrollable container for form cards
            JPanel formsContainer = new JPanel();
            formsContainer.setLayout(new BoxLayout(formsContainer, BoxLayout.Y_AXIS));
            formsContainer.setBackground(Color.WHITE);
            formsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

            for (FormCardInfo formData : formsData) {
                JPanel formCard = createFormCard(formData, actionLabel);
                formsContainer.add(formCard);
                formsContainer.add(Box.createRigidArea(new Dimension(0, 12)));
            }

            // Add glue at the end to prevent expanding
            formsContainer.add(Box.createVerticalGlue());

            // Wrap in scroll pane with fixed size
            JScrollPane scrollPane = new JScrollPane(formsContainer);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
            scrollPane.setPreferredSize(new Dimension(580, 450));
            scrollPane.setMaximumSize(new Dimension(580, 450));
            scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            centerPanel.add(scrollPane);
        }

        mainPanel.add(centerPanel);
        mainPanel.add(Box.createVerticalGlue());

        // Add main panel to frame
        add(mainPanel);
    }

    /**
     * Gets subtitle text based on form action
     */
    private String getSubtitleForAction() {
        return switch (formAction) {
            case VIEW_AFFINITY -> "Click on a form to view its affinity group";
            case MANAGE -> "Click on a form to manage it";
            case ANSWER -> "Click on a form to answer it";
            case VIEW_FORM -> "Click on a form to view its details";
            case VIEW_ADMIN -> "Click on a form to import answers";
            case VIEW_ANSWERED -> "Click on a form to view your answers";
        };
    }

    /**
     * Gets action label based on form action
     */
    private String getActionLabelForAction() {
        return switch (formAction) {
            case VIEW_AFFINITY -> "View Group →";
            case MANAGE -> "Manage →";
            case ANSWER -> "Answer →";
            case VIEW_FORM -> "View →";
            case VIEW_ADMIN -> "Import →";
            case VIEW_ANSWERED -> "View Answers →";
        };
    }

    /**
     * Gets forms data based on the form type
     */
    private ArrayList<FormCardInfo> getFormsData() {
        formIds = new ArrayList<>();
        ArrayList<FormCardInfo> formsData = new ArrayList<>();

        try {
            switch (formType) {
                case ANSWERED:
                    loadAnsweredForms(formsData);
                    break;
                case CREATED:
                    loadCreatedForms(formsData);
                    break;
                case EXISTING:
                    loadExistingForms(formsData);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error loading forms: " + e.getMessage());
        }

        return formsData;
    }

    /**
     * Loads answered forms
     */
    private void loadAnsweredForms(ArrayList<FormCardInfo> formsData) {
        ArrayList<String> answeredFormsInfo = controller.getController().getAnsweredFormsInfo();

        for (String formInfo : answeredFormsInfo) {
            String[] lines = formInfo.split("\n");
            int formId = -1;
            String title = "";
            String description = "";
            
            for (String line : lines) {
                if (line.startsWith("Form UFID: ")) {
                    formId = Integer.parseInt(line.substring("Form UFID: ".length()).trim());
                } else if (line.startsWith("Title: ")) {
                    title = line.substring("Title: ".length()).trim();
                } else if (line.startsWith("Description: ")) {
                    description = line.substring("Description: ".length()).trim();
                }
            }
            
            if (formId != -1 && !title.isEmpty()) {
                formIds.add(formId);
                formsData.add(new FormCardInfo(formId, title, description));
            }
        }
    }

    /**
     * Loads created forms
     */
    private void loadCreatedForms(ArrayList<FormCardInfo> formsData) {
        var createdFormsIds = controller.getController().getCurrentProfile().getcreatedForms();

        for (Integer formId : createdFormsIds) {
            try {
                String title = controller.getController().getForm(formId).getTitle();
                String description = controller.getController().getForm(formId).getDescription();
                
                formIds.add(formId);
                formsData.add(new FormCardInfo(formId, title, description));
                
            } catch (Exception e) {
                System.err.println("Error loading form " + formId + ": " + e.getMessage());
            }
        }
    }

    /**
     * Loads existing forms
     */
    private void loadExistingForms(ArrayList<FormCardInfo> formsData) {
        ArrayList<String> existingFormsInfo = controller.getController().getExistingFormsInfo();

        for (String formInfo : existingFormsInfo) {
            String[] lines = formInfo.split("\n");
            int formId = -1;
            String title = "";
            String description = "";
            
            for (String line : lines) {
                if (line.startsWith("Form UFID: ")) {
                    formId = Integer.parseInt(line.substring("Form UFID: ".length()).trim());
                } else if (line.startsWith("Title: ")) {
                    title = line.substring("Title: ".length()).trim();
                } else if (line.startsWith("Description: ")) {
                    description = line.substring("Description: ".length()).trim();
                }
            }
            
            if (formId != -1 && !title.isEmpty()) {
                formIds.add(formId);
                formsData.add(new FormCardInfo(formId, title, description));
            }
        }
    }

    /**
     * Gets empty message based on form type
     */
    private String getEmptyMessage() {
        return switch (formType) {
            case ANSWERED -> "You haven't answered any forms yet.";
            case CREATED -> "You haven't created any forms yet.";
            case EXISTING -> "No forms available.";
        };
    }

    /**
     * Gets article (a/an) based on form type
     */
    private String getArticle() {
        return switch (formType) {
            case ANSWERED -> "an";
            case CREATED -> "a";
            case EXISTING -> "an";
        };
    }

    /**
     * Creates a form card (clickable)
     */
    private JPanel createFormCard(FormCardInfo formData, String actionLabel) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 1, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.setPreferredSize(new Dimension(520, 100));
        card.setMaximumSize(new Dimension(520, 100));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Left panel (info)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(formData.title);
        titleLabel.setFont(UIComponents.STANDARD_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);
        
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        String displayDesc = formData.description;
        if (displayDesc.length() > 70) {
            displayDesc = displayDesc.substring(0, 67) + "...";
        }
        
        JLabel descLabel = new JLabel(displayDesc);
        descLabel.setFont(UIComponents.SMALL_FONT);
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Right panel (action label)
        JLabel actionLabelComponent = new JLabel(actionLabel);
        actionLabelComponent.setFont(UIComponents.STANDARD_FONT);
        actionLabelComponent.setForeground(UIComponents.DARK_GREEN);
        actionLabelComponent.setVisible(false);
        card.add(actionLabelComponent, BorderLayout.EAST);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 250, 245));
                infoPanel.setBackground(new Color(245, 250, 245));
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(UIComponents.DARK_GREEN, 2, true),
                    new EmptyBorder(20, 25, 20, 25)
                ));
                actionLabelComponent.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                infoPanel.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(UIComponents.DARK_GREEN, 1, true),
                    new EmptyBorder(20, 25, 20, 25)
                ));
                actionLabelComponent.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleFormCardClick(formData.formId);
            }
        });

        return card;
    }

    /**
     * Handles form card click based on form action
     */
    private void handleFormCardClick(int formId) {
        try {
            switch (formAction) {
                case VIEW_AFFINITY:
                    viewAffinityGroup(formId);
                    break;
                case MANAGE:
                    new ManageForm(controller, formId).setVisible(true);
                    dispose();
                    break;
                case ANSWER:
                    int userId = controller.getController().getCurrentProfile().getUUID();
                    boolean alreadyAnswered = controller.getController().hasUserAnsweredForm(formId, userId);
                    
                    if (alreadyAnswered) {
                        int choice = JOptionPane.showConfirmDialog(this,
                            "You have already answered this form.\nDo you want to edit your answers?",
                            "Edit Answers",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                        
                        if (choice == JOptionPane.YES_OPTION) {
                            Runnable goBack = () -> {
                                new SelectFormView(controller, 
                                    SelectFormView.FormType.EXISTING, 
                                    SelectFormView.FormAction.ANSWER,
                                    () -> new MainMenuView(controller).setVisible(true)
                                ).setVisible(true);
                            };
                            new AnswerFormView(controller, formId, true, goBack).setVisible(true); // Edit mode
                            dispose();
                        }
                        // If NO, do nothing (stays on selection screen)
                    } else {
                        new AnswerFormView(controller, formId, false, null).setVisible(true); // New answer
                        dispose();
                    }
                    break;
                case VIEW_FORM:
                    new ViewFormsView(controller, formId).setVisible(true);
                    dispose();
                    break;
                case VIEW_ANSWERED:
                    new ViewAnsweredFormsView(controller, formId).setVisible(true);
                    dispose();
                    break;
                case VIEW_ADMIN:
                    new ImportManyAnswersView(controller, formId).setVisible(true);
                    dispose();
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows the affinity group for the selected form
     */
    private void viewAffinityGroup(int formId) {
        try {
            int userId = controller.getController().getCurrentProfile().getUUID();
            
            boolean hasGroup = controller.getController().searchAffinityGroup(formId, userId);
            
            if (!hasGroup) {
                JOptionPane.showMessageDialog(this,
                    "You are not assigned to any affinity group for this form yet.\n" +
                    "Groups are created after running the clustering algorithm.",
                    "No Affinity Group",
                    JOptionPane.INFORMATION_MESSAGE);
                
                return;
            }
            
            new AffinityMenuView(controller, formId).setVisible(true);
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error viewing affinity group: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Inner class to store form card information
     */
    private static class FormCardInfo {
        int formId;
        String title;
        String description;

        FormCardInfo(int formId, String title, String description) {
            this.formId = formId;
            this.title = title;
            this.description = description;
        }
    }
}