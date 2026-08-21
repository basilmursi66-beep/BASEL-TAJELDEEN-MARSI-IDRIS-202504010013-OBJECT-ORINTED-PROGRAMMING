import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;

// Builds text fields that reject invalid keystrokes as the user types,
// instead of only complaining after they hit submit.
public class TextFieldFilters {

    private static void applyFilter(JTextField field, String allowedPattern, int maxLength) {

        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String current = currentText(fb);
                String result = current.substring(0, offset) + string + current.substring(offset);
                if (string.matches(allowedPattern) && result.length() <= maxLength) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) text = "";
                String current = currentText(fb);
                String result = current.substring(0, offset) + text + current.substring(offset + length);
                if (text.matches(allowedPattern) && result.length() <= maxLength) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private String currentText(FilterBypass fb) throws BadLocationException {
                return fb.getDocument().getText(0, fb.getDocument().getLength());
            }
        });
    }

    // Letters, spaces, apostrophes, and hyphens only - for names (blocks numbers/symbols)
    public static JTextField nameField() {
        JTextField field = new JTextField();
        applyFilter(field, "[a-zA-Z '\\-]*", 60);
        return field;
    }

    // Digits only - for age, capacity
    public static JTextField digitsOnlyField(int maxLength) {
        JTextField field = new JTextField();
        applyFilter(field, "[0-9]*", maxLength);
        return field;
    }

    // Letters and digits only, no spaces - for student IDs and course codes
    public static JTextField alphanumericField(int maxLength) {
        JTextField field = new JTextField();
        applyFilter(field, "[a-zA-Z0-9]*", maxLength);
        return field;
    }

    // Anything except spaces - for passwords (spaces in passwords cause silent bugs)
    public static JTextField noSpacesField(int maxLength) {
        JTextField field = new JTextField();
        applyFilter(field, "[^\\s]*", maxLength);
        return field;
    }

    // Letters, digits, and common email symbols only
    public static JTextField emailField(int maxLength) {
        JTextField field = new JTextField();
        applyFilter(field, "[a-zA-Z0-9._%+\\-@]*", maxLength);
        return field;
    }

    // Digits and a single decimal point - for money amounts
    public static JTextField decimalField(int maxLength) {
        JTextField field = new JTextField();
        applyFilter(field, "[0-9.]*", maxLength);
        return field;
    }
}
