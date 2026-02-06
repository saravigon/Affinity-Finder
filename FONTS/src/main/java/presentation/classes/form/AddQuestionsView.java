package presentation.classes.form;

import presentation.classes.UIComponents;
import presentation.controllers.PresentationController;
import domain.exceptions.FormException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * View to add a question to a form with the "Pine Modern" style.
 */
public class AddQuestionsView extends JFrame {

    /**
     * Attribute: form ID to which questions are added
     */
    private int formID;
    /**
     * Attribute: reference to PresentationController
     */
    private PresentationController controller;
    /**
     * Attribute: action to perform when going back
     */
    private Runnable goBackAction;
    
    // Paneles principales
    /**
     * Attribute: midPanel that changes based on question type
     */
    private JPanel midPanel;     // Panel cambiante (CardLayout)
    /**
     * Attribute: cardLayout for midPanel
     */
    private CardLayout cardLayout;

    // Campos básicos
    /**
     * Attribute: question text input
     */
    private JTextField txtQuestion;
    /**
     * Attribute: question type selector
     */
    private JComboBox<String> typeSelector;

    // Campos MULTIPLE_CHOICE
    /**
     * Attribute: choices input for multiple choice questions
     */
    private JTextField txtChoices;
    /**
     * Attribute: radio button for ordered choices (yes)
     */
    private JRadioButton rbOrderedYes;
    /**
     * Attribute: radio button for ordered choices (no)
     */
    private JRadioButton rbOrderedNo;
    /**
     * Attribute: maximum number of choices allowed
     */
    private JTextField txtMaxChoices;

    // Campos NUMERIC
    /**
     * Attribute: minimum numeric value
     */
    private JTextField txtMin;
    /**
     * Attribute: maximum numeric value
     */
    private JTextField txtMax;

    /**
     * Constructor
     * @param controller PresentationController reference
     * @param id Form ID to which questions are added
     * @param goBackAction Action to perform when going back
     */
    public AddQuestionsView(PresentationController controller, int id, Runnable goBackAction) {
        super("Add Question");
        this.controller = controller;
        this.formID = id;
        this.goBackAction = goBackAction;
        initComponents();
    }

    /**
     * Initializes the view components
     */
    private void initComponents() {
        UIComponents.configureWindow(this, "Add Question to Form", 500, 600);
        

        // 1. PANEL PRINCIPAL (Marco Global)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // ---------------------------------------------------------
        // 2. HEADER (Título y Botón Cancelar)
        // ---------------------------------------------------------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBackground(Color.WHITE);

        JButton btnBack = UIComponents.backButton();
        btnBack.addActionListener(e -> {
            if (goBackAction != null) {
                goBackAction.run(); 
            }
            dispose();
        });
        topPanel.add(btnBack);

        // ---------------------------------------------------------
        // 3. CENTER (Formulario)
        // ---------------------------------------------------------
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(new EmptyBorder(20, 0, 20, 0));

        // --- A. Campos Comunes ---

        // Question Text
        formContainer.add(UIComponents.createLeftLabel("Question Text:"));
        txtQuestion = UIComponents.createStyledTextField();
        formContainer.add(txtQuestion);
        formContainer.add(Box.createVerticalStrut(15));

        // Type Selector
        formContainer.add(UIComponents.createLeftLabel("Question Type:"));
        typeSelector = new JComboBox<>(new String[]{"OPEN_ENDED", "MULTIPLE_CHOICE", "NUMERIC"});
        typeSelector.setFont(UIComponents.STANDARD_FONT);
        typeSelector.setBackground(Color.WHITE);
        typeSelector.setMaximumSize(new Dimension(200, 35));
        typeSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        typeSelector.addActionListener(e -> updateTypeFields());
        formContainer.add(typeSelector);
        formContainer.add(Box.createVerticalStrut(20));

        // --- B. Panel Cambiante (CardLayout) ---
        // Lo envolvemos en un borde verde para distinguirlo
        cardLayout = new CardLayout();
        midPanel = new JPanel(cardLayout);
        midPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIComponents.DARK_GREEN, 1),
                " Type Specific Options ",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                UIComponents.ITALIC_FONT,
                UIComponents.DARK_GREEN
        ));
        midPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        midPanel.setBackground(Color.WHITE);

        // -- CARD 1: OPEN_ENDED --
        JPanel openPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        openPanel.setBackground(Color.WHITE);
        JLabel lblInfo = new JLabel("No additional options required for open questions.");
        lblInfo.setFont(UIComponents.ITALIC_FONT);
        lblInfo.setForeground(Color.GRAY);
        openPanel.add(lblInfo);
        midPanel.add(openPanel, "OPEN_ENDED");

        // -- CARD 2: MULTIPLE_CHOICE --
        JPanel multiplePanel = new JPanel();
        multiplePanel.setLayout(new BoxLayout(multiplePanel, BoxLayout.Y_AXIS));
        multiplePanel.setBackground(Color.WHITE);
        multiplePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        multiplePanel.add(UIComponents.createLeftLabel("Choices (comma separated):"));
        txtChoices = UIComponents.createStyledTextField();
        multiplePanel.add(txtChoices);
        multiplePanel.add(Box.createVerticalStrut(10));

        // Radio Buttons (Ordered)
        JPanel orderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        orderPanel.setBackground(Color.WHITE);
        orderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rbOrderedYes = new JRadioButton("Ordered");
        rbOrderedNo  = new JRadioButton("Not ordered", true);
        UIComponents.styleRadioButton(rbOrderedYes);
        UIComponents.styleRadioButton(rbOrderedNo);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbOrderedYes);
        bg.add(rbOrderedNo);
        orderPanel.add(rbOrderedYes);
        orderPanel.add(Box.createHorizontalStrut(15));
        orderPanel.add(rbOrderedNo);
        
        multiplePanel.add(orderPanel);
        multiplePanel.add(Box.createVerticalStrut(10));

        multiplePanel.add(UIComponents.createLeftLabel("Max choices (if NOT ordered):"));
        txtMaxChoices = UIComponents.createStyledTextField();
        multiplePanel.add(txtMaxChoices);

        midPanel.add(multiplePanel, "MULTIPLE_CHOICE");

        // -- CARD 3: NUMERIC --
        JPanel numericPanel = new JPanel();
        numericPanel.setLayout(new BoxLayout(numericPanel, BoxLayout.Y_AXIS));
        numericPanel.setBackground(Color.WHITE);
        numericPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        numericPanel.add(UIComponents.createLeftLabel("Minimum Bound:"));
        txtMin = UIComponents.createStyledTextField();
        numericPanel.add(txtMin);
        numericPanel.add(Box.createVerticalStrut(10));

        numericPanel.add(UIComponents.createLeftLabel("Maximum Bound:"));
        txtMax = UIComponents.createStyledTextField();
        numericPanel.add(txtMax);

        midPanel.add(numericPanel, "NUMERIC");

        // Añadir el panel cambiante al formulario
        formContainer.add(midPanel);
        formContainer.add(Box.createVerticalGlue()); // Empuja todo hacia arriba

        // ---------------------------------------------------------
        // 4. BOTTOM (Botones Finish y Save)
        // ---------------------------------------------------------
        // Usamos un gap (hueco) de 15px entre botones
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottomPanel.setBackground(Color.WHITE);

        // --- Botón FINISH (Volver al menú) ---
        // Lo hacemos un poco diferente (o igual) para distinguir que es "Salir"
        // Si tienes un método para botón secundario en UIComponents úsalo, si no, usa el mismo.
        JButton btnFinish = UIComponents.createStyledButton("Finish", 120, 35);
        
        // Opcional: Cambiar el color del texto o fondo si tu UIComponents lo permite
        // para indicar que no es la acción principal de "Guardar".
        // btnFinish.setBackground(Color.LIGHT_GRAY); 

        btnFinish.addActionListener(e -> {
            new FormMenuView(controller).setVisible(true);
            dispose(); // Cerramos esta ventana
        });

        // --- Botón SAVE (Guardar pregunta y seguir) ---
        JButton btnSave = UIComponents.createStyledButton("Save", 150, 35);
        btnSave.addActionListener(e -> saveQuestion());
        
        // Añadimos al panel (Orden: Finish a la izquierda, Save a la derecha)
        bottomPanel.add(btnFinish);
        bottomPanel.add(btnSave);

        // ---------------------------------------------------------
        // 5. ENSAMBLAJE FINAL
        // ---------------------------------------------------------
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formContainer, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        
        updateTypeFields(); // Inicializar estado
    }

    /**
     * Updates the midPanel fields based on selected question type
     */
    private void updateTypeFields() {
        String type = (String) typeSelector.getSelectedItem();
        cardLayout.show(midPanel, type);
    }
    /**
     * Saves the question to the form
     */
    private void saveQuestion() {
        String qt = txtQuestion.getText().trim();
        if (qt.isEmpty()) { showWarning("Question text cannot be empty."); return; }

        String type = (String) typeSelector.getSelectedItem();
        ArrayList<String> choices = null;
        Boolean ordered = null;
        Integer maxChoices = null;
        Integer minBound = null;
        Integer maxBound = null;

        try {
            switch (type) {
                case "MULTIPLE_CHOICE":
                    String rawChoices = txtChoices.getText().trim();
                    if (rawChoices.isEmpty()) { showWarning("Choices cannot be empty."); return; }
                    choices = new ArrayList<>(Arrays.asList(rawChoices.split(",")));
                    ordered = rbOrderedYes.isSelected();
                    if (!ordered) {
                        String maxTxt = txtMaxChoices.getText().trim();
                        if (maxTxt.isEmpty()) {
                            showWarning("Please specify the maximum number of choices for unordered questions.");
                            return;
                        }
                        maxChoices = Integer.parseInt(maxTxt);
                    }
                    break;
                case "NUMERIC":
                    minBound = Integer.parseInt(txtMin.getText().trim());
                    maxBound = Integer.parseInt(txtMax.getText().trim());
                    break;
            }

            controller.getController().addQuestionToForm(
                    formID, qt, type, choices, ordered, maxChoices, minBound, maxBound
            );

            JOptionPane.showMessageDialog(this, "Question added successfully!");
            new AddQuestionsView(controller, formID, goBackAction).setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            showWarning("Invalid numeric value.");
        } catch (FormException ex) {
            showWarning(ex.getMessage());
        } catch (Exception ex) {
            showWarning("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Shows a warning message dialog
     * @param msg The warning message to display
     */
    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

}