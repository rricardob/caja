package vista.dataTableModel;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PasswordCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        // Llamar al renderer por defecto para obtener el componente base
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Si el valor no es null, ofuscarlo con asteriscos
        if (value != null) {
            String strValue = String.valueOf(value);
            int length = strValue.length();

            // Alternativa compatible con Java 8+: usar un StringBuilder para repetir asteriscos
            StringBuilder asterisks = new StringBuilder();
            for (int i = 0; i < length; i++) {
                asterisks.append('*');
            }
            setText(asterisks.toString());
        } else {
            setText(""); // O maneja null como quieras
        }

        return this;
    }

}
