package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.UIComponents;
import domain.exceptions.FormException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Presentation class for importing a form from a JSON file.
 * Beautiful modern UI with gradient background and styled components.
 */
public class ImportFormView extends JFrame {

    /**
     * Reference to PresentationController
     */
    private PresentationController controller;
    /**
     * Action to perform when going back
     */
    private Runnable goBackAction;
    /**
     * Main panel, which holds all components
     */
    private JPanel mainPanel;
    /**
     * Path input field 
     */
    private JTextField pathField;
    /**
     * Browse button to select file
     */
    private JButton btnBrowse;
    /**
     * Import button to import the form
     */
    private JButton btnImport;
    /**
     * Back button to return to previous view
     * Determined by goBackAction
     */
    private JButton btnBack;
    /**
     * Label to display selected file name
     */
    private JLabel fileNameLabel;

    /**
     * Constructor
     * @param controller PresentationController reference
     * @param goBackAction Action to perform when returning (cannot be null)
     */
    public ImportFormView(PresentationController controller, Runnable goBackAction) {
        this.controller = controller;
        this.goBackAction = goBackAction;
        initComponents();
    }

    /**
     * Initializes all components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "IMPORT FORM", 700, 700);

        // Main panel with gradient background
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            if (goBackAction != null) goBackAction.run();
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Title
        JLabel titleLabel = new JLabel("Import Form from JSON");
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Select a JSON file containing form data");
        subtitleLabel.setFont(UIComponents.ITALIC_FONT);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Content card
        JPanel contentCard = createContentCard();
        mainPanel.add(contentCard);
        
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates the main content card with file selection
     */
    private JPanel createContentCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 1, true),
            new EmptyBorder(40, 30, 30, 30)  // Increased top padding from 30 to 40
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(500, 350));

        // Add some space at the top
        //card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Icon
        JLabel iconLabel = new JLabel("📁");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // File selection label
        JLabel selectLabel = new JLabel("Select JSON File");
        selectLabel.setFont(UIComponents.STANDARD_FONT);
        selectLabel.setForeground(Color.DARK_GRAY);
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(selectLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // File name display (initially empty)
        fileNameLabel = new JLabel("No file selected");
        fileNameLabel.setFont(UIComponents.ITALIC_FONT);
        fileNameLabel.setForeground(Color.GRAY);
        fileNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(fileNameLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Browse button
        btnBrowse = UIComponents.createStyledButton("Browse Files", 300, 45);
        btnBrowse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBrowse.addActionListener(e -> browseFile());
        card.add(btnBrowse);
        
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        separator.setMaximumSize(new Dimension(440, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(separator);
        
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        // Import button
        btnImport = UIComponents.createStyledButton("Import Form", 300, 45);
        btnImport.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImport.setEnabled(false); // Disabled until file is selected
        btnImport.addActionListener(e -> importForm());
        card.add(btnImport);

        return card;
    }

    /**
     * Opens a file chooser dialog to select the JSON file
     */
    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Form JSON File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Set current directory to user's home
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        // Add file filter for JSON files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".json");
            }
            
            @Override
            public String getDescription() {
                return "JSON Files (*.json)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            
            // Update UI
            fileNameLabel.setText(selectedFile.getName());
            fileNameLabel.setForeground(UIComponents.DARK_GREEN);
            fileNameLabel.setFont(UIComponents.STANDARD_FONT);
            
            // Store path (hidden field)
            pathField = new JTextField(path);
            
            // Enable import button
            btnImport.setEnabled(true);
        }
    }

    /**
     * Imports the form from the selected JSON file
     */
    private void importForm() {
        if (pathField == null || pathField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select a file first.",
                "No File Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String path = pathField.getText().trim();

        try {
            // Show loading message
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            btnImport.setEnabled(false);
            btnImport.setText("Importing...");

            // Import form
            controller.getController().importFormFromJson(path);

            // Success
            JOptionPane.showMessageDialog(this,
                "Form imported successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            goBackAction.run();
            dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error reading the file:\n" + ex.getMessage() +
                "\n\nPlease check the file path and format.",
                "File Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (FormException ex) {
            JOptionPane.showMessageDialog(this,
                "Error importing form:\n" + ex.getMessage(),
                "Import Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Unexpected error:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            // Restore UI
            setCursor(Cursor.getDefaultCursor());
            btnImport.setEnabled(true);
            btnImport.setText("Import Form");
        }
    }
}