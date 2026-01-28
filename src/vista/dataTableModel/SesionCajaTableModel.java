package vista.dataTableModel;

import dao.UsuarioDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.SesionCaja;

public class SesionCajaTableModel extends AbstractTableModel {

    private final String[] columns;
    private final UsuarioDAO usuarioDAO;

    private final List<SesionCaja> rows = new ArrayList<>();

    public SesionCajaTableModel(List<SesionCaja> aSesionCajas, UsuarioDAO usuarioDAO) {
        super();
        this.usuarioDAO = usuarioDAO;
        this.columns = new String[]{"Id", "Usuario", "Hora Inicio", "Hora Fin", "Saldo Inicial", "Saldo Final", "Estado"};
        if (aSesionCajas != null) {
            this.rows.addAll(aSesionCajas);
        }
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        SesionCaja sesionCaja = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return sesionCaja.getIdSesion();
            case 1:
                return this.usuarioDAO.infoUsuario(sesionCaja.getIdUsuario()).getNombre_completo();
            case 2:
                return sesionCaja.getHoraInicio();
            case 3:
                return sesionCaja.getHoraFin();
            case 4:
                return sesionCaja.getSaldoInicial();
            case 5:
                return sesionCaja.getSaldoFinal();
            case 6:
                return sesionCaja.getEstado();
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
            case 3:
                return java.sql.Timestamp.class;
            case 4:
            case 5:
                return Double.class;
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

    public void load(List<SesionCaja> lista) {
        rows.clear();
        if (lista != null) {
            rows.addAll(lista);
        }
        fireTableDataChanged();
    }

    public SesionCaja getSesionAt(int row) {
        if (row < 0 || row >= rows.size()) {
            return null;
        }
        return rows.get(row);
    }
}
