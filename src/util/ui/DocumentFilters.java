package util.ui;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;

/**
 * Helpers para aplicar DocumentFilter reutilizables.
 */
public final class DocumentFilters {

    private DocumentFilters() {
    }

    // filtro numérico con longitud máxima
    public static void attachNumeric(JTextField field, int maxLen) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericLengthFilter(maxLen));
    }

    // filtro alfanumérico con símbolos - . / y espacio
    public static void attachAlphaNumSymbol(JTextField field, int maxLen) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new AlphaNumSymbolFilter(maxLen));
    }

    // Implementaciones
    private static class NumericLengthFilter extends DocumentFilter {

        private final int maxLen;

        NumericLengthFilter(int maxLen) {
            this.maxLen = maxLen;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
                throws BadLocationException {
            if (string == null) {
                return;
            }
            String filtered = filterDigits(string);
            if (filtered.isEmpty()) {
                return;
            }
            if (fb.getDocument().getLength() + filtered.length() <= maxLen) {
                super.insertString(fb, offset, filtered, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            if (text.isEmpty()) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            String filtered = filterDigits(text);
            if (filtered.isEmpty()) {
                return;
            }
            int curLen = fb.getDocument().getLength();
            int newLen = curLen - length + filtered.length();
            if (newLen <= maxLen) {
                super.replace(fb, offset, length, filtered, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private String filterDigits(String in) {
            StringBuilder sb = new StringBuilder(in.length());
            for (int i = 0; i < in.length(); i++) {
                char c = in.charAt(i);
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }

    private static class AlphaNumSymbolFilter extends DocumentFilter {

        private final int maxLen;
        private static final Pattern ALLOW = Pattern.compile("[\\p{L}0-9\\.\\-/ ]");

        AlphaNumSymbolFilter(int maxLen) {
            this.maxLen = maxLen;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
                throws BadLocationException {
            if (string == null) {
                return;
            }
            String filtered = filterAllowed(string);
            if (filtered.isEmpty()) {
                return;
            }
            if (fb.getDocument().getLength() + filtered.length() <= maxLen) {
                super.insertString(fb, offset, filtered, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            if (text.isEmpty()) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            String filtered = filterAllowed(text);
            if (filtered.isEmpty()) {
                return;
            }
            int curLen = fb.getDocument().getLength();
            int newLen = curLen - length + filtered.length();
            if (newLen <= maxLen) {
                super.replace(fb, offset, length, filtered, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private String filterAllowed(String in) {
            StringBuilder sb = new StringBuilder(in.length());
            for (int i = 0; i < in.length(); i++) {
                String ch = in.substring(i, i + 1);
                if (ALLOW.matcher(ch).matches()) {
                    sb.append(ch);
                }
            }
            return sb.toString();
        }
    }
}
