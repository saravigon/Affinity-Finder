package presentation.classes.admin;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import domain.exceptions.FormException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Presentation class for importing many users' answers from CSV.
 * Features modern card-based design with gradient background.
 */
public class ImportManyAnswersView extends JFrame {

    /**
     * Attribute: reference to the controller
     */
    private PresentationController controller;

    /**
     * Main panel with gradient
     */
    private JPanel mainPanel;

    /**
     * Attribute: file path input
     */
    private JTextField fieldFilePath;

    /**
     * Attribute: import button
     */
    private JButton btnImport;

    /**
     * Attribute: back button
     */
    private JButton btnBack;

    /**
     * Attribute: browse button
     */
    private JButton btnBrowse;

    /**
     * Attribute: UFID of the form managed
     */
    private int ufid;

    /**
     * Constructor
     * @param controller PresentationController reference
     * @param ufid UFID of the form managed
     */
    public ImportManyAnswersView(PresentationController controller, int ufid) {
        this.controller = controller;
        this.ufid = ufid;
        initComponents();
    }

    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Import Answers", 700, 700);

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
        JLabel titleLabel = new JLabel("Import Answers from CSV");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentCard.add(titleLabel);

        contentCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Bulk import form responses from CSV file");
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
        infoPanel.setMaximumSize(new Dimension(520, 120));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoIcon = new JLabel("ℹ️ CSV Format");
        infoIcon.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        infoIcon.setForeground(UIComponents.DARK_GREEN);
        infoIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoIcon);
        
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel infoText = new JLabel("<html>The CSV file should contain user answers with proper<br>formatting. Each row represents one user's responses.</html>");
        infoText.setFont(UIComponents.TEXT_FONT);
        infoText.setForeground(Color.DARK_GRAY);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoText);

        contentCard.add(infoPanel);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form ID display (if available)
        if (ufid != -1) {
            JPanel formIDPanel = new JPanel();
            formIDPanel.setLayout(new BoxLayout(formIDPanel, BoxLayout.Y_AXIS));
            formIDPanel.setBackground(new Color(240, 248, 255));
            formIDPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 149, 237), 1, true),
                new EmptyBorder(10, 15, 10, 15)
            ));
            formIDPanel.setMaximumSize(new Dimension(520, 50));
            formIDPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel formIDLabel = new JLabel("Target Form ID: " + ufid);
            formIDLabel.setFont(UIComponents.STANDARD_FONT);
            formIDLabel.setForeground(new Color(70, 130, 180));
            formIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            formIDPanel.add(formIDLabel);

            contentCard.add(formIDPanel);
            contentCard.add(Box.createRigidArea(new Dimension(0, 25)));
        }

        // File selection section
        JPanel fileSection = new JPanel();
        fileSection.setLayout(new BoxLayout(fileSection, BoxLayout.Y_AXIS));
        fileSection.setBackground(Color.WHITE);
        fileSection.setOpaque(false);
        fileSection.setMaximumSize(new Dimension(520, 100));
        fileSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFile = new JLabel("Select CSV File:");
        lblFile.setFont(UIComponents.STANDARD_FONT);
        lblFile.setForeground(UIComponents.DARK_GREEN);
        lblFile.setAlignmentX(Component.LEFT_ALIGNMENT);
        fileSection.add(lblFile);

        fileSection.add(Box.createRigidArea(new Dimension(0, 8)));

        // File path panel with text field and browse button
        JPanel filePathPanel = new JPanel(new BorderLayout(8, 0));
        filePathPanel.setBackground(Color.WHITE);
        filePathPanel.setOpaque(false);
        filePathPanel.setMaximumSize(new Dimension(520, 40));
        filePathPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldFilePath = new JTextField();
        fieldFilePath.setFont(UIComponents.TEXT_FONT);
        fieldFilePath.setEditable(false);
        fieldFilePath.setBackground(new Color(245, 245, 245));
        fieldFilePath.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        filePathPanel.add(fieldFilePath, BorderLayout.CENTER);

        btnBrowse = createBrowseButton();
        btnBrowse.addActionListener(e -> browseFile());
        filePathPanel.add(btnBrowse, BorderLayout.EAST);

        fileSection.add(filePathPanel);

        contentCard.add(fileSection);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Import button
        btnImport = createImportButton();
        btnImport.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImport.addActionListener(e -> importAnswersManyUsers());
        contentCard.add(btnImport);

        mainPanel.add(contentCard);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates the browse button with custom styling
     */
    private JButton createBrowseButton() {
        JButton btn = new JButton("📁 Browse") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gray gradient background
                Color grayColor = new Color(120, 120, 120);
                Color darkGray = new Color(90, 90, 90);
                
                GradientPaint gp;
                if (getModel().isRollover()) {
                    gp = new GradientPaint(0, 0, darkGray, 0, getHeight(), grayColor);
                } else {
                    gp = new GradientPaint(0, 0, grayColor, 0, getHeight(), darkGray);
                }
                
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Border
                g2d.setColor(darkGray);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setFont(UIComponents.SMALL_FONT);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBorder(null);

        return btn;
    }

    /**
     * Creates the import button with custom styling
     */
    private JButton createImportButton() {
        JButton btn = new JButton("📥 Import Answers") {
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
     * Opens a file chooser dialog to select the CSV file
     */
    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Answers CSV File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Optional: Add file filter for CSV files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }
            
            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            fieldFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Handles the import of answers from CSV for many users
     */
    private void importAnswersManyUsers() {

        //String f = fieldFormID.getText().trim();
        String p = fieldFilePath.getText().trim();

        try {
            //int formID = Integer.parseInt(f);

            HashMap<Integer, ArrayList<String>> answersByUser = controller.getController().importAnswersFromCsv(ufid, p);

            controller.getController().processImportedAnswers(ufid, answersByUser);

            JOptionPane.showMessageDialog(this, "Answers imported successfully!");

            dispose();

        } //catch (NumberFormatException ex) {
            //JOptionPane.showMessageDialog(this, "Form ID must be an integer.");

        //}
        catch (IOException ex) {
             JOptionPane.showMessageDialog(this, "Error reading the file: " + ex.getMessage());

        }
        catch (FormException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}