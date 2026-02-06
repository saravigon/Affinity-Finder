package presentation.classes.admin;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Presentation class for evaluating K-Means clustering on a form.
 * Features modern card-based design with gradient background.
 */
public class EvaluateKmeansView extends JFrame {

    /**
     * Attribute: reference to PresentationController
     */
    private PresentationController controller;

    /**
     * Main panel with gradient
     */
    private JPanel mainPanel;

    /**
     * Attribute: Text field for Form ID input
     */
    private JTextField fieldFormID;

    /**
     * Attribute: Text field for K value input (optional)
     */
    private JTextField fieldK;

    /**
     * Attribute: Execute button
     */
    private JButton btnExecute;

    /**
     * Attribute: Back button
     */
    private JButton btnBack;

    /**
     * Constructor
     * @param controller PresentationController reference
     */
    public EvaluateKmeansView(PresentationController controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * Initializes all components and layout
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Evaluate K-Means", 700, 700);

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
        contentCard.setMaximumSize(new Dimension(600, 450));
        contentCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Evaluate K-Means Clustering");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentCard.add(titleLabel);

        contentCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Analyze clustering quality with Silhouette Score");
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentCard.add(subtitleLabel);

        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Info panel - About process
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(UIComponents.LIGHT_GREEN);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.ACCENT_GREEN, 2, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        infoPanel.setMaximumSize(new Dimension(520, 200));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoIcon = new JLabel("⚠️ Important: Re-execution Required");
        infoIcon.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        infoIcon.setForeground(UIComponents.DARK_GREEN);
        infoIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoIcon);
        
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel infoText = new JLabel("<html>Before evaluating, the K-Means algorithm will be<br>re-executed on your form.</html>");
        infoText.setFont(UIComponents.TEXT_FONT);
        infoText.setForeground(Color.DARK_GRAY);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoText);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        contentCard.add(infoPanel);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Input section
        JPanel inputSection = new JPanel();
        inputSection.setLayout(new BoxLayout(inputSection, BoxLayout.Y_AXIS));
        inputSection.setBackground(Color.WHITE);
        inputSection.setOpaque(false);
        inputSection.setMaximumSize(new Dimension(520, 180));
        inputSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFormID = new JLabel("Form ID:");
        lblFormID.setFont(UIComponents.STANDARD_FONT);
        lblFormID.setForeground(UIComponents.DARK_GREEN);
        lblFormID.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(lblFormID);

        inputSection.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldFormID = new JTextField();
        fieldFormID.setFont(UIComponents.TEXT_FONT);
        fieldFormID.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        fieldFormID.setMaximumSize(new Dimension(520, 40));
        fieldFormID.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(fieldFormID);

        inputSection.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblK = new JLabel("K Clusters (optional - leave empty for automatic):");
        lblK.setFont(UIComponents.STANDARD_FONT);
        lblK.setForeground(UIComponents.DARK_GREEN);
        lblK.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(lblK);

        inputSection.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldK = new JTextField();
        fieldK.setFont(UIComponents.TEXT_FONT);
        fieldK.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        fieldK.setMaximumSize(new Dimension(520, 40));
        fieldK.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputSection.add(fieldK);

        contentCard.add(inputSection);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Execute button
        btnExecute = createEvaluateButton();
        btnExecute.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExecute.addActionListener(e -> evaluateKmeans());
        contentCard.add(btnExecute);

        mainPanel.add(contentCard);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates the evaluate button with custom styling
     */
    private JButton createEvaluateButton() {
        JButton btn = new JButton("📊 Evaluate Clustering") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Green gradient background
                GradientPaint gp = new GradientPaint(0, 0, UIComponents.ACCENT_GREEN, 0, getHeight(), UIComponents.DARK_GREEN);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Border
                g2d.setColor(UIComponents.DARK_GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                // Hover effect
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(UIComponents.STANDARD_FONT);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(280, 50));
        btn.setMaximumSize(new Dimension(280, 50));
        btn.setBorder(null);

        return btn;
    }

    /**
     * Handles execution of K-Means evaluation
     */
    private void evaluateKmeans() {
        String f = fieldFormID.getText().trim();
        String k = fieldK.getText().trim();

        if (f.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a Form ID.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int formID = Integer.parseInt(f);
            Integer kValue = null;
            
            if (!k.isEmpty()) {
                try {
                    kValue = Integer.parseInt(k);
                    if (kValue <= 0) {
                        JOptionPane.showMessageDialog(this,
                            "K value must be greater than 0.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                        "K value must be a valid integer.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            JOptionPane.showMessageDialog(this,
                "Re-executing K-Means algorithm...\nThis may take a moment.",
                "Processing",
                JOptionPane.INFORMATION_MESSAGE);
            

            double silhouette = controller.getController().evaluateCluster(formID, kValue);

            showEvaluateResults(silhouette, formID);

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Form ID must be a valid integer.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Evaluation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays a modern results window with the silhouette score
     */
    private void showEvaluateResults(double silhouette, int formID) {
        JFrame resultFrame = new JFrame("Evaluation Results");
        UIComponents.configureWindow(resultFrame, "Evaluation Results", 600, 500);

        // Main panel with gradient
        JPanel mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Title
        JLabel titleLabel = new JLabel("K-Means Evaluation Results");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Results card
        JPanel resultsCard = new JPanel();
        resultsCard.setLayout(new BoxLayout(resultsCard, BoxLayout.Y_AXIS));
        resultsCard.setBackground(Color.WHITE);
        resultsCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 2, true),
            new EmptyBorder(25, 30, 25, 30)
        ));
        resultsCard.setMaximumSize(new Dimension(500, 300));
        resultsCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form ID
        JLabel formIDLabel = new JLabel("Form ID: " + formID);
        formIDLabel.setFont(UIComponents.STANDARD_FONT);
        formIDLabel.setForeground(Color.GRAY);
        formIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCard.add(formIDLabel);

        resultsCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // Silhouette score
        JLabel scoreLabel = new JLabel(String.format("%.4f", silhouette));
        scoreLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
        scoreLabel.setForeground(getScoreColor(silhouette));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCard.add(scoreLabel);

        resultsCard.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel scoreTitle = new JLabel("Silhouette Score");
        scoreTitle.setFont(UIComponents.STANDARD_FONT);
        scoreTitle.setForeground(Color.GRAY);
        scoreTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCard.add(scoreTitle);

        resultsCard.add(Box.createRigidArea(new Dimension(0, 25)));

        // Interpretation
        JLabel interpretation = new JLabel(getInterpretation(silhouette));
        interpretation.setFont(UIComponents.TEXT_FONT);
        interpretation.setForeground(UIComponents.DARK_GREEN);
        interpretation.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCard.add(interpretation);

        mainPanel.add(resultsCard);
        mainPanel.add(Box.createVerticalGlue());

        // Close button
        JButton btnClose = UIComponents.createStyledButton("Close", 150, 45);
        btnClose.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnClose.addActionListener(e -> {
            new AdminMenuView(controller).setVisible(true);
            resultFrame.dispose();
        });
        mainPanel.add(btnClose);

        resultFrame.add(mainPanel);
        resultFrame.setVisible(true);
    }

    /**
     * Returns a color based on the silhouette score quality
     */
    private Color getScoreColor(double score) {
        if (score >= 0.7) return new Color(76, 175, 80); // Green - excellent
        if (score >= 0.5) return new Color(156, 204, 101); // Light green - good
        if (score >= 0.25) return new Color(255, 152, 0); // Orange - fair
        return new Color(244, 67, 54); // Red - poor
    }

    /**
     * Returns interpretation text based on silhouette score
     */
    private String getInterpretation(double score) {
        if (score >= 0.7) return "Excellent clustering quality";
        if (score >= 0.5) return "Good clustering structure";
        if (score >= 0.25) return "Fair clustering with some overlap";
        if (score >= 0) return "Weak clustering structure";
        return "Poor clustering with significant overlap";
    }
}
