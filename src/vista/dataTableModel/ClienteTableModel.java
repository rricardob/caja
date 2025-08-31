package vista.dataTableModel;

import modelo.Cliente;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ClienteTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Nombre", "Doc / RUC", "Dirección", "Teléfono"};
    private final List<Cliente> rows = new ArrayList<>();

    public void load(List<Cliente> lista) {
        rows.clear();
        if (lista != null) {
            rows.addAll(lista);
        }
        fireTableDataChanged();
    }

    public void add(Cliente c) {
        rows.add(c);
        int r = rows.size() - 1;
        fireTableRowsInserted(r, r);
    }

    public void update(int rowIndex, Cliente c) {
        rows.set(rowIndex, c);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeAt(int rowIndex) {
        rows.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Cliente getClienteAt(int rowIndex) {
        return rows.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cliente c = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return c.getId_cliente();
            case 1:
                return c.getNombre_completo();
            case 2:
                String doc = c.getDoc_identidad();
                if (doc != null && !doc.trim().isEmpty()) {
                    return doc;
                }
                return c.getRuc() == null ? "" : c.getRuc();
            case 3:
                return c.getDireccion();
            case 4:
                return c.getTelefono();
            default:
                return "";
        }
    }
}
