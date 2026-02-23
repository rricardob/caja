
package controlador;

import dao.BancoDepositoDAO;
import java.time.LocalDate;
import java.util.List;
import modelo.BancoDeposito;

public class BancoDepositoController {
    
    private final BancoDepositoDAO bancoDepositoDAO;

    public BancoDepositoController() {
        this.bancoDepositoDAO = new BancoDepositoDAO();
    }

    public Integer registrarBancoDeposito(BancoDeposito bancoDeposito) {
        return this.bancoDepositoDAO.registrarBancoDeposito(bancoDeposito);
    }

    public boolean actualizarBancoDeposito(BancoDeposito bancoDeposito) {
        return this.bancoDepositoDAO.actualizarBancoDeposito(bancoDeposito);
    }

    public boolean eliminarBancoDeposito(long idBancoDeposito) {
        return this.bancoDepositoDAO.eliminarBancoDeposito(idBancoDeposito);
    }

    public List<BancoDeposito> listarBancoDeposito(
            Long idCuenta,
            String nroVoucher,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean estadoActivo) {
        return this.bancoDepositoDAO.buscarBancoDeposito(idCuenta, nroVoucher, fechaDesde, fechaHasta, estadoActivo);
    }

    public BancoDeposito obtenerBancoDepositoPorId(long idBancoDeposito) {
        return this.bancoDepositoDAO.obteneBancoDepositoPorId(idBancoDeposito);
    }

    public boolean existeBancoDeposito(String nroVoucher) {
        return this.bancoDepositoDAO.existeBancoDeposito(nroVoucher);
    }
    
}
