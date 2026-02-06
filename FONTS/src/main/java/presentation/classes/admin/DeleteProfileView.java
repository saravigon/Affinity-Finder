package presentation.classes.admin;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * View for admin to delete a user profile by username.
 * Features modern card-based design with gradient background.
 */
public class DeleteProfileView extends JFrame {
    
    /**
     * Reference to PresentationController
     */
    private PresentationController controller;
    
    /**
     * Main panel with gradient
     */
    private JPanel mainPanel;
    
    /**
     * Username input field
     */
    private JTextField txtUsername;
    
    /**
     * Delete button
     */
    private JButton btnDelete;
    
    /**
     * Back button
     */
    private JButton btnBack;

    /**
     * Constructor
     * @param controller PresentationController reference
     */
    public DeleteProfileView(PresentationController controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Delete User Profile", 700, 700);

        // Main panel with gradient background
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- TOP: Back button ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new AdminMenuView(controller).setVisible(true);
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- CENTER: Content card ---
        JPanel contentCard = new JPanel();
        contentCard.setLayout(new BoxLayout(contentCard, BoxLayout.Y_AXIS));
        contentCard.setBackground(Color.WHITE);
        contentCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 2, true),
            new EmptyBorder(30, 40, 30, 40)
        ));
        contentCard.setMaximumSize(new Dimension(600, 400));
        contentCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Delete User Profile");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentCard.add(titleLabel);

        contentCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Remove a user account from the system");
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentCard.add(subtitleLabel);

        contentCard.add(Box.createRigidArea(new Dimension(0, 25)));

        // Warning banner
        JPanel warningPanel = new JPanel();
        warningPanel.setLayout(new BoxLayout(warningPanel, BoxLayout.Y_AXIS));
        warningPanel.setBackground(new Color(255, 243, 224));
        warningPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 152, 0), 2, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        warningPanel.setMaximumSize(new Dimension(520, 80));
        warningPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel warningIcon = new JLabel("⚠️ WARNING");
        warningIcon.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        warningIcon.setForeground(new Color(230, 81, 0));
        warningIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        warningPanel.add(warningIcon);
        
        warningPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel warningText = new JLabel("This action is permanent and cannot be undone.");
        warningText.setFont(UIComponents.TEXT_FONT);
        warningText.setForeground(new Color(102, 60, 0));
        warningText.setAlignmentX(Component.LEFT_ALIGNMENT);
        warningPanel.add(warningText);

        contentCard.add(warningPanel);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Username input section
        JPanel inputSection = new JPanel();
        inputSection.setLayout(new BoxLayout(inputSection, BoxLayout.Y_AXIS));
        inputSection.setBackground(Color.WHITE);
        inputSection.setOpaque(false);
        inputSection.setMaximumSize(new Dimension(520, 100));
        inputSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsername = new JLabel("Username to delete:");
        lblUsername.setFont(UIComponents.STANDARD_FONT);
        lblUsername.setForeground(UIComponents.DARK_GREEN);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(lblUsername);

        inputSection.add(Box.createRigidArea(new Dimension(0, 8)));

        txtUsername = new JTextField();
        txtUsername.setFont(UIComponents.TEXT_FONT);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        txtUsername.setMaximumSize(new Dimension(520, 40));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(txtUsername);

        contentCard.add(inputSection);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Delete button
        btnDelete = createDeleteButton();
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.addActionListener(e -> handleDeleteProfile());
        contentCard.add(btnDelete);

        mainPanel.add(contentCard);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates the delete button with custom styling
     */
    private JButton createDeleteButton() {
        JButton btn = new JButton("🗑 Delete Profile") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Red gradient background
                Color redColor = new Color(220, 53, 69);
                Color darkRed = new Color(200, 35, 51);
                
                GradientPaint gp;
                if (getModel().isRollover()) {
                    gp = new GradientPaint(0, 0, darkRed, 0, getHeight(), redColor);
                } else {
                    gp = new GradientPaint(0, 0, redColor, 0, getHeight(), darkRed);
                }
                
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Border
                g2d.setColor(darkRed);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(UIComponents.STANDARD_FONT);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setBorder(null);

        return btn;
    }

    /**
     * Handles the delete profile action
     */
    private void handleDeleteProfile() {
        String username = txtUsername.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a username.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete the profile '" + username + "'?\nThis action cannot be undone.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Boolean deleted = controller.getController().deleteUserByUsername(username);
            
            if (deleted) {
                JOptionPane.showMessageDialog(this,
                    "Profile '" + username + "' deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                new AdminMenuView(controller).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete profile '" + username + "'.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
