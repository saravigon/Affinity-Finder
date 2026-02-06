package presentation.classes.admin;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Presentation class for executing K-Means clustering on a form.
 * Features modern card-based design with gradient background.
 */
public class ExecuteKmeansView extends JFrame {

    /**
     * Attribute: reference to PresentationController
     */
    private PresentationController controller;

    /**
     * Main panel with gradient
     */
    private JPanel mainPanel;

    /**
     * Attribute: text field for form ID
     */
    private JTextField fieldFormID;

    /**
     * Attribute: text field for k clusters (optional)
     */
    private JTextField fieldKClusters;

    /**
     * Attribute: execute button
     */
    private JButton btnExecute;

    /**
     * Attribute: back button
     */
    private JButton btnBack;

    /**
     * Constructor
     * @param controller PresentationController reference
     */
    public ExecuteKmeansView(PresentationController controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Execute K-Means", 700, 700);

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
        contentCard.setMaximumSize(new Dimension(600, 500));
        contentCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Execute K-Means Clustering");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentCard.add(titleLabel);

        contentCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Generate affinity groups from form responses");
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentCard.add(subtitleLabel);

        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(UIComponents.LIGHT_GREEN);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.ACCENT_GREEN, 2, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        infoPanel.setMaximumSize(new Dimension(520, 100));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoIcon = new JLabel("ℹ️ About K-Means");
        infoIcon.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        infoIcon.setForeground(UIComponents.DARK_GREEN);
        infoIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoIcon);
        
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel infoText = new JLabel("<html>K-Means will automatically determine the optimal number<br>of clusters using the Elbow Method.</html>");
        infoText.setFont(UIComponents.TEXT_FONT);
        infoText.setForeground(Color.DARK_GRAY);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoText);

        contentCard.add(infoPanel);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form ID input section
        JPanel formIDSection = new JPanel();
        formIDSection.setLayout(new BoxLayout(formIDSection, BoxLayout.Y_AXIS));
        formIDSection.setBackground(Color.WHITE);
        formIDSection.setOpaque(false);
        formIDSection.setMaximumSize(new Dimension(520, 80));
        formIDSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFormID = new JLabel("Form ID:");
        lblFormID.setFont(UIComponents.STANDARD_FONT);
        lblFormID.setForeground(UIComponents.DARK_GREEN);
        lblFormID.setAlignmentX(Component.LEFT_ALIGNMENT);
        formIDSection.add(lblFormID);

        formIDSection.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldFormID = new JTextField();
        fieldFormID.setFont(UIComponents.TEXT_FONT);
        fieldFormID.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        fieldFormID.setMaximumSize(new Dimension(520, 40));
        fieldFormID.setAlignmentX(Component.LEFT_ALIGNMENT);
        formIDSection.add(fieldFormID);

        contentCard.add(formIDSection);
        contentCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // K Clusters input section (optional)
        JPanel kClustersSection = new JPanel();
        kClustersSection.setLayout(new BoxLayout(kClustersSection, BoxLayout.Y_AXIS));
        kClustersSection.setBackground(Color.WHITE);
        kClustersSection.setOpaque(false);
        kClustersSection.setMaximumSize(new Dimension(520, 80));
        kClustersSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblKClusters = new JLabel("K Clusters (optional - leave empty for automatic):");
        lblKClusters.setFont(UIComponents.STANDARD_FONT);
        lblKClusters.setForeground(UIComponents.DARK_GREEN);
        lblKClusters.setAlignmentX(Component.LEFT_ALIGNMENT);
        kClustersSection.add(lblKClusters);

        kClustersSection.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldKClusters = new JTextField();
        fieldKClusters.setFont(UIComponents.TEXT_FONT);
        fieldKClusters.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        fieldKClusters.setMaximumSize(new Dimension(520, 40));
        fieldKClusters.setAlignmentX(Component.LEFT_ALIGNMENT);
        kClustersSection.add(fieldKClusters);

        contentCard.add(kClustersSection);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Execute button
        btnExecute = createExecuteButton();
        btnExecute.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExecute.addActionListener(e -> executeKmeans());
        contentCard.add(btnExecute);

        mainPanel.add(contentCard);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates the execute button with custom styling
     */
    private JButton createExecuteButton() {
        JButton btn = new JButton("🔄 Execute K-Means") {
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
        btn.setPreferredSize(new Dimension(260, 50));
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setBorder(null);

        return btn;
    }

    /**
     * Executes the K-Means clustering and shows the results.
     */
    private void executeKmeans() {
        String f = fieldFormID.getText().trim();
        String aux = fieldKClusters.getText().trim().replaceAll("[^0-9]", "");;
        Integer k = aux.isEmpty() ? null : Integer.parseInt(aux);

        if (f.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a Form ID.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int formID = Integer.parseInt(f);

            // Show loading message
            JOptionPane.showMessageDialog(this,
                "Executing K-Means clustering... This may take a moment.",
                "Processing",
                JOptionPane.INFORMATION_MESSAGE);

            ArrayList<ArrayList<String>> result = controller.getController().executeKmeans(formID, k);
            showKMeansResults(result, formID);
            controller.getController().chartAffinityGroups(formID);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Form ID must be a valid integer.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Execution Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays modern results window with K-Means clustering results
     */
    private void showKMeansResults(ArrayList<ArrayList<String>> result, int formID) {
        JFrame resultFrame = new JFrame("K-Means Results");
        UIComponents.configureWindow(resultFrame, "K-Means Results", 700, 600);

        // Main panel with gradient
        JPanel mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel("K-Means Clustering Results");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Form ID and cluster count
        JLabel infoLabel = new JLabel("Form ID: " + formID + " | Clusters: " + result.size());
        infoLabel.setFont(UIComponents.STANDARD_FONT);
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(infoLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Results panel (scrollable)
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < result.size(); i++) {
            JPanel clusterCard = createClusterCard(i + 1, result.get(i));
            resultsPanel.add(clusterCard);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIComponents.DARK_GREEN, 2, true));
        scrollPane.setMaximumSize(new Dimension(620, 400));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scrollPane);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

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
     * Creates a card for a single cluster
     */
    private JPanel createClusterCard(int clusterNum, ArrayList<String> clusterData) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.ACCENT_GREEN, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(580, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cluster title
        JLabel titleLabel = new JLabel("Cluster " + clusterNum);
        titleLabel.setFont(UIComponents.STANDARD_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Representative
        if (!clusterData.isEmpty()) {
            JLabel repLabel = new JLabel("👤 Representative: " + clusterData.get(0));
            repLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
            repLabel.setForeground(UIComponents.ACCENT_GREEN);
            repLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(repLabel);

            card.add(Box.createRigidArea(new Dimension(0, 8)));

            // Members
            if (clusterData.size() > 1) {
                JLabel membersTitle = new JLabel("Members (" + (clusterData.size() - 1) + "):");
                membersTitle.setFont(UIComponents.SMALL_FONT);
                membersTitle.setForeground(Color.GRAY);
                membersTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.add(membersTitle);

                card.add(Box.createRigidArea(new Dimension(0, 5)));

                for (int j = 1; j < clusterData.size(); j++) {
                    JLabel memberLabel = new JLabel("  • " + clusterData.get(j));
                    memberLabel.setFont(UIComponents.TEXT_FONT);
                    memberLabel.setForeground(Color.DARK_GRAY);
                    memberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    card.add(memberLabel);
                }
            } else {
                JLabel noMembers = new JLabel("No additional members");
                noMembers.setFont(UIComponents.ITALIC_FONT);
                noMembers.setForeground(Color.GRAY);
                noMembers.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.add(noMembers);
            }
        }

        return card;
    }
}
