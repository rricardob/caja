
package vista.dataTableModel;

import dao.RegistroBancoDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.BancoCuenta;


public class BancoCuentaTableModel extends AbstractTableModel {
    private final String[] columns;

    private final List<BancoCuenta> rows = new ArrayList<>();
    
    private RegistroBancoDAO bancoDao = new RegistroBancoDAO();

    public BancoCuentaTableModel(List<BancoCuenta> bancoCuenta) {
        super();
        this.columns = new String[]{"Id", "Nombre Banco", "Nro Cuenta","Fecha","Moneda","Descripcion", "Estado"};
        if (bancoCuenta != null) {
            this.rows.addAll(bancoCuenta);
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
        BancoCuenta bancoCuenta = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return bancoCuenta.getId_cuenta();
            case 1:
                return this.bancoDao.obteneBancoPorId(bancoCuenta.getId_banco());
            case 2:
                return bancoCuenta.getNro_cuenta();
            case 3:
                return bancoCuenta.getFecha();
            case 4:
                return bancoCuenta.getMoneda().equals("PEN") ? "Soles" : "Dolares";
            case 5:
                return bancoCuenta.getDescripcion();
            case 6:
                return (bancoCuenta.getEstado() == 1 ? "Activo": "Inactivo");
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
            case 3:
                return Object.class;
            case 4:
                return String.class;
            case 5:
                return String.class;
            case 6:
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    public void load(List<BancoCuenta> lista) {
        rows.clear();
        if (lista != null) {
            rows.addAll(lista);
        }
        fireTableDataChanged();
    }

    public BancoCuenta getBancoAt(int row) {
        if (row < 0 || row >= rows.size()) {
            return null;
        }
        return rows.get(row);
    }
}
