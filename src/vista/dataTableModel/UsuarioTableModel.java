package vista.dataTableModel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Usuario;

public class UsuarioTableModel extends AbstractTableModel {

    private final String[] columns;

    private final List<Usuario> rows = new ArrayList<>();

    public UsuarioTableModel(List<Usuario> usuarios) {
        super();
        this.columns = new String[]{"Id", "Nombre Usuario", "Nombre Completo", "Fecha Creacion", "clave"};
        if (usuarios != null) {
            this.rows.addAll(usuarios);
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
        Usuario usuario = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return usuario.getId_usuario();
            case 1:
                return usuario.getNombre_usuario();
            case 2:
                return usuario.getNombre_completo();
            case 3:
                return usuario.getFecha_creacion();
            case 4:
                return usuario.getClave_usuario();
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
                return java.sql.Timestamp.class;
            case 4:
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    public void load(List<Usuario> lista) {
        rows.clear();
        if (lista != null) {
            rows.addAll(lista);
        }
        fireTableDataChanged();
    }

    public Usuario getUsuarioAt(int row) {
        if (row < 0 || row >= rows.size()) {
            return null;
        }
        return rows.get(row);
    }

}
