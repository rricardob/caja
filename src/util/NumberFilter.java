/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author ricardo
 */
public class NumberFilter extends DocumentFilter {

    // Formato para enteros (sin decimales)
    private DecimalFormat dfEntero = new DecimalFormat("#,##0", new java.text.DecimalFormatSymbols(Locale.US));
    // Formato para decimales (con dos decimales)
    private DecimalFormat dfDecimal = new DecimalFormat("#,##0.00", new java.text.DecimalFormatSymbols(Locale.US));
    
    private int limite;

    public NumberFilter(int limite) {
        this.limite = limite;
        // Para asegurar que los puntos sean para decimales y las comas para miles
        dfEntero.setParseBigDecimal(true);
        dfDecimal.setParseBigDecimal(true);
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        // Obtenemos el texto actual sin comas ni puntos para verificar la longitud
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength()).replaceAll("[^0-9]", "");
        
        // Verificamos si el texto es un número y si no excede el límite
        if (string.matches("[0-9,.]") && (currentText.length() + string.length() - (string.contains(".") ? 1 : 0)) <= this.limite) {
            super.insertString(fb, offset, string, attr);
            formatText(fb);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        // Obtenemos el texto actual sin comas ni puntos para verificar la longitud
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength()).replaceAll("[^0-9]", "");
        
        // Verificamos si el texto es un número y si no excede el límite
        if ((text.matches("[0-9,.]*") || text.isEmpty()) && (currentText.length() - length + text.length() - (text.contains(".") ? 1 : 0)) <= this.limite) {
            super.replace(fb, offset, length, text, attrs);
            formatText(fb);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
        formatText(fb);
    }

    private void formatText(FilterBypass fb) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        currentText = currentText.replaceAll("[^0-9.]", "");
        
        if (!currentText.isEmpty()) {
            try {
                // Revisa si hay punto decimal
                if (currentText.contains(".")) {
                    double number = Double.parseDouble(currentText);
                    String formattedText = dfDecimal.format(number);
                    fb.replace(0, fb.getDocument().getLength(), formattedText, null);
                } else {
                    long number = Long.parseLong(currentText);
                    String formattedText = dfEntero.format(number);
                    fb.replace(0, fb.getDocument().getLength(), formattedText, null);
                }
            } catch (NumberFormatException e) {
                // Manejar errores de formato, por ejemplo, si el texto es solo "."
            }
        }
    }
}
