package presentation.classes.form;

import presentation.controllers.PresentationController;
import presentation.classes.MainMenuView;
import presentation.classes.UIComponents;
import domain.classes.Answer;
import domain.exceptions.FormException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * View for answering a form.
 * Fixed layout issues: No horizontal scroll, starts at top.
 */
public class AnswerFormView extends JFrame {

    /**
     * Reference to the PresentationController
     */
    private PresentationController controller;
    /**
     * UFID of the form to answer
     * Used to fetch questions and submit answers
     */
    private int ufid;
    /**
     * Main panel containing all components
     */
    private JPanel mainPanel;
    /**
     * Header panel for form title and description
     */
    private JPanel headerPanel;
    /**
     * Panel containing the questions
     */
    private JPanel questionsPanel;
    /**
     * Scroll pane for questions
     */
    private JScrollPane questionsScrollPane;
    /**
     * List of answer text areas
     */
    private ArrayList<JTextArea> answerComponents; 
    /**
     * Submit questions button
     * Allows user to submit their answers
     */
    private JButton btnSubmit;
    /**
     * Import answers button
     * Allows user to import answers from CSV file
     */
    private JButton btnImport;
    /**
     * Progress bar for answering progress
     * Shows percentage of questions answered
     */
    private JProgressBar progressBar;
    /**
     * Progress label
     */
    private JLabel progressLabel;
    /**
     * Title of the form used in the header
     */
    private String formTitle;
    /**
     * Description of the form used in the header
     */
    private String formDescription;
    /**
     * List of questions in the form
     */
    private List<String> questions;
    /**
     * Total number of questions in the form
     */
    private int totalQuestions = 0;
    /**
     * Flag to indicate if submission is in progress
     * (true = submitting, false = not submitting)
     */
    private boolean isSubmitting = false;
    /**
     * Flag indicating if the form is being edited
     * (true = editing existing answers, false = new answers)
     */
    private boolean isEditMode = false;
    /**
     * Action to perform when going back
     * (used in edit mode to return to previous view)
     */
    private Runnable goBackAction;
    /**
     * List of existing answers (for edit mode)
     * Used to pre-fill answer fields
     */
    private List<String> existingAnswers = new ArrayList<>();

    /**
     * Constructor for AnswerFormView.
     * @param controller Reference to the PresentationController
     * @param ufid UFID of the form to answer
     * @param isEditMode Flag indicating if the form is being edited
     * @param goBackAction Action to perform when going back
     */
    public AnswerFormView(PresentationController controller, int ufid, boolean isEditMode, Runnable goBackAction) {
        this.controller = controller;
        this.ufid = ufid;
        this.answerComponents = new ArrayList<>();
        this.isEditMode = isEditMode;
        this.goBackAction = goBackAction;
        initComponents();
        loadQuestions(); // Load and display questions
    }

    /**
     * Initializes the view components.
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Answer Form", 750, 750);

        // 1. MAIN PANEL
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // 2. TOP PANEL (Back Button)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBackground(Color.WHITE);
        
        JButton btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            new MainMenuView(controller);
            dispose();
        });
        topPanel.add(btnBack);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 3. HEADER PANEL
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(headerPanel);

        // 4. PROGRESS SECTION
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBackground(new Color(248, 249, 250));
        progressPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        progressPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        progressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressLabel = new JLabel("Progress: 0 / 0 questions answered");
        progressLabel.setFont(UIComponents.TEXT_FONT);
        progressLabel.setForeground(UIComponents.DARK_GREEN);
        progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressPanel.add(progressLabel);
        progressPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(UIComponents.DARK_GREEN);
        progressBar.setBackground(Color.WHITE);
        progressBar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressPanel.add(progressBar);

        mainPanel.add(progressPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // QUESTIONS PANEL 
        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(Color.WHITE);
        questionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        questionsScrollPane = new JScrollPane(questionsPanel);
        questionsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        questionsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        questionsScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        questionsScrollPane.setBackground(Color.WHITE);
        questionsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        questionsScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        questionsScrollPane.setPreferredSize(new Dimension(650, 350));
        
        mainPanel.add(questionsScrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // BUTTONS PANEL (Import and Submit)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Import button
        btnImport = new JButton("📥 Import Answers");
        btnImport.setFont(UIComponents.STANDARD_FONT);
        btnImport.setPreferredSize(new Dimension(200, 45));
        btnImport.setBackground(new Color(108, 117, 125)); // Gray color
        btnImport.setForeground(Color.WHITE);
        btnImport.setFocusPainted(false);
        btnImport.setBorderPainted(false);
        btnImport.setOpaque(true);
        btnImport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImport.addActionListener(e -> importAnswers());
        
        // Hover effect for import button
        btnImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnImport.setBackground(new Color(90, 98, 104));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnImport.setBackground(new Color(108, 117, 125));
            }
        });
        
        buttonsPanel.add(btnImport);

        // Submit button
        btnSubmit = UIComponents.createStyledButton(
            isEditMode ? "Update Answers" : "Submit Answers", 
            200, 
            45
        );
        btnSubmit.setEnabled(false); 
        btnSubmit.addActionListener(e -> submitAnswers());
        buttonsPanel.add(btnSubmit);

        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createVerticalGlue()); 

        setContentPane(mainPanel);
    }

    /**
     * Loads the questions from the controller and displays them.
     */
    private void loadQuestions() {
        try {
            // Load form metadata
            formTitle = controller.getController().getForm(ufid).getTitle();
            formDescription = controller.getController().getForm(ufid).getDescription();
            questions = controller.getController().getForm(ufid).getQuestionsText();
            
            totalQuestions = questions.size();

            // Update header
            displayHeader();

            // Display questions
            if (questions.isEmpty()) {
                JLabel emptyLabel = new JLabel("This form has no questions yet.");
                emptyLabel.setFont(UIComponents.TEXT_FONT);
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                questionsPanel.add(emptyLabel);
            } else {
                for (int i = 0; i < questions.size(); i++) {
                    String question = questions.get(i);
                    JPanel questionCard = createQuestionCard(i + 1, question);
                    questionsPanel.add(questionCard);
                    questionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
                btnSubmit.setEnabled(true);
                
                // Load existing answers if in edit mode
                if (isEditMode) {
                    loadExistingAnswers();
                }
            }

            updateProgress();
            questionsPanel.revalidate();
            questionsPanel.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading form: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads existing answers for the current user and form
     */
    private void loadExistingAnswers() {
        if (!isEditMode) return;
        
        try {
            int userId = controller.getController().getCurrentProfile().getUUID();
            Answer aux = controller.getController().getUserAnswers(ufid, userId);
            
            if (aux == null) return;

            existingAnswers = UIComponents.answerToStringList(aux);
            
            // Populate answer components with existing answers
            for (int i = 0; i < answerComponents.size(); i++) {
                if (i >= existingAnswers.size()) break;
                
                String answerText = existingAnswers.get(i);
                JTextArea area = answerComponents.get(i);
                
                if (answerText != null) {
                    area.setText(answerText);
                }
            }
            
            // Update progress after loading
            updateProgress();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading your previous answers: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Imports answers from a CSV file
     */
    private void importAnswers() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Answers CSV File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        // Add file filter for CSV files
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }
            
            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            
            try {
                // Show loading cursor
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                btnImport.setEnabled(false);
                btnImport.setText("Importing...");

                // Import answers from CSV using the controller method
                HashMap<Integer, ArrayList<String>> imported =
                        controller.getController().importAnswersFromCsvUserFixed(path);

                // Check if the current form is in the imported data
                if (!imported.containsKey(ufid)) {
                    JOptionPane.showMessageDialog(this,
                        "The selected CSV file does not contain answers for this form.\n" +
                        "Please select a file with answers for: " + formTitle,
                        "Form Not Found",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Get the answers for this specific form
                ArrayList<String> importedAnswers = imported.get(ufid);

                // Validate number of answers matches questions
                if (importedAnswers.size() != totalQuestions) {
                    JOptionPane.showMessageDialog(this,
                        "The imported file contains " + importedAnswers.size() + " answers,\n" +
                        "but this form has " + totalQuestions + " questions.\n\n" +
                        "Please select a file with matching answers.",
                        "Answer Count Mismatch",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Fill in the answer components
                for (int i = 0; i < answerComponents.size() && i < importedAnswers.size(); i++) {
                    JTextArea area = answerComponents.get(i);
                    String answer = importedAnswers.get(i);
                    area.setText(answer != null ? answer : "");
                }

                // Update progress
                updateProgress();

                JOptionPane.showMessageDialog(this,
                    "Answers imported successfully from CSV!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error reading the CSV file:\n" + ex.getMessage() +
                    "\n\nPlease check the file path and format.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (FormException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error importing answers:\n" + ex.getMessage(),
                    "Import Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Unexpected error importing answers:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } finally {
                // Restore UI
                setCursor(Cursor.getDefaultCursor());
                btnImport.setEnabled(true);
                btnImport.setText("📥 Import Answers");
            }
        }
    }

    /**
     * Displays the header with the form title and description.
     */
    private void displayHeader() {
        headerPanel.removeAll();

        // Title with edit indicator
        String titleText = isEditMode ? formTitle + " (Editing)" : formTitle;
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(UIComponents.TITLE_FONT);
        titleLabel.setForeground(UIComponents.DARK_GREEN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Description
        JLabel descLabel = new JLabel("<html><center><i>" + formDescription + "</i></center></html>");
        descLabel.setFont(UIComponents.ITALIC_FONT);
        descLabel.setForeground(Color.DARK_GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(descLabel);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Question count with edit note
        String countText = totalQuestions + " question" + (totalQuestions != 1 ? "s" : "");
        if (isEditMode) {
            countText += " • Your previous answers are loaded below";
        }
        JLabel countLabel = new JLabel(countText);
        countLabel.setFont(UIComponents.SMALL_FONT);
        countLabel.setForeground(Color.GRAY);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(countLabel);

        headerPanel.revalidate();
        headerPanel.repaint();
    }

    /**
     * Creates a card panel for a question with its number and text.
     * @param questionNumber The number of the question.
     * @param questionText The text of the question.
     * @return A JPanel representing the question card.
     */
    private JPanel createQuestionCard(int questionNumber, String questionText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(252, 252, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); 
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- HEADER DE LA PREGUNTA ---
        JPanel qHeader = new JPanel(new BorderLayout());
        qHeader.setBackground(new Color(252, 252, 252));
        qHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); 
        qHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel numberLabel = new JLabel("Q" + questionNumber + ". ");
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        numberLabel.setForeground(UIComponents.DARK_GREEN);
        numberLabel.setVerticalAlignment(SwingConstants.TOP); 

        // El texto ya viene con metadatos desde getQuestionText()
        JTextArea questionTextArea = new JTextArea(questionText);
        questionTextArea.setFont(UIComponents.TEXT_FONT);
        questionTextArea.setForeground(Color.BLACK);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setOpaque(false);
        questionTextArea.setFocusable(false);
        questionTextArea.setBorder(null);

        qHeader.add(numberLabel, BorderLayout.WEST);
        qHeader.add(questionTextArea, BorderLayout.CENTER);

        card.add(qHeader);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- ÁREA DE RESPUESTA ---
        JTextArea answerArea = new JTextArea(3, 20);
        answerArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        
        JScrollPane areaScroll = new JScrollPane(answerArea);
        areaScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        areaScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Efecto visual al hacer clic
        answerArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                areaScroll.setBorder(new LineBorder(UIComponents.DARK_GREEN, 2));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                areaScroll.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            }
        });

        // Listener progreso
        answerArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateProgress(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateProgress(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateProgress(); }
        });

        card.add(areaScroll);
        answerComponents.add(answerArea);

        return card;
    }

    /**
     * Updates the progress bar and label based on answered questions.
     */
    private void updateProgress() {
        int answeredCount = 0;
        for (JTextArea area : answerComponents) {
            if (!area.getText().trim().isEmpty()) answeredCount++;
        }
        progressLabel.setText("Progress: " + answeredCount + " / " + totalQuestions + " questions answered");
        if (totalQuestions > 0) {
            int percentage = (answeredCount * 100) / totalQuestions;
            progressBar.setValue(percentage);
            progressBar.setString(percentage + "%");
        }
    }

    /**
     * Submits the answers entered by the user.
     */
    private void submitAnswers() {
        if (answerComponents.isEmpty()) return;

        ArrayList<String> answers = new ArrayList<>();
        boolean hasEmpty = false;
        for (JTextArea area : answerComponents) {
            String txt = area.getText().trim();
            if (txt.isEmpty()) hasEmpty = true;
            answers.add(txt);
        }

        if (hasEmpty) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "You have unanswered questions.\nSubmit anyway?", "Incomplete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
        }

        try {
            if (isEditMode) {
                isSubmitting = true;
                controller.getController().modifyAnsweredForm(ufid, answers);
                if (goBackAction != null) {
                    goBackAction.run(); 
                }
                dispose();
            }
            else{
                isSubmitting = true;
                int userId = controller.getController().getCurrentProfile().getUUID();
                controller.getController().answerForm(ufid, userId, answers);
                
                JOptionPane.showMessageDialog(this, "Answers submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                new MainMenuView(controller).setVisible(true);
                dispose();
            }
            
        } catch (Exception ex) {
            isSubmitting = false;
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    /**
     * Disposes of the view, prompting the user if there is unsaved progress.
     */
    @Override
    public void dispose() {
        if (isSubmitting) { super.dispose(); return; }
        
        int answered = 0;
        for (JTextArea area : answerComponents) if (!area.getText().trim().isEmpty()) answered++;

        if (answered > 0 && answered < totalQuestions) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Unsaved progress. Exit?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new MainMenuView(controller).setVisible(true);
                super.dispose();
            }
        } else {
            new MainMenuView(controller).setVisible(true);
            super.dispose();
        }
    }
}