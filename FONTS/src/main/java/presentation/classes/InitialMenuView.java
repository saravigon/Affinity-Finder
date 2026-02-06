package presentation.classes;

import presentation.controllers.PresentationController;
import presentation.classes.admin.AdminMenuView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Initial menu view with login and user creation options.
 * Modern UI with gradient background and styled components.
 */
public class InitialMenuView extends JFrame {
    /**
     * Reference to PresentationController
     */
    private PresentationController controller;

    /**
     * Username text field
     * Where the user inputs their username
     */
    private JTextField tfUsername;
    /**
     * Password field
     * Where the user inputs their password
     */
    private JPasswordField tfPassword;
    /**
     * Login button
     * Triggers the login process, validating user credentials
     */
    private JButton btnLogin;
    /**
     * Create user button
     * Triggers the user creation process, registering a new user. Then user needs to login with the new credentials
     */
    private JButton btnCreateUser;
    /**
     * Forgot password button
     * Triggers the password recovery process, contacting admin for reset
     */
    private JButton btnForgotPassword;
    /**
     * Logo label
     * Displays the application logo at the top of the menu
     */
    private JLabel logoLabel;
    /**
     * Main panel
     * Holds all components with a gradient background
     */
    private JPanel mainPanel;

    /**
     * Constructor
     * @param controller PresentationController reference
     */
    public InitialMenuView(PresentationController controller) {
        this.controller = controller;
        initComponents();
    }
    /**
     * Initializes all components
     */
    private void initComponents() {
        // Configurar ventana
        setTitle("Affinity Finder");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con fondo degradado
        mainPanel = new UIComponents.GradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(0, 50, 50, 50));

        // ==================== LOGO ====================
        logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadAndSetLogo("AffinityFinderLogoGreen.png", 200, 200);

        // ==================== CARD DE LOGIN ====================
        JPanel loginCard = createLoginCard();

        // ==================== BOTONES ACCIÓN ====================
        JPanel buttonsPanel = createButtonsPanel();

        // ==================== CONTENEDOR CENTRADO ====================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(loginCard);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(buttonsPanel);

        // ==================== MONTAJE ====================
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(centerPanel);
        mainPanel.add(Box.createVerticalGlue());

        setContentPane(mainPanel);
    }

    /**
     * Creates card panel for login form (username and password fields)
     */
    private JPanel createLoginCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIComponents.LIGHT_GREEN);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 2, true),
            new EmptyBorder(20, 35, 20, 35)
        ));
        card.setMaximumSize(new Dimension(350, 300));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label de descripción
        JLabel loginDescLabel = new JLabel("Enter your credentials to continue");
        loginDescLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        loginDescLabel.setForeground(new Color(80, 80, 80));
        loginDescLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(loginDescLabel);
        card.add(Box.createVerticalStrut(20));

        // ==================== USERNAME ====================
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        usernameLabel.setForeground(UIComponents.DARK_GREEN);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfUsername = new JTextField();
        tfUsername.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        tfUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfUsername.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));

        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(tfUsername);
        card.add(Box.createVerticalStrut(20));

        // ==================== PASSWORD ====================
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        passwordLabel.setForeground(UIComponents.DARK_GREEN);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfPassword = new JPasswordField();
        tfPassword.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        tfPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(tfPassword);
        card.add(Box.createVerticalStrut(15));

        // ==================== FORGOT PASSWORD ====================
        btnForgotPassword = new JButton("Forgot password?");
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setFocusPainted(false);
        btnForgotPassword.setOpaque(false);
        btnForgotPassword.setForeground(new Color(33, 150, 243)); // Azul
        btnForgotPassword.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.setMargin(new Insets(0, 0, 0, 0));
        btnForgotPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnForgotPassword.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "To reset your password, please contact with us\n" +
                "admin@affinityfinder.com",
                "Password Recovery",
                JOptionPane.INFORMATION_MESSAGE);
        });

        card.add(btnForgotPassword);

        return card;
    }

    /**
     * Creates the panel with action buttons
     */
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Botón Login
        btnLogin = createGreenButton("LOG IN", 200, 45);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> handleLogin());

        // Botón Crear Usuario
        btnCreateUser = createOutlineButton("CREATE NEW USER", 200, 45);
        btnCreateUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCreateUser.addActionListener(e -> handleCreateUser());

        panel.add(btnLogin);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnCreateUser);

        return panel;
    }

    /**
     * Handles the creation of a new user
     */
    private void handleCreateUser() {
        String username = tfUsername.getText().trim();
        String password = new String(tfPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controller.getController().createUser(username, password);
            JOptionPane.showMessageDialog(this, "Profile created successfully!");
            
            // Limpiar los campos
            tfUsername.setText("");
            tfPassword.setText("");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a button with a green gradient background
     */
    private JButton createGreenButton(String text, int width, int height) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Degradado verde
                GradientPaint gp = new GradientPaint(0, 0, UIComponents.ACCENT_GREEN, 0, getHeight(), UIComponents.DARK_GREEN);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Efecto hover
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(width, height));
        btn.setMaximumSize(new Dimension(width, height));
        btn.setBorder(null);

        return btn;
    }

    /**
     * Creates a button with an outline and transparent background
     */
    private JButton createOutlineButton(String text, int width, int height) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo semi-transparente
                g2d.setColor(new Color(245, 250, 245));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Borde
                g2d.setColor(UIComponents.DARK_GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                // Efecto hover
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(76, 175, 80, 30));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        btn.setForeground(UIComponents.DARK_GREEN);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(width, height));
        btn.setMaximumSize(new Dimension(width, height));
        btn.setBorder(null);

        return btn;
    }

    /**
     * Handles the login event
     */
    private void handleLogin() {
        String username = tfUsername.getText().trim();
        String password = new String(tfPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controller.getController().logIn(username, password);
            JOptionPane.showMessageDialog(this, "Logged in successfully!");

            if (username.equals("admin")) {
                new AdminMenuView(controller).setVisible(true);
                dispose();
                return;
            }

            int id = controller.getController().getUUIDbyUsername(username);
            controller.inicializeController(id);
            new MainMenuView(controller).setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads the logo from resources
     */
    private void loadAndSetLogo(String path, int width, int height) {
        try {
            // Intenta primero desde el classpath (para JAR)
            java.net.URL imgURL = getClass().getResource("/" + path);
            
            if (imgURL == null) {
                // Si no encuentra, intenta con ClassLoader
                imgURL = getClass().getClassLoader().getResource(path);
            }
            
            if (imgURL == null) {
                // Último intento: ruta relativa desde el proyecto
                String projectPath = System.getProperty("user.dir");
                java.io.File imageFile = new java.io.File(projectPath + "/src/main/resources/" + path);
                
                if (imageFile.exists()) {
                    imgURL = imageFile.toURI().toURL();
                }
            }

            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image originalImage = originalIcon.getImage();
                Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(resizedImage));
                System.out.println("Logo cargado desde: " + imgURL);
            } else {
                logoLabel.setText("AF");
                logoLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
                logoLabel.setForeground(UIComponents.ACCENT_GREEN);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
            e.printStackTrace();
            logoLabel.setText("AF");
            logoLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
            logoLabel.setForeground(UIComponents.ACCENT_GREEN);
        }
    }

}