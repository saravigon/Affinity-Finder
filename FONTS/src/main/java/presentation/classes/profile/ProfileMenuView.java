package presentation.classes.profile;

import presentation.controllers.PresentationController;
import presentation.classes.*;
import domain.classes.Form;
import domain.classes.Profile;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Profile menu view.
 * Displays current user profile with complete information, statistics, and editable fields.
 * Features a modern card-based design similar to VisitProfileView.
 */
public class ProfileMenuView extends JFrame {

    /**
     * Reference to PresentationController
     */
    private PresentationController controller;
    /**
     * Main panel with gradient background
     */
    private JPanel mainPanel;
    /**
     * Back button
     * It allows returning to the main menu
     */
    private JButton btnBack;
    /**
     * The current user's profile
     */
    private Profile profile;

    /**
     * Blue accent color
     */    
    private Color blueAccent = new Color(33, 150, 243);

    /**
     * Constructor
     * @param controller Presentation controller reference
     */
    public ProfileMenuView(PresentationController controller) {
        this.controller = controller;
        initComponents();
    }
    
    /**
     * Initializes components of the view
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "MY PROFILE", 700, 700);

        // Main Panel con degradado
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Top Panel (Back button)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(15, 20, 5, 20));
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new MainMenuView(controller).setVisible(true);
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);

        // Get profile data
        try {
            profile = controller.getController().getCurrentProfile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading profile", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // Create scrollable content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(5, 30, 20, 30));

        // Profile Header Card
        JPanel headerCard = createProfileHeaderCard();
        contentPanel.add(headerCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Username & Password Card (Editable)
        JPanel credentialsCard = createCredentialsCard();
        contentPanel.add(credentialsCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Description Card (Editable)
        JPanel descriptionCard = createDescriptionCard();
        contentPanel.add(descriptionCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(600, 60));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel createdStat = createStatCard("Forms Created", profile.getcreatedForms().size(), UIComponents.ACCENT_GREEN);
        JPanel answeredStat = createStatCard("Forms Answered", profile.getAnsweredForms().size(), blueAccent);
        
        statsPanel.add(createdStat);
        statsPanel.add(answeredStat);
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Created Forms Section (con scroll)
        JPanel createdFormsSection = createFormsSection("Created Forms", profile.getcreatedForms(), UIComponents.ACCENT_GREEN);
        contentPanel.add(createdFormsSection);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Answered Forms Section (con scroll)
        JPanel answeredFormsSection = createFormsSection("Answered Forms", profile.getAnsweredForms(), blueAccent);
        contentPanel.add(answeredFormsSection);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Action Buttons Panel
        JPanel actionsPanel = createActionsPanel();
        contentPanel.add(actionsPanel);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane);

        add(mainPanel);
    }

    /**
     * Creates the profile header card with avatar and username
     */
    private JPanel createProfileHeaderCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIComponents.LIGHT_GREEN);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(600, 140));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Avatar (circle with initials)
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(UIComponents.ACCENT_GREEN);
                g2d.fillOval(0, 0, 60, 60);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(UIComponents.STANDARD_FONT);
                String initials = getInitials(profile.getUsername());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (60 - fm.stringWidth(initials)) / 2;
                int y = ((60 - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(initials, x, y);
            }
        };
        avatarPanel.setPreferredSize(new Dimension(60, 60));
        avatarPanel.setMaximumSize(new Dimension(60, 60));
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarPanel.setOpaque(false);
        card.add(avatarPanel);
        
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel usernameLabel = new JLabel(profile.getUsername());
        usernameLabel.setFont(UIComponents.TITLE_FONT);
        usernameLabel.setForeground(UIComponents.DARK_GREEN);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(usernameLabel);

        return card;
    }

    /**
     * Creates the credentials card (Username and Password) with edit icons
     */
    private JPanel createCredentialsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        card.setMaximumSize(new Dimension(600, 100));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username Row
        JPanel usernameRow = createEditableField("Username", profile.getUsername(), true);
        card.add(usernameRow);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password Row
        JPanel passwordRow = createEditableField("Password", "••••••••", false);
        card.add(passwordRow);

        return card;
    }

    /**
     * Creates an editable field with label and edit icon
     */
    private JPanel createEditableField(String label, String value, boolean isUsername) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Label and value
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setOpaque(false);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(UIComponents.SMALL_FONT);
        labelLabel.setForeground(UIComponents.DARK_GREEN);
        labelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIComponents.TEXT_FONT);
        valueLabel.setForeground(Color.DARK_GRAY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(labelLabel);
        textPanel.add(valueLabel);

        // Edit button
        JButton editBtn = new JButton("✎") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(76, 175, 80, 30));
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }
                
                super.paintComponent(g);
            }
        };
        editBtn.setContentAreaFilled(false);
        editBtn.setFocusPainted(false);
        editBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        editBtn.setForeground(UIComponents.ACCENT_GREEN);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setPreferredSize(new Dimension(35, 35));
        editBtn.setMaximumSize(new Dimension(35, 35));
        editBtn.setBorder(null);
        editBtn.addActionListener(e -> {
            if (isUsername) {
                handleChangeUsername();
            } else {
                handleChangePassword();
            }
        });

        row.add(textPanel, BorderLayout.CENTER);
        row.add(editBtn, BorderLayout.EAST);

        return row;
    }

    /**
     * Creates the description card (Editable)
     */
    private JPanel createDescriptionCard() {
        JPanel card = new JPanel(new BorderLayout(8, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        card.setMaximumSize(new Dimension(600, 90));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Left: Description content
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setBackground(Color.WHITE);
        descPanel.setOpaque(true);

        JLabel titleLabel = new JLabel("About");
        titleLabel.setFont(UIComponents.SMALL_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descPanel.add(titleLabel);
        
        descPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        String description = profile.getDescription();
        if (description == null || description.trim().isEmpty() || description.equals("No description yet")) {
            description = "No description added yet.";
        }
        
        // Truncar descripción si es muy larga
        if (description.length() > 80) {
            description = description.substring(0, 77) + "...";
        }

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(UIComponents.TEXT_FONT);
        descLabel.setForeground(Color.DARK_GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descPanel.add(descLabel);

        // Right: Edit button
        JButton editBtn = new JButton("✎") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(76, 175, 80, 30));
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }
                
                super.paintComponent(g);
            }
        };
        editBtn.setContentAreaFilled(false);
        editBtn.setFocusPainted(false);
        editBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        editBtn.setForeground(UIComponents.ACCENT_GREEN);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setPreferredSize(new Dimension(35, 35));
        editBtn.setMaximumSize(new Dimension(35, 35));
        editBtn.setBorder(null);
        editBtn.setAlignmentY(Component.TOP_ALIGNMENT);
        editBtn.addActionListener(e -> handleChangeDescription());

        card.add(descPanel, BorderLayout.CENTER);
        card.add(editBtn, BorderLayout.EAST);

        return card;
    }

    /**
     * Creates a statistics card
     */
    private JPanel createStatCard(String label, int count, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accentColor, 2, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel countLabel = new JLabel(String.valueOf(count));
        countLabel.setFont(UIComponents.TITLE_FONT);
        countLabel.setForeground(accentColor);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(countLabel);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(UIComponents.SMALL_FONT);
        textLabel.setForeground(Color.GRAY);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(textLabel);

        return card;
    }

    /**
     * Creates a forms section (created or answered) with internal scroll
     */
    private JPanel createFormsSection(String title, List<Integer> formIDs, Color accentColor) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        // Section title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIComponents.SMALL_FONT);
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        
        section.add(Box.createRigidArea(new Dimension(0, 6)));

        if (formIDs == null || formIDs.isEmpty()) {
            JLabel emptyLabel = new JLabel("No forms to display");
            emptyLabel.setFont(UIComponents.ITALIC_FONT);
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            section.add(emptyLabel);
        } else {
            JPanel formsContainer = new JPanel();
            formsContainer.setLayout(new BoxLayout(formsContainer, BoxLayout.Y_AXIS));
            formsContainer.setBackground(Color.WHITE);
            formsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (Integer formID : formIDs) {
                JPanel formCard = createFormCard(formID);
                formsContainer.add(formCard);
                formsContainer.add(Box.createRigidArea(new Dimension(0, 6)));
            }

            // Scroll interno para los forms
            JScrollPane scrollPane = new JScrollPane(formsContainer);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
            scrollPane.setMaximumSize(new Dimension(600, 150));
            scrollPane.setPreferredSize(new Dimension(600, 150));
            scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            section.add(scrollPane);
        }

        return section;
    }

    /**
     * Creates a form card
     */
    private JPanel createFormCard(Integer formID) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        card.setMaximumSize(new Dimension(580, 70));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        String tempTitle = "Unknown Form";
        String tempDesc = "Form details unavailable";
        
        try {
            Form form = controller.getController().getForm(formID);
            tempTitle = form.getTitle();
            String description = form.getDescription();
            
            if (description.length() > 60) {
                tempDesc = description.substring(0, 57) + "...";
            } else {
                tempDesc = description;
            }
        } catch (Exception e) {
            // Keep defaults
        }

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(tempTitle);
        titleLabel.setFont(UIComponents.SMALL_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);
        
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        JLabel descLabel = new JLabel(tempDesc);
        descLabel.setFont(UIComponents.TEXT_FONT);
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Creates the action buttons panel
     */
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(600, 100));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Log Out Button
        JButton btnLogout = createActionButton("🚪 Log Out", "Return to login", blueAccent);
        btnLogout.addActionListener(e -> {
            controller.inicializeController(null);
            new InitialMenuView(controller).setVisible(true);
            dispose();
        });

        // Delete Button
        JButton btnDelete = createActionButton("🗑 Delete Profile", "Permanently delete account", new Color(244, 67, 54));
        btnDelete.addActionListener(e -> handleDelete());

        panel.add(btnLogout);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnDelete);

        return panel;
    }

    /**
     * Creates an action button with custom styling
     */
    private JButton createActionButton(String title, String description, Color accentColor) {
        JButton btn = new JButton("<html><b>" + title + "</b><br><small>" + description + "</small></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), 
                    new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                if (getModel().isRollover()) {
                    g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(UIComponents.TEXT_FONT);
        btn.setForeground(accentColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(600, 60));
        btn.setMaximumSize(new Dimension(600, 60));
        btn.setBorder(null);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        return btn;
    }

    /**
     * Handles username change
     */
    private void handleChangeUsername() {
        String newUsername = JOptionPane.showInputDialog(this,
            "Enter new username:",
            "Change Username",
            JOptionPane.QUESTION_MESSAGE);
        
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            try {
                controller.getController().changeProfileUsername(newUsername.trim());
                JOptionPane.showMessageDialog(this,
                    "Username updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                // Refresh view
                dispose();
                new ProfileMenuView(controller).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error updating username: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles password change
     */
    private void handleChangePassword() {
        // Create password input panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel oldLabel = new JLabel("Current Password:");
        oldLabel.setFont(UIComponents.TEXT_FONT);
        oldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField oldPasswordField = new JPasswordField(20);
        oldPasswordField.setFont(UIComponents.TEXT_FONT);
        oldPasswordField.setMaximumSize(new Dimension(300, 30));
        oldPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(oldLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(oldPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel newLabel = new JLabel("New Password:");
        newLabel.setFont(UIComponents.TEXT_FONT);
        newLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(UIComponents.TEXT_FONT);
        newPasswordField.setMaximumSize(new Dimension(300, 30));
        newPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(newLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(newPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel confirmLabel = new JLabel("Confirm New Password:");
        confirmLabel.setFont(UIComponents.TEXT_FONT);
        confirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(UIComponents.TEXT_FONT);
        confirmPasswordField.setMaximumSize(new Dimension(300, 30));
        confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(confirmLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(confirmPasswordField);
        
        int result = JOptionPane.showConfirmDialog(this,
            panel,
            "Change Password",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Validations
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "All fields are required.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
                        
            if (newPassword.length() < 4) {
                JOptionPane.showMessageDialog(this,
                    "Password must be at least 4 characters long.",
                    "Invalid Password",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "New passwords don't match.",
                    "Password Mismatch",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Verify old password
                if (!controller.getController().getCurrentProfile().getPassword().equals(oldPassword)) {
                    JOptionPane.showMessageDialog(this,
                        "Current password is incorrect.",
                        "Authentication Failed",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Change password
                controller.getController().changeProfilePassword(newPassword);
                JOptionPane.showMessageDialog(this,
                    "Password updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh view
                dispose();
                new ProfileMenuView(controller).setVisible(true);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error updating password: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles description change
     */
    private void handleChangeDescription() {
        JTextArea textArea = new JTextArea(5, 35);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(UIComponents.STANDARD_FONT);
        
        try {
            String currentDesc = controller.getController().getCurrentProfile().getDescription();
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
                    controller.getController().changeProfileDescription(newDesc);;
                    JOptionPane.showMessageDialog(this,
                        "Description updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    // Refresh view
                    dispose();
                    new ProfileMenuView(controller).setVisible(true);
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
     * Handles the delete profile action
     */
    private void handleDelete() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure about deleting your profile?\nThis action cannot be undone.",
            "Delete Profile",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.getController().deleteCurrentProfile();
                JOptionPane.showMessageDialog(this, "Profile deleted successfully.");
                new InitialMenuView(controller).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Gets initials from username
     */
    private String getInitials(String username) {
        if (username == null || username.isEmpty()) return "?";
        String[] parts = username.split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return initials.length() > 0 ? initials.toString() : "?";
    }
}