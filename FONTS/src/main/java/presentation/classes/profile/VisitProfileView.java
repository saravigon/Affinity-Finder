package presentation.classes.profile;

import presentation.controllers.PresentationController;
import presentation.classes.*;
import domain.classes.Form;
import domain.classes.Profile;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * View Profile page.
 * Displays user information including username, description, created forms and answered forms.
 * Features a modern card-based design with gradient header.
 */
public class VisitProfileView extends JFrame {
    
    /**
     * Presentation controller reference
     */
    private PresentationController controller;
    
    /**
     * User ID to display
     */
    private Integer userID;

    /**
     * Form ID to return to AffinityMenu
     */
    private Integer formID;
    
    /**
     * Profile object
     */
    private Profile profile;
    
    /**
     * Main panel
     */
    private JPanel mainPanel;
    
    /**
     * Back button
     */
    private JButton btnBack;

    /**
     * Constructor
     * @param controller Presentation controller
     * @param userID User ID to view profile
     * @param formID Form ID to return to AffinityMenu (optional, can be null)
     */
    public VisitProfileView(PresentationController controller, Integer userID, Integer formID) {
        this.controller = controller;
        this.userID = userID;
        this.formID = formID;
        initComponents();
    }

    /**
     * Initialize GUI components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "USER PROFILE", 700, 700);

        // Main Panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Top Panel (Back button)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            if (formID != null) {
                new presentation.classes.affinity.AffinityMenuView(controller, formID).setVisible(true);
            }
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);

        // Get profile data
        try {
            profile = controller.getController().getProfileByID(userID);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading profile", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // Create scrollable content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 50, 30, 50));

        // Profile Header Card (with gradient-style background)
        JPanel headerCard = createProfileHeaderCard();
        contentPanel.add(headerCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Description Card
        JPanel descriptionCard = createDescriptionCard();
        contentPanel.add(descriptionCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Statistics Panel (side by side)
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setMaximumSize(new Dimension(650, 60));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel createdStat = createStatCard("Forms Created", profile.getcreatedForms().size(), new Color(76, 175, 80));
        JPanel answeredStat = createStatCard("Forms Answered", profile.getAnsweredForms().size(), new Color(33, 150, 243));
        
        statsPanel.add(createdStat);
        statsPanel.add(answeredStat);
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Created Forms Section
        JPanel createdFormsSection = createFormsSection("Created Forms", profile.getcreatedForms(), new Color(76, 175, 80));
        contentPanel.add(createdFormsSection);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Answered Forms Section
        JPanel answeredFormsSection = createFormsSection("Answered Forms", profile.getAnsweredForms(), new Color(33, 150, 243));
        contentPanel.add(answeredFormsSection);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
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
        card.setBackground(new Color(245, 250, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 2, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        card.setMaximumSize(new Dimension(650, 180));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Avatar (circle with initials)
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circle
                g2d.setColor(UIComponents.DARK_GREEN);
                g2d.fillOval(0, 0, 80, 80);
                
                // Draw initials
                g2d.setColor(Color.WHITE);
                g2d.setFont(UIComponents.TITLE_FONT);
                String initials = getInitials(profile.getUsername());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (80 - fm.stringWidth(initials)) / 2;
                int y = ((80 - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(initials, x, y);
            }
        };
        avatarPanel.setPreferredSize(new Dimension(80, 80));
        avatarPanel.setMaximumSize(new Dimension(80, 80));
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarPanel.setOpaque(false);
        card.add(avatarPanel);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Username
        JLabel usernameLabel = new JLabel(profile.getUsername());
        usernameLabel.setFont(UIComponents.TITLE_FONT);
        usernameLabel.setForeground(UIComponents.DARK_GREEN);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(usernameLabel);

        card.add(Box.createRigidArea(new Dimension(0, 5)));

        return card;
    }

    /**
     * Creates the description card
     */
    private JPanel createDescriptionCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(650, 120));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("About");
        titleLabel.setFont(UIComponents.STANDARD_FONT);
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        String description = profile.getDescription();
        if (description == null || description.trim().isEmpty() || description.equals("No description yet")) {
            description = "This user hasn't added a description yet.";
        }

        JTextArea descArea = new JTextArea(description);
        descArea.setFont(UIComponents.TEXT_FONT);
        descArea.setForeground(Color.DARK_GRAY);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setBorder(null);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descArea);

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
            new EmptyBorder(15, 20, 15, 20)
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
     * Creates a forms section (created or answered)
     */
    private JPanel createFormsSection(String title, List<Integer> formIDs, Color accentColor) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.setMaximumSize(new Dimension(650, Integer.MAX_VALUE));

        // Section title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIComponents.STANDARD_FONT);
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        
        section.add(Box.createRigidArea(new Dimension(0, 12)));

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
                JPanel formCard = createFormCard(formID, accentColor);
                formsContainer.add(formCard);
                formsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            // Wrap in scroll pane if there are many forms
            if (formIDs.size() > 3) {
                JScrollPane scrollPane = new JScrollPane(formsContainer);
                scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
                scrollPane.setMaximumSize(new Dimension(650, 250));
                scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                section.add(scrollPane);
            } else {
                section.add(formsContainer);
            }
        }

        return section;
    }

    /**
     * Creates a form card (clickable)
     */
    private JPanel createFormCard(Integer formID, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(630, 80));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Get form info - use temporary variables first
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
            // Keep default values
        }
        
        // Now assign to final variables for use in MouseAdapter
        final String formTitle = tempTitle;
        final String formDesc = tempDesc;

        // Left panel (info)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(formTitle);
        titleLabel.setFont(UIComponents.STANDARD_FONT);
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);
        
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel descLabel = new JLabel(formDesc);
        descLabel.setFont(UIComponents.SMALL_FONT);
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Right panel (action label)
        JLabel actionLabel = new JLabel("View →");
        actionLabel.setFont(UIComponents.SMALL_FONT);
        actionLabel.setForeground(accentColor);
        actionLabel.setVisible(false);
        card.add(actionLabel, BorderLayout.EAST);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
                infoPanel.setBackground(new Color(250, 250, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(accentColor, 2, true),
                    new EmptyBorder(15, 20, 15, 20)
                ));
                actionLabel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                infoPanel.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(220, 220, 220), 1, true),
                    new EmptyBorder(15, 20, 15, 20)
                ));
                actionLabel.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigate to form view
                JOptionPane.showMessageDialog(
                    VisitProfileView.this,
                    "Navigate to form: " + formTitle + " (ID: " + formID + ")\n\nThis feature is coming soon!",
                    "View Form",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        return card;
    }

    /**
     * Gets initials from username (max 2 characters)
     */
    private String getInitials(String username) {
        if (username == null || username.isEmpty()) {
            return "?";
        }
        String[] parts = username.split(" ");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
        }
        return username.substring(0, Math.min(2, username.length())).toUpperCase();
    }
}