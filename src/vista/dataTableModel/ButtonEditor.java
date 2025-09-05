
package vista.dataTableModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private final JButton button;
    private int row;
    private JTable table;

    public ButtonEditor() {
        button = new JButton("..."); // Crea el botón del editor
        button.setOpaque(true);
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;
        if (value instanceof String) {
            button.setText((String) value);
        } else {
            button.setText("...");
        }
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Aquí puedes obtener los datos de la fila
        Object id = table.getValueAt(row, 0); // Suponiendo que la primera columna es el ID
        System.out.println("Botón presionado en la fila: " + row + ", ID de la fila: " + id);

        // Dispara el evento para detener la edición
        fireEditingStopped();
    }

}
