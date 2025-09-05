
package vista.dataTableModel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Reposicion;

public class ListadoReposicionTableModel extends AbstractTableModel {
    
    private final List<Reposicion> reposiciones;
    private final String[] columns;

    public ListadoReposicionTableModel(List<Reposicion> reposiciones) {
        super();
        this.reposiciones = reposiciones;
        this.columns = new String[]{"Id", "Usuario", "Fecha Registro", "Descripcion", "Importe"};
    }
    
    @Override
    public int getRowCount() {
        return this.reposiciones.size();
    }

    @Override
    public int getColumnCount() {
        return this.columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reposicion reposicion = reposiciones.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return reposicion.getId_reposicionamiento();
            case 1:
                return reposicion.getTransaccion().getNombre_completo();
            case 2:
                return reposicion.getFecha_creacion();
            case 3:
                return reposicion.getTransaccion().getDescripcion();
            case 4:
                return reposicion.getTransaccion().getImporte();
            default:
                return null;
        }
    }
    
    public String getColumnName(int col) {
        return columns[col];
    }
    
    // Hace que solo la columna del botón sea editable
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 7;
    }
    
}
