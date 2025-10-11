package vista.dataTableModel;

import modelo.TipoTransaccion;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class TipoTransaccionTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Categoría", "Descripción", "Estado"};
    private final List<TipoTransaccion> rows = new ArrayList<>();
    private Map<Long, String> categorias = new HashMap<>();

    public void setCategorias(Map<Long, String> categorias) {
        this.categorias = (categorias == null) ? new HashMap<>() : new HashMap<>(categorias);
        fireTableDataChanged();
    }

    public void load(List<TipoTransaccion> lista) {
        rows.clear();
        if (lista != null) {
            rows.addAll(lista);
        }
        fireTableDataChanged();
    }

    public void add(TipoTransaccion t) {
        rows.add(t);
        int r = rows.size() - 1;
        fireTableRowsInserted(r, r);
    }

    public void update(int rowIndex, TipoTransaccion t) {
        rows.set(rowIndex, t);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeAt(int rowIndex) {
        rows.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public TipoTransaccion getTipoAt(int rowIndex) {
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
        TipoTransaccion t = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return t.getId_tipo();
            case 1: {
                Long idCat = t.getId_categoria_transacciones();
                return (idCat == null) ? "" : categorias.getOrDefault(idCat, "");
            }
            case 2:
                return t.getDescripcion();
            case 3:
                return Boolean.TRUE.equals(t.getEstado()) ? "Activo" : "Inactivo";
            default:
                return "";
        }
    }

}
