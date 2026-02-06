package presentation.classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import domain.classes.Answer;
import domain.classes.QuestionAnswer;
import presentation.classes.profile.VisitProfileView;
import presentation.controllers.PresentationController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating reusable UI components with consistent styling.
 */
public class UIComponents {

    // ---------------------------------------------
    // FONTS
    // ---------------------------------------------   
 
    /**
     * Font used for the buttons
     */
    public static Font STANDARD_FONT = new Font(Font.MONOSPACED, Font.BOLD, 15);
    /**
     * Font used for texts
     */
    public static Font SMALL_FONT = new Font(Font.MONOSPACED, Font.BOLD, 13);
    /**
     * Font used for the username
     */
    public static Font TITLE_FONT = new Font(Font.MONOSPACED, Font.BOLD, 22); // Un poco más grande
    /**
     * Font used for italic text
     */
    public static Font ITALIC_FONT = new Font(Font.MONOSPACED, Font.ITALIC, 13);
    /**
     * Font used for general text
     */
    public static Font TEXT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 15);


    // ---------------------------------------------
    // COLORS
    // ---------------------------------------------

    /**
     * Main color for the profile menu
     * A dark green color
     */
    public static Color DARK_GREEN = new Color(12, 60, 40); 
    /**
     * Secondary color for backgrounds
     * A lighter green color
     */
    public static Color LIGHT_GREEN = new Color(245, 250, 245); 
    /**
     * Accent color for buttons and highlights
     */
    public static Color ACCENT_GREEN = new Color(76, 175, 80);
    /**
     * Grey color for warnings
     */
    public static Color GREY_COLOR = new Color(100, 100, 100); 


    // ---------------------------------------------
    // BUTTON CREATORS
    // ---------------------------------------------

    /**
     * Creates a styled button with standard dimensions and appearance.
     * @param text Button text
     * @return Styled JButton
     */
    public static JButton createStyledButton(String text, int width, int height) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, height));
        btn.setMaximumSize(new Dimension(width, height));
        //btn.setBackground(Color.WHITE);     
        btn.setFont(STANDARD_FONT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Styles a button to look like a hyperlink.
     * @param btn JButton to style
     */
    public static void styleLinkButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 0, 0, 0));
    }

    /**
     * Creates a button with a green gradient background and dark green text and border.
     * @param text Button text
     * @param width Button width
     * @param height Button height
     * @return JButton with green gradient styling
     */
    public static JButton createGreenGradientButton(String text, int width, int height) {
      
        // 1. BUTTON CREATION OVERRIDING paintComponent
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp;
                if (getModel().isRollover()) {
                    // If hovered, darker gradient
                    gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(210, 235, 225));
                } else {
                    gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), LIGHT_GREEN);
                }
                
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();

                // Calls super to paint the text over the background
                super.paintComponent(g);
            }
        };

        // 2. STANDARD STYLING
        btn.setContentAreaFilled(false); 
        btn.setFocusPainted(false);
        btn.setForeground(DARK_GREEN);
        btn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 4. DARK GREEN THIN BORDER
        // Simple 1px line border
        btn.setBorder(BorderFactory.createLineBorder(DARK_GREEN, 1));

        // 5. DIMENSIONS
        btn.setPreferredSize(new Dimension(width, height));
        btn.setMaximumSize(new Dimension(width, height));

        return btn;
    }

    /**
     * Creates a link-styled button.
     * @param text Button text
     * @return Link-styled JButton
     */
    public static JButton createLinkButton(String text) {
        JButton btn = new JButton(text);
        styleLinkButton(btn);
        return btn;
    }

    /**
     * Creates a back button with arrow and "Back" text.
     * @return Back JButton
     */
    public static JButton backButton(){
        JButton btnBack = new JButton("← Back");
        styleLinkButton(btnBack);
        btnBack.setFont(SMALL_FONT);
        return btnBack;
    }

    /**
     * Creates a left-aligned label with standard styling.
     * @param text Label text
     * @return Styled JLabel
     */
    public static JLabel createLeftLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(STANDARD_FONT);
        lbl.setForeground(DARK_GREEN);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    /**
     * Creates a center-aligned label with standard styling.
     * @param text Label text
     * @return Styled JLabel
     */
    public static JLabel createCenterLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(STANDARD_FONT);
        lbl.setForeground(DARK_GREEN);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    /**
     * Creates a styled JTextField with standard dimensions and appearance.
     * @return Styled JTextField
     */
    public static JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(STANDARD_FONT);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tf;
    }
    
    /**
     * Styles a JRadioButton with standard appearance.
     * @param rb JRadioButton to style
     */
    public static void styleRadioButton(JRadioButton rb) {
        rb.setBackground(Color.WHITE);
        rb.setFont(STANDARD_FONT);
        rb.setFocusPainted(false);
        rb.setForeground(Color.BLACK);
    }

    /**
     * Creates a clickable user box (card-style) with hover effect
     * @param username Username to display
     * @param userID User ID for navigation
     * @return Clickable JPanel
     */
    public static JPanel createClickableUserBox(String username, Integer userID, Integer formID, PresentationController controller, JFrame parentFrame) {
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout());
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponents.DARK_GREEN, 1, true),
            new EmptyBorder(12, 20, 12, 20)
        ));
        box.setMaximumSize(new Dimension(550, 50));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Username label (left)
        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(UIComponents.TEXT_FONT);
        nameLabel.setForeground(UIComponents.DARK_GREEN);
        box.add(nameLabel, BorderLayout.WEST);

        // "View profile" label (right, initially invisible)
        JLabel actionLabel = new JLabel("View profile →");
        actionLabel.setFont(UIComponents.ITALIC_FONT);
        actionLabel.setForeground(UIComponents.DARK_GREEN);
        actionLabel.setVisible(false);
        box.add(actionLabel, BorderLayout.EAST);

        // Hover effects
        box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                box.setBackground(new Color(245, 250, 245));
                actionLabel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                box.setBackground(Color.WHITE);
                actionLabel.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigate to profile view
                new VisitProfileView(controller, userID, formID).setVisible(true);
                parentFrame.dispose();
            }
        });
        
        return box;
    }

    
    /**
     * Converts an Answer object to a List of String representations
     * @param answer The Answer object to convert
     * @return List of strings representing each answer
     */
    public static List<String> answerToStringList(Answer answer) {
        List<String> stringList = new ArrayList<>();
        
        for (QuestionAnswer qa : answer.getAnswer()) {
            String answerStr = "";
            
            switch (qa.getQuestionType()) {
                case NUMERIC:
                    Integer numAnswer = qa.getAnswerInteger();
                    answerStr = numAnswer != null ? numAnswer.toString() : "";
                    break;
                    
                case OPEN_ENDED:
                    String strAnswer = qa.getAnswerString();
                    answerStr = strAnswer != null ? strAnswer : "";
                    break;
                    
                case MULTIPLE_CHOICE:
                    ArrayList<String> multipleAnswer = qa.getAnswerMultiple();
                    if (multipleAnswer != null && !multipleAnswer.isEmpty()) {
                        answerStr = multipleAnswer.get(0); // Toma la primera opción seleccionada
                        // O si pueden ser múltiples: answerStr = String.join(", ", multipleAnswer);
                    } else {
                        answerStr = "";
                    }
                    break;
                    
                default:
                    answerStr = "";
            }
            
            stringList.add(answerStr);
        }
        
        return stringList;
    }

    // ---------------------------------------------
    // WINDOW CONFIGURATION
    // ---------------------------------------------

    /**
     * Configures common JFrame properties.
     * @param frame The frame to configure
     * @param title Window title
     * @param width Window width
     * @param height Window height
     */
    public static void configureWindow(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
    }

    // ---------------------------------------------
    // GRADIENT PANEL
    // ---------------------------------------------

    /**
     * Creates a panel with a gradient background (white to light blue).
     * @return JPanel with gradient background
     */
    public static JPanel createGradientPanel() {
        return new GradientPanel();
    }

    /**
     * Panel personalizado con degradado de fondo
     * Blanco (arriba) a azul pálido (abajo)
     */
    public static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int w = getWidth();
            int h = getHeight();

            // Degradado: blanco (arriba) a azul pálido (abajo)
            Color color1 = Color.WHITE;
            Color color2 = new Color(230, 255, 242);

            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}