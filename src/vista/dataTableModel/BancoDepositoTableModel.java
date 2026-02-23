
package vista.dataTableModel;

import dao.BancoCuentaDAO;
import dao.RegistroBancoDAO;
import dao.UsuarioDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Banco;
import modelo.BancoCuenta;
import modelo.BancoDeposito;
import modelo.Usuario;


public class BancoDepositoTableModel extends AbstractTableModel {
    private final String[] columns;

    private final List<BancoDeposito> rows = new ArrayList<>();
    
    private RegistroBancoDAO bancoDao = new RegistroBancoDAO();
    private BancoCuentaDAO bancoCuentaDAO = new BancoCuentaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public BancoDepositoTableModel(List<BancoDeposito> bancoDepositos) {
        super();
        this.columns = new String[]{"Id", "Fecha", "Nro Voucher","Cuenta","Banco","Importe", "Usuario", "Terminal", "Estado"};
        if (bancoDepositos != null) {
            this.rows.addAll(bancoDepositos);
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
        BancoDeposito bancoDeposito = rows.get(rowIndex);
        BancoCuenta bancoCuenta = this.bancoCuentaDAO.obteneBancoCuentaPorId(bancoDeposito.getId_cuenta());
        Banco banco = this.bancoDao.obteneBancoPorId(bancoCuenta.getId_banco());
        Usuario usuario = this.usuarioDAO.infoUsuario(Integer.parseInt(String.valueOf(bancoDeposito.getId_usuario())));
        
        switch (columnIndex) {
            case 0:
                return bancoDeposito.getId_deposito();
            case 1:
                return bancoDeposito.getFecha();
            case 2:
                return bancoDeposito.getNro_voucher();
            case 3:
                return bancoCuenta.getNro_cuenta();
            case 4:
                return banco.getDescripcion();
            case 5:
                return bancoDeposito.getImporte();
            case 6:
                return usuario.getNombre_usuario();
            case 7:
                return bancoDeposito.getTerminal();
            case 8:
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
                return Object.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return Double.class;
            case 6:
                return String.class;
            case 7:
                return String.class;
            case 8:
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    public void load(List<BancoDeposito> lista) {
        rows.clear();
        if (lista != null) {
            rows.addAll(lista);
        }
        fireTableDataChanged();
    }

    public BancoDeposito getBancoAt(int row) {
        if (row < 0 || row >= rows.size()) {
            return null;
        }
        return rows.get(row);
    }
}
