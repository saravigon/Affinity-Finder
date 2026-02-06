package presentation.classes.form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import presentation.classes.UIComponents;
import presentation.controllers.PresentationController;
import java.awt.*;

/**
 * View to manage a specific form.
 * Allows editing title, description, questions, and deleting the form.
 */
public class ManageForm extends JFrame {
    /**
     * Attribute: reference to the controller
     */
    private PresentationController controller;
    /**
     * Attribute: UFID of the form to manage
     */
    private int ufid;
    
    /**
     * Main panel
     */
    private JPanel mainPanel;
    /**
     * Top panel (contents Back button)
     */
    private JPanel topPanel;
    /**
     * Content panel (Form info and buttons)
     */
    private JPanel contentPanel;
    /**
     * Back button
     */
    private JButton btnBack;

    /**
     * Constructor
     * @param controller PresentationController reference
     * @param ufid UFID of the form to manage
     */
    public ManageForm(PresentationController controller, int ufid) {
        this.controller = controller;
        this.ufid = ufid;
        initComponents();
    }
    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Manage Form", 700, 700);

        // ================================
        // 1. MAIN PANEL
        // ================================
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // ================================
        // 2. HEADER (Back Button)
        // ================================
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBackground(Color.WHITE);
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new FormMenuView(controller).setVisible(true);
            dispose();
        });
        
        topPanel.add(btnBack);

        // ================================
        // 3. CONTENT PANEL (Title + Info + Buttons)
        // ================================
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // ────────────────────────────────
        // 3.1 FORM INFO PANEL
        // ────────────────────────────────

        // Get form title and description
        String formTitle;
        String formDescription;
        try {
            formTitle = controller.getController().getForm(ufid).getTitle();
            formDescription = controller.getController().getForm(ufid).getDescription();
        } catch (Exception e) {
            formTitle = "N/A";
            formDescription = "N/A";
        }

        // Form Info Panel (with border) - CENTERED
        JPanel formInfoPanel = new JPanel();
        formInfoPanel.setLayout(new BoxLayout(formInfoPanel, BoxLayout.Y_AXIS));
        formInfoPanel.setBackground(new Color(245, 250, 245));
        formInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 2, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        formInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formInfoPanel.setMaximumSize(new Dimension(580, 150));

        JLabel title = UIComponents.createCenterLabel("Form managed:");
        formInfoPanel.add(title);
        
        formInfoPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel formTitleLabel = new JLabel(formTitle);
        formTitleLabel.setFont(UIComponents.TITLE_FONT);
        formTitleLabel.setForeground(UIComponents.DARK_GREEN);
        formTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formInfoPanel.add(formTitleLabel);
        
        formInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel formDescLabel = new JLabel("<html><center><i>" + formDescription + "</i></center></html>");
        formDescLabel.setFont(UIComponents.ITALIC_FONT);
        formDescLabel.setForeground(Color.DARK_GRAY);
        formDescLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formInfoPanel.add(formDescLabel);

        contentPanel.add(formInfoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // ────────────────────────────────
        // ACTIONS SECTION - CENTERED
        // ────────────────────────────────
        JLabel actionsTitle = new JLabel("Available Actions");
        actionsTitle.setFont(UIComponents.STANDARD_FONT);
        actionsTitle.setForeground(Color.DARK_GRAY);
        actionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(actionsTitle);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Actions Panel (Scrollable list like members)
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(Color.WHITE);

        // Create action boxes
        actionsPanel.add(createActionBox("Add Question", "➕ Add a new question to the form", e -> handleAddQuestion()));
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        actionsPanel.add(createActionBox("Modify Question", "✏️ Edit existing questions", e -> handleModifyQuestion()));
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        actionsPanel.add(createActionBox("Change Title", "📝 Update form title", e -> handleChangeTitle()));
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        actionsPanel.add(createActionBox("Change Description", "📄 Update form description", e -> handleChangeDescription()));
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        

        actionsPanel.add(createActionBox("Delete Question", "🗑️ Remove a question from the form", e -> handleDeleteQuestion()));
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        actionsPanel.add(createDangerActionBox("Delete Form", "❌ Permanently delete this form", e -> handleDeleteForm()));
        

        JScrollPane scrollPane = new JScrollPane(actionsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        scrollPane.setMaximumSize(new Dimension(580, 300));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane);

        // Añadir el panel superior al NORTE (Arriba)
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Añadir el panel de contenido al CENTRO
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Asignar el panel principal a la ventana
        setContentPane(mainPanel);
        
        // Centrar en pantalla de nuevo por si el contenido cambió el tamaño
        setLocationRelativeTo(null);
    }

    /**
     * Creates a clickable action box (card-style) with hover effect
     * @param actionName Action name to display
     * @param description Action description
     * @param listener Action to perform on click
     * @return Clickable JPanel
     */
    private JPanel createActionBox(String actionName, String description, java.awt.event.ActionListener listener) {
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout());
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 1, true),
            new EmptyBorder(12, 20, 12, 20)
        ));
        box.setMaximumSize(new Dimension(550, 50));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Action name label (left)
        JLabel nameLabel = new JLabel(actionName);
        nameLabel.setFont(UIComponents.TEXT_FONT);
        nameLabel.setForeground(UIComponents.DARK_GREEN);
        box.add(nameLabel, BorderLayout.WEST);

        // Description label (right, initially invisible)
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(UIComponents.ITALIC_FONT);
        descLabel.setForeground(UIComponents.DARK_GREEN);
        descLabel.setVisible(false);
        box.add(descLabel, BorderLayout.EAST);

        // Hover effects
        box.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                box.setBackground(new Color(245, 250, 245));
                descLabel.setVisible(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                box.setBackground(Color.WHITE);
                descLabel.setVisible(false);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                listener.actionPerformed(new java.awt.event.ActionEvent(box, 0, ""));
            }
        });
        
        return box;
    }

    /**
     * Creates a danger action box (for delete form)
     */
    private JPanel createDangerActionBox(String actionName, String description, java.awt.event.ActionListener listener) {
        JPanel box = createActionBox(actionName, description, listener);
        Color dangerColor = new Color(220, 53, 69);
        
        // Override colors
        box.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(dangerColor, 1, true),
            new EmptyBorder(12, 20, 12, 20)
        ));
        
        // Update labels colors
        Component[] components = box.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(dangerColor);
            }
        }
        
        return box;
    }

    /**
     * Handles the action of adding a question
     */
    private void handleAddQuestion() {

        Runnable goBackToManage = () -> {
            new ManageForm(controller, ufid).setVisible(true);
        };
        new AddQuestionsView(controller, ufid, goBackToManage).setVisible(true);
        dispose();
    }

    /**
     * Handles the action of modifying a question
     */
    private void handleModifyQuestion() {
        new ModifyQuestionView(controller, ufid).setVisible(true);
        dispose();
    }

    /**
     * Handles the action of changing the form title
     */
    private void handleChangeTitle() {
        String newTitle = JOptionPane.showInputDialog(this,
            "Enter new title:",
            "Change Title",
            JOptionPane.QUESTION_MESSAGE);
        
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            try {
                controller.getController().changeFormTitle(ufid, newTitle.trim());
                JOptionPane.showMessageDialog(this,
                    "Title updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                // Refresh view
                dispose();
                new ManageForm(controller, ufid).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error updating title: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles the action of changing the form description
     */
    private void handleChangeDescription() {
        JTextArea textArea = new JTextArea(5, 35);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(UIComponents.STANDARD_FONT);
        
        try {
            String currentDesc = controller.getController().getForm(ufid).getDescription();
            textArea.setText(currentDesc);
        } catch (Exception e) {
            // Continue with empty text
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        
        int result = JOptionPane.showConfirmDialog(this,
            scrollPane,
            "Change Description",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String newDesc = textArea.getText().trim();
            if (!newDesc.isEmpty()) {
                try {
                    controller.getController().changeFormDescription(ufid, newDesc);
                    JOptionPane.showMessageDialog(this,
                        "Description updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    // Refresh view
                    dispose();
                    new ManageForm(controller, ufid).setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                        "Error updating description: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Handles the action of deleting a question
     */
    private void handleDeleteQuestion() {
        new DeleteQuestionView(controller, ufid).setVisible(true);
    }
    /**
     * Handle the action of plotting affinity groups
     */
    @SuppressWarnings("unused")
    private void handlePlotAffinityGroup(){
        try {
            controller.getController().chartAffinityGroups(ufid);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,
                "Error ploting: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the action of deleting the form
     */
    private void handleDeleteForm() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this form?\nThis action cannot be undone.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.getController().deleteForm(ufid);
                JOptionPane.showMessageDialog(this,
                    "Form deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                new FormMenuView(controller).setVisible(true);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting form: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}