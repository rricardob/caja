package util.ui;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Helpers para aplicar DocumentFilter reutilizables.
 */
public final class DocumentFilters {

    private DocumentFilters() {
    }

    // filtro numérico entero con longitud máxima (enteros: sólo dígitos)
    public static void attachNumeric(JTextField field, int maxLen) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericLengthFilter(maxLen));
    }

    // filtro decimal (acepta dígitos y un separador decimal: punto o coma). maxLen es longitud total permitida.
    public static void attachDecimal(JTextField field, int maxLen) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DecimalFilter(maxLen));
    }

    // filtro alfanumérico con símbolos - . / y espacio
    public static void attachAlphaNumSymbol(JTextField field, int maxLen) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new AlphaNumSymbolFilter(maxLen));
    }

    // Limita JTextArea a maxLen caracteres (permite cualquier carácter)
    public static void attachTextAreaLimit(JTextArea area, int maxLen) {
        Document doc = area.getDocument();
        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).setDocumentFilter(new TextAreaLengthFilter(maxLen));
        }
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
            if (string == null || string.isEmpty()) {
                return;
            }
            String filtered = filterDigits(string);
            if (filtered.isEmpty()) {
                return;
            }
            if (fb.getDocument().getLength() + filtered.length() <= maxLen) {
                super.insertString(fb, offset, filtered, attr);
            } else {
                // truncar si es necesario
                int allowed = maxLen - fb.getDocument().getLength();
                if (allowed > 0) {
                    super.insertString(fb, offset, filtered.substring(0, allowed), attr);
                }
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
            } else {
                int allowed = maxLen - (curLen - length);
                if (allowed > 0) {
                    super.replace(fb, offset, length, filtered.substring(0, allowed), attrs);
                }
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

    /**
     * DecimalFilter: - permite dígitos y un solo separador decimal (punto o
     * coma) - evita múltiples separadores - controla longitud total (incluye
     * separador)
     */
    private static class DecimalFilter extends DocumentFilter {

        private final int maxLen;
        // permitidos: dígitos, punto, coma
        private static final Pattern ALLOWED = Pattern.compile("[0-9\\.,]");

        DecimalFilter(int maxLen) {
            this.maxLen = maxLen;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
                throws BadLocationException {
            if (string == null || string.isEmpty()) {
                return;
            }
            String filtered = filterAllowed(string, fb);
            if (filtered.isEmpty()) {
                return;
            }
            if (fb.getDocument().getLength() + filtered.length() <= maxLen) {
                super.insertString(fb, offset, filtered, attr);
            } else {
                int allowed = maxLen - fb.getDocument().getLength();
                if (allowed > 0) {
                    super.insertString(fb, offset, filtered.substring(0, allowed), attr);
                }
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
            String filtered = filterAllowed(text, fb);
            if (filtered.isEmpty()) {
                return;
            }
            int curLen = fb.getDocument().getLength();
            int newLen = curLen - length + filtered.length();
            if (newLen <= maxLen) {
                super.replace(fb, offset, length, filtered, attrs);
            } else {
                int allowed = maxLen - (curLen - length);
                if (allowed > 0) {
                    super.replace(fb, offset, length, filtered.substring(0, allowed), attrs);
                }
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private String filterAllowed(String in, FilterBypass fb) throws BadLocationException {
            StringBuilder sb = new StringBuilder(in.length());
            String existing = fb.getDocument().getText(0, fb.getDocument().getLength());
            int existingSeparators = countSeparators(existing);
            for (int i = 0; i < in.length(); i++) {
                char c = in.charAt(i);
                String ch = String.valueOf(c);
                if (!ALLOWED.matcher(ch).matches()) {
                    continue;
                }
                if (c == '.' || c == ',') {
                    if (existingSeparators >= 1) {
                        continue; // ya existe un separador
                    }
                    existingSeparators++;
                    // permitir punto o coma; normalización se hará al parsear
                }
                sb.append(c);
            }
            return sb.toString();
        }

        private int countSeparators(String s) {
            int cnt = 0;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '.' || c == ',') {
                    cnt++;
                }
            }
            return cnt;
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
            if (string == null || string.isEmpty()) {
                return;
            }
            String filtered = filterAllowed(string);
            if (filtered.isEmpty()) {
                return;
            }
            if (fb.getDocument().getLength() + filtered.length() <= maxLen) {
                super.insertString(fb, offset, filtered, attr);
            } else {
                int allowed = maxLen - fb.getDocument().getLength();
                if (allowed > 0) {
                    super.insertString(fb, offset, filtered.substring(0, allowed), attr);
                }
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
            } else {
                int allowed = maxLen - (curLen - length);
                if (allowed > 0) {
                    super.replace(fb, offset, length, filtered.substring(0, allowed), attrs);
                }
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

    // Limita longitud de JTextArea (permite cualquier caracter)
    private static class TextAreaLengthFilter extends DocumentFilter {

        private final int maxLen;

        TextAreaLengthFilter(int maxLen) {
            this.maxLen = maxLen;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
                throws BadLocationException {
            if (string == null || string.isEmpty()) {
                return;
            }
            int curLen = fb.getDocument().getLength();
            int newLen = curLen + string.length();
            if (newLen <= maxLen) {
                super.insertString(fb, offset, string, attr);
            } else {
                int allowed = maxLen - curLen;
                if (allowed > 0) {
                    super.insertString(fb, offset, string.substring(0, allowed), attr);
                }
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                super.replace(fb, offset, length, text, attrs);
                return;
            }
            int curLen = fb.getDocument().getLength();
            int newLen = curLen - length + text.length();
            if (newLen <= maxLen) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                int allowed = maxLen - (curLen - length);
                if (allowed > 0) {
                    super.replace(fb, offset, length, text.substring(0, allowed), attrs);
                }
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }
}
