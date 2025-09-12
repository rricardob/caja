package vista.dataTableModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Transaccion;

public class TransaccionTableModel extends AbstractTableModel {

    private final String[] columnNames = {
        "Sr.(es)", "DNI o RUC", "Dirección", "Descripción", "Importe ", "Fecha Registro"
    };

    private final List<Transaccion> datos = new ArrayList<>();

    public TransaccionTableModel() {
    }

    public void load(List<Transaccion> lista) {
        datos.clear();
        if (lista != null) {
            datos.addAll(lista);
        }
        fireTableDataChanged();
    }

    public void add(Transaccion t) {
        if (t == null) {
            return;
        }
        int row = datos.size();
        datos.add(t);
        fireTableRowsInserted(row, row);
    }

    public Transaccion getTransaccionAt(int row) {
        if (row < 0 || row >= datos.size()) {
            return null;
        }
        return datos.get(row);
    }

    public void removeAt(int row) {
        if (row < 0 || row >= datos.size()) {
            return;
        }
        datos.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void clear() {
        datos.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return datos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        if (column < 0 || column >= columnNames.length) {
            return "";
        }
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 4:
                return BigDecimal.class; // importe
            case 5:
                return Timestamp.class;  // fecha
            default:
                return String.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaccion t = datos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return t.getNombre_completo();
            case 1:
                // Mostrar DNI si existe; si no, mostrar RUC; si es ninguno, cadena vacía
                String identificador = t.getDoc_identidad();
                if (identificador != null && !identificador.trim().isEmpty()) {
                    return identificador;
                }
                String ruc = t.getRuc();
                return (ruc != null && !ruc.trim().isEmpty()) ? ruc : "";
            case 2:
                return t.getDireccion();
            case 3:
                return t.getDescripcion();
            case 4:
                return t.getImporte();
            case 5:
                return t.getFecha_creacion();
            default:
                return null;
        }
    }
}
