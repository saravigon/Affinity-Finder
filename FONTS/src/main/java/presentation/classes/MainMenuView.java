package presentation.classes;

import presentation.classes.form.*;
import presentation.classes.profile.*;
import presentation.controllers.PresentationController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Main menu view of the Affinity Finder application.
 */
public class MainMenuView extends JFrame {

    /**
     * PresentationController reference
     */
    private PresentationController controller;
    /**
     * Button for opening the form view
     */
    private JButton btnForm;
    /**
     * Button for answering forms
     */
    private JButton btnAnswer; 
    /**
     * Button for viewing affinity groups
     */  
    private JButton btnAffinity;
    /**
     * Profile icon button
     * It will open the profile menu when clicked
     */
    private JButton btnProfileIcon;
    /**
     * Main panel containing all components
     */
    private JPanel mainPanel;

    /**
     * Constructor
     * @param controller PresentationController reference
     */
    public MainMenuView(PresentationController controller) {
        this.controller = controller;
        initComponents();
    }
    /**
     * Initializes the view components
     */
    private void initComponents() {
        
        UIComponents.configureWindow(this, "Affinity Finder", 700, 700);

        // Panel principal con degradado
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ==================== TOP PANEL (Profile Icon + Exit Button) ====================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        
        // Left: Exit Button
        JButton btnExit = createExitButton();
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        exitPanel.setOpaque(false);
        exitPanel.add(btnExit);
        topPanel.add(exitPanel, BorderLayout.WEST);
        
        // Right: Profile Icon
        btnProfileIcon = createProfileButton();
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        profilePanel.setOpaque(false);
        profilePanel.add(btnProfileIcon);
        topPanel.add(profilePanel, BorderLayout.EAST);
        
        mainPanel.add(topPanel);

        // ==================== TITLE SECTION ====================
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(10, 50, 20, 50));

        JLabel titleLabel = new JLabel("Main Menu");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Welcome to Affinity Finder");
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titlePanel.add(subtitleLabel);
        
        mainPanel.add(titlePanel);

        // ==================== BUTTONS WRAPPER PANEL (Centers buttons vertically) ====================
        JPanel buttonsWrapperPanel = new JPanel();
        buttonsWrapperPanel.setLayout(new BoxLayout(buttonsWrapperPanel, BoxLayout.Y_AXIS));
        buttonsWrapperPanel.setOpaque(false);

        // ==================== BUTTONS PANEL ====================
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Wrapper para centrar horizontalmente
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);

        // Botones de opciones
        btnForm = createMenuButton("📋 Form Management", "Create and manage your forms");
        btnForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnForm.addActionListener(e -> {
            new FormMenuView(controller).setVisible(true);
            dispose();
        });

        btnAnswer = createMenuButton("✎ Answer Form", "Complete forms and share your thoughts");
        btnAnswer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAnswer.addActionListener(e -> {
            new SelectFormView(controller, 
                SelectFormView.FormType.EXISTING, 
                SelectFormView.FormAction.ANSWER,
                () -> new MainMenuView(controller).setVisible(true)
            ).setVisible(true);
            dispose();
        });

        btnAffinity = createMenuButton("🔗 Affinity Groups", "View your affinity connections");
        btnAffinity.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAffinity.addActionListener(e -> {
            if (controller.getController().getAnsweredFormsInfo().isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have not answered any forms yet.", "No Answered Forms", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            new SelectFormView(controller, 
                SelectFormView.FormType.ANSWERED, 
                SelectFormView.FormAction.VIEW_AFFINITY,
                () -> new MainMenuView(controller).setVisible(true)
            ).setVisible(true);
            dispose();
        });

        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(btnForm);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonsPanel.add(btnAnswer);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonsPanel.add(btnAffinity);

        centerWrapper.add(buttonsPanel, BorderLayout.CENTER);
        buttonsWrapperPanel.add(centerWrapper);
        buttonsWrapperPanel.add(Box.createVerticalGlue());

        mainPanel.add(buttonsWrapperPanel);

        add(mainPanel);
    }

    /**
     * Creates a menu button with icon and description
     */
    private JButton createMenuButton(String title, String description) {
        JButton btn = new JButton("<html><b>" + title + "</b><br><small>" + description + "</small></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con degradado sutil
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), UIComponents.LIGHT_GREEN);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Borde
                g2d.setColor(UIComponents.DARK_GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

                // Efecto hover
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(76, 175, 80, 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        btn.setForeground(UIComponents.DARK_GREEN);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(280, 80));
        btn.setMaximumSize(new Dimension(280, 80));
        btn.setBorder(null);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        return btn;
    }

    /**
     * Creates the profile button (icon at the top left)
     */
    private JButton createProfileButton() {
        JButton btn = new JButton("👤") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Círculo de fondo
                GradientPaint gp = new GradientPaint(0, 0, UIComponents.ACCENT_GREEN, 0, getHeight(), UIComponents.DARK_GREEN);
                g2d.setPaint(gp);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Efecto hover
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(50, 50));
        btn.setMaximumSize(new Dimension(50, 50));
        btn.setBorder(null);

        btn.addActionListener(e -> {
            new ProfileMenuView(controller).setVisible(true);
            dispose();
        });

        return btn;
    }

    /**
     * Creates the exit button (door)
     */
    private JButton createExitButton() {
        JButton btn = new JButton("🚪") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Círculo de fondo
                GradientPaint gp = new GradientPaint(0, 0, UIComponents.ACCENT_GREEN, 0, getHeight(), UIComponents.DARK_GREEN);
                g2d.setPaint(gp);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Efecto hover
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(50, 50));
        btn.setMaximumSize(new Dimension(50, 50));
        btn.setBorder(null);

        btn.addActionListener(e -> {
            controller.inicializeController(null);
            new InitialMenuView(controller).setVisible(true);
            dispose();
        });

        return btn;
    }
}