package controlador;

import dao.BancoCuentaDAO;
import java.time.LocalDate;
import java.util.List;
import modelo.BancoCuenta;

public class BancoCuentaController {

    private final BancoCuentaDAO bancoCuentaDAO;

    public BancoCuentaController() {
        this.bancoCuentaDAO = new BancoCuentaDAO();
    }

    public Integer registrarBancoCuenta(BancoCuenta bancoCuenta) {
        return this.bancoCuentaDAO.registrarBancoCuenta(bancoCuenta);
    }

    public boolean actualizarBancoCuenta(BancoCuenta bancoCuenta) {
        return this.bancoCuentaDAO.actualizarBancoCuenta(bancoCuenta);
    }

    public boolean eliminarBancoCuenta(long idBancoCuenta) {
        return this.bancoCuentaDAO.eliminarBancoCuenta(idBancoCuenta);
    }

    public List<BancoCuenta> listarBancoCuenta(
            Long idBanco,
            String nroCuenta,
            String descripcion,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean estadoActivo) {
        return this.bancoCuentaDAO.buscarCuentasBancarias(idBanco, nroCuenta, descripcion, fechaDesde, fechaHasta, estadoActivo);
    }

    public BancoCuenta obtenerBancoCuentaPorId(long idBancoCuenta) {
        return this.bancoCuentaDAO.obteneBancoCuentaPorId(idBancoCuenta);
    }

    public boolean existeBancoCuenta(String nroCuenta) {
        return this.bancoCuentaDAO.existeBancoCuenta(nroCuenta);
    }

}
