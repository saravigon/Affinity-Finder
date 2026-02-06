package presentation.classes.admin;

import presentation.controllers.PresentationController;
import presentation.classes.*;
import presentation.classes.form.FormMenuView;
import presentation.classes.form.SelectFormView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Admin menu view with options to manage profiles and clustering.
 * Features modern card-based design consistent with other views.
 */
public class AdminMenuView extends JFrame {
    
    /**
     * Attribute: reference to PresentationController
     */
    private PresentationController controller;
    
    /**
     * Main panel containing all components
     */
    private JPanel mainPanel;
    
    /**
     * Evaluate button to evaluate clustering
     */
    private JButton btnEvaluate;
    
    /**
     * Kmeans execution button
     */
    private JButton btnKmeans;
    
    /**
     * Delete profile button
     */
    private JButton btnDelete;
    
    /**
     * Import answers button
     */
    private JButton btnImportAnswers;
    
    /**
     * Logout button
     */
    private JButton btnLogout;

    /**
     * Constructor of the AdminMenuView
     * @param controller PresentationController instance
     */
    public AdminMenuView(PresentationController controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * Method to initialize components and layout
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Admin Menu", 700, 700);

        // Main panel with gradient background
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ==================== TITLE SECTION ====================
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(40, 50, 20, 50));

        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("System management and administration");
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titlePanel.add(subtitleLabel);
        
        mainPanel.add(titlePanel);

        // ==================== BUTTONS WRAPPER PANEL ====================
        JPanel buttonsWrapperPanel = new JPanel();
        buttonsWrapperPanel.setLayout(new BoxLayout(buttonsWrapperPanel, BoxLayout.Y_AXIS));
        buttonsWrapperPanel.setOpaque(false);

        // ==================== BUTTONS PANEL ====================
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create menu buttons
        btnKmeans = createMenuButton("🔄 Execute K-Means", "Run clustering algorithm on forms");
        btnKmeans.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnKmeans.addActionListener(e -> {
            new ExecuteKmeansView(controller).setVisible(true);
            dispose();
        });

        btnEvaluate = createMenuButton("📊 Evaluate Clustering", "Analyze clustering quality metrics");
        btnEvaluate.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEvaluate.addActionListener(e -> {
            new EvaluateKmeansView(controller).setVisible(true);
            dispose();
        });

        btnDelete = createMenuButton("🗑 Delete Profile", "Remove user profiles from system");
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.addActionListener(e -> {
            new DeleteProfileView(controller).setVisible(true);
            dispose();
        });

        btnImportAnswers = createMenuButton("📥 Import Answers", "Bulk import form responses");
        btnImportAnswers.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImportAnswers.addActionListener(e -> {
            new SelectFormView(controller, SelectFormView.FormType.EXISTING, SelectFormView.FormAction.VIEW_ADMIN, () -> {
                new AdminMenuView(controller).setVisible(true);
            }).setVisible(true);
            dispose();
        });

        btnLogout = createMenuButton("🚪 Log Out", "Exit admin panel and return to login");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.addActionListener(e -> {
            controller.inicializeController(null);
            new InitialMenuView(controller).setVisible(true);
            dispose();
        });

        // Add buttons with spacing
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(btnKmeans);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnEvaluate);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnDelete);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnImportAnswers);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        buttonsPanel.add(btnLogout);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonsPanel, BorderLayout.CENTER);
        
        // Create scroll pane for buttons
        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        buttonsWrapperPanel.add(scrollPane);
        buttonsWrapperPanel.add(Box.createVerticalGlue());

        mainPanel.add(buttonsWrapperPanel);

        add(mainPanel);
    }

    /**
     * Creates a menu button with icon and description
     */
    private JButton createMenuButton(String title, String description) {
        JButton btn = new JButton("<html><center><b style='font-size:14px'>" + title + "</b><br><span style='font-size:11px'>" + description + "</span></center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), UIComponents.LIGHT_GREEN);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Border
                g2d.setColor(UIComponents.DARK_GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

                // Hover effect
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(76, 175, 80, 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(UIComponents.TEXT_FONT);
        btn.setForeground(UIComponents.DARK_GREEN);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(450, 70));
        btn.setMaximumSize(new Dimension(450, 70));
        btn.setMinimumSize(new Dimension(450, 70));
        btn.setBorder(null);

        return btn;
    }
}
