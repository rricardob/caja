
package vista.dataTableModel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Banco;


public class BancoTableModel extends AbstractTableModel {
    private final String[] columns;

    private final List<Banco> rows = new ArrayList<>();

    public BancoTableModel(List<Banco> bancos) {
        super();
        this.columns = new String[]{"Id", "Nombre Banco", "Estado"};
        if (bancos != null) {
            this.rows.addAll(bancos);
        }
    }

    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    @Override
    public int getColumnCount() {
        return this.columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Banco banco = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return banco.getId_banco();
            case 1:
                return banco.getDescripcion();
            case 2:
                return (banco.getEstado() == 1 ? "Activo": "Inactivo");
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    public void load(List<Banco> lista) {
        rows.clear();
        if (lista != null) {
            rows.addAll(lista);
        }
        fireTableDataChanged();
    }

    public Banco getBancoAt(int row) {
        if (row < 0 || row >= rows.size()) {
            return null;
        }
        return rows.get(row);
    }
}
