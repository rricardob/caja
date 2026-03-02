package vista.dataTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Cheque;
import java.text.SimpleDateFormat;

public class ChequeTableModel extends AbstractTableModel {

    private final String[] columnNames = { "ID", "Saldo Anterior", "Fecha Emisión", "Nro Cheque", "Total Cheque" };
    private final List<Cheque> datos = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void load(List<Cheque> lista) {
        datos.clear();
        if (lista != null) {
            datos.addAll(lista);
        }
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
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 1:
            case 4:
                return BigDecimal.class;
            case 2:
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cheque c = datos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return c.getId_cheque();
            case 1:
                return c.getSaldo_anterior();
            case 2:
                return c.getFecha_emision() != null ? dateFormat.format(c.getFecha_emision()) : "";
            case 3:
                return c.getNro_cheque();
            case 4:
                return c.getTotal_cheque();
            default:
                return null;
        }
    }

    public Cheque getChequeAt(int row) {
        if (row >= 0 && row < datos.size()) {
            return datos.get(row);
        }
        return null;
    }
}
