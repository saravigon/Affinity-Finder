package presentation.classes.affinity;

import presentation.controllers.PresentationController;
import presentation.classes.*;
import presentation.classes.form.SelectFormView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Affinity Group details view.
 * Shows the affinity group information including the form title, representative and members.
 */
public class AffinityMenuView extends JFrame {
    
    /**
     * Presentation controller reference.
     */
    private PresentationController controller;
    
    /**
     * Form ID associated with this affinity group
     */
    private Integer formID;

    /**
     * Main panel
     */
    private JPanel mainPanel;

    /**
     * Top panel (Back button)
     */
    private JPanel topPanel;

    /**
     * Center panel (Affinity group info)
     */
    private JPanel centerPanel;

    /**
     * Back button
     */
    private JButton btnBack;

    /**
     * Export Results button
     */
    private JButton btnExportResults;

    /**
     * Constructor
     * @param controller Presentation controller
     * @param formID Form ID for the affinity group
     */
    public AffinityMenuView(PresentationController controller, Integer formID) {
        this.controller = controller;
        this.formID = formID;
        initComponents();
    }

    /**
     * Initialize GUI components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "AFFINITY GROUP", 700, 700);

        // Main Panel with Gradient Background
        mainPanel = UIComponents.createGradientPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 40, 40));

        // Top Panel (Back button)
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        
        btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new SelectFormView(controller, 
                SelectFormView.FormType.CREATED, 
                SelectFormView.FormAction.VIEW_AFFINITY,
                () -> new MainMenuView(controller).setVisible(true)
            ).setVisible(true);
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Center Panel (Affinity group information) - CENTERED
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Get form title and description
        String formTitle;
        String formDescription;
        try {
            formTitle = controller.getController().getForm(formID).getTitle();
            formDescription = controller.getController().getForm(formID).getDescription();
        } catch (Exception e) {
            formTitle = "N/A";
            formDescription = "N/A";
        }

        // Form Info Panel (with border) - CENTERED
        JPanel formInfoPanel = new JPanel();
        formInfoPanel.setLayout(new BoxLayout(formInfoPanel, BoxLayout.Y_AXIS));
        formInfoPanel.setBackground(new Color(245, 250, 245));
        formInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 2, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        formInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formInfoPanel.setMaximumSize(new Dimension(580, 150));

        JLabel titleLabel = UIComponents.createCenterLabel("Affinity Group");
        formInfoPanel.add(titleLabel);
        
        formInfoPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel formTitleLabel = UIComponents.createCenterLabel(formTitle);
        formInfoPanel.add(formTitleLabel);
        
        formInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel formDescLabel = new JLabel("<html><center><i>" + formDescription + "</i></center></html>");
        formDescLabel.setFont(UIComponents.ITALIC_FONT);
        formDescLabel.setForeground(Color.DARK_GRAY);
        formDescLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formInfoPanel.add(formDescLabel);

        centerPanel.add(formInfoPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Representative section - CENTERED
        JLabel representativeTitle = new JLabel("Representative");
        representativeTitle.setFont(UIComponents.STANDARD_FONT);
        representativeTitle.setForeground(Color.DARK_GRAY);
        representativeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(representativeTitle);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        Integer representativeID = controller.getController().getActiveAffinityGroup().getRepresentativeID();
        String representativeName;
        try { 
            representativeName = controller.getController().getProfileByID(representativeID).getUsername(); 
        } catch (Exception e) { 
            representativeName = "N/A"; 
        }

        JPanel representativeBox = UIComponents.createClickableUserBox(representativeName, representativeID, formID, controller, this);
        centerPanel.add(representativeBox);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Members section - CENTERED
        JLabel membersTitle = new JLabel("Members (" + controller.getController().getActiveAffinityGroup().getMemberIDs().size() + ")");
        membersTitle.setFont(UIComponents.STANDARD_FONT);
        membersTitle.setForeground(Color.DARK_GRAY);
        membersTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(membersTitle);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        List<Integer> membersIDs = controller.getController().getActiveAffinityGroup().getMemberIDs();
        
        if (membersIDs.isEmpty()) {
            JLabel noMembersLabel = new JLabel("No members found");
            noMembersLabel.setFont(UIComponents.SMALL_FONT);
            noMembersLabel.setForeground(Color.GRAY);
            noMembersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(noMembersLabel);
        } else {
            // Create a scrollable list for members
            JPanel membersPanel = new JPanel();
            membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
            membersPanel.setBackground(Color.WHITE);

            for (Integer memberID : membersIDs) {
                String memberName;
                try { 
                    memberName = controller.getController().getProfileByID(memberID).getUsername(); 
                } catch (Exception e) { 
                    memberName = "N/A"; 
                }

                JPanel memberBox = UIComponents.createClickableUserBox(memberName, memberID, formID, controller, this);
                membersPanel.add(memberBox);
                membersPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }

            JScrollPane scrollPane = new JScrollPane(membersPanel);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
            scrollPane.setMaximumSize(new Dimension(580, 250));
            scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            scrollPane.setBackground(Color.WHITE);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            centerPanel.add(scrollPane);
        }

        mainPanel.add(centerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Export Results button
        btnExportResults = UIComponents.createStyledButton("Export Results", 200, 40);
        btnExportResults.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExportResults.addActionListener(e -> {
            try {
                // Open file chooser
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Export Results as JSON");
                String frmTitle = controller.getController().getForm(formID).getTitle();
                fileChooser.setSelectedFile(new java.io.File("affinity_results_" + frmTitle + ".json"));
                
                // Set file filter to JSON
                javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("JSON files (*.json)", "json");
                fileChooser.setFileFilter(filter);
                
                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    // Ensure .json extension
                    if (!filePath.endsWith(".json")) {
                        filePath += ".json";
                    }
                    controller.getController().exportFormResults(formID, filePath);
                    JOptionPane.showMessageDialog(this, "Results exported successfully to:\n" + filePath, "Export", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting results: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(btnExportResults);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }  
}