package controlador;

import dao.SesionCajaDAO;
import dao.TransaccionDAO;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import modelo.SesionCaja;

public class CajaController {

    private final SesionCajaDAO sesionCajaDAO;
    private final TransaccionDAO transaccionDAO;

    public CajaController() {
        this.sesionCajaDAO = new SesionCajaDAO();
        this.transaccionDAO = new TransaccionDAO();
    }

    public BigDecimal calcularSaldoInicialParaNuevaApertura(Integer idUsuario) {
        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 1. Buscar último cierre del día actual
        SesionCaja ultimoCierreHoy = sesionCajaDAO.obtenerUltimoCierreDelDia(idUsuario, fechaHoy);
        if (ultimoCierreHoy != null) {
            return ultimoCierreHoy.getSaldoFinal();
        }

        // 2. Si no hay cierres hoy, buscar último cierre general
        SesionCaja ultimoCierreGeneral = sesionCajaDAO.obtenerUltimoCierreGeneral(idUsuario);
        if (ultimoCierreGeneral != null) {
            return ultimoCierreGeneral.getSaldoFinal();
        }

        // 3. Primera vez = 0
        return BigDecimal.ZERO;
    }

    public SesionCaja aperturarSesion(Integer idUsuario, BigDecimal montoAdicional) {
        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // VALIDACIÓN ESTRICTA: No puede haber sesión abierta
        boolean existeSesionAbierta = sesionCajaDAO.existeSesionAbiertaV2(idUsuario, fechaHoy);
        if (existeSesionAbierta) {
            throw new RuntimeException("❌ ERROR: Ya existe una sesión de caja abierta. Debe cerrarla antes de abrir una nueva.");
        }

        int numeroSesion = sesionCajaDAO.obtenerSiguienteNumeroSesionDia(idUsuario, fechaHoy);

        // Crear nueva sesión
        SesionCaja nuevaSesion = new SesionCaja(idUsuario, montoAdicional);
        nuevaSesion.setNumeroSesionDia(numeroSesion);

        // Guardar en BD
        Integer idGenerado = sesionCajaDAO.insertarSesion(nuevaSesion);
        if (Objects.isNull(idGenerado)) {
            throw new RuntimeException("❌ ERROR: No se obtuvo el registro de sesion!!");
        }
        nuevaSesion.setIdSesion(idGenerado);

        return nuevaSesion;
    }

    public SesionCaja cerrarSesion(Integer idSesion) {
        SesionCaja sesion = sesionCajaDAO.obtenerSesionPorId(idSesion);
        if (sesion == null) {
            throw new RuntimeException("❌ ERROR: Sesión no encontrada con ID: " + idSesion);
        }

        if (!"ABIERTA".equals(sesion.getEstado())) {
            throw new RuntimeException("❌ ERROR: La sesión ya está cerrada. No se puede cerrar nuevamente.");
        }
        
        boolean conteo = this.transaccionDAO.existeRegistrosPorProcesar(idSesion);

        //CALCULAR SALDO FINAL AUTOMÁTICAMENTE 
        BigDecimal saldoFinalCalculado = this.calcularSaldoFinalAutomatico(idSesion);

        // Calcular diferencia del turno
        BigDecimal diferenciaTurno = saldoFinalCalculado.subtract(sesion.getSaldoInicial());

        sesion.setHoraFin(new Timestamp(System.currentTimeMillis()));
        sesion.setSaldoFinal(saldoFinalCalculado);
        sesion.setEstado("CERRADA");
        sesion.setDiferenciaTurno(diferenciaTurno);

        boolean actualizado = sesionCajaDAO.actualizarSesion(sesion);
        if (!actualizado) {
            throw new RuntimeException("Error al cerrar la sesión");
        }
        
        if (conteo) {
            boolean transaccionesProcesadas = this.transaccionDAO.actualizarTransaccionesProcesadas(idSesion);
            if (!transaccionesProcesadas) {
                throw new RuntimeException("Error al actualizar transacciones procesadas");
            }
        }

        return sesion;
    }

    public SesionCaja obtenerSesionActivaActual(Integer idUsuario) {
        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return sesionCajaDAO.obtenerSesionActivaDelDia(idUsuario, fechaHoy);
    }

    public SesionCaja validarYObtenerSesionAbierta(Integer idUsuario) {
        SesionCaja sesionActiva = obtenerSesionActivaActual(idUsuario);
        if (sesionActiva == null) {
            throw new RuntimeException("❌ ERROR: No hay ninguna sesión de caja abierta. Debe aperturar caja primero.");
        }
        return sesionActiva;
    }

    public boolean puedeAperturarSesion(Integer idUsuario) {
        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return sesionCajaDAO.existeSesionAbiertaV2(idUsuario, fechaHoy);
    }

    public List<SesionCaja> obtenerHistorialDelDia(Integer idUsuario, String fecha) {
        return sesionCajaDAO.obtenerSesionesPorDia(idUsuario, fecha);
    }

    public BigDecimal calcularSaldoFinalAutomatico(Integer idSesion) {
        SesionCaja sesion = this.sesionCajaDAO.obtenerSesionPorId(idSesion);
        if (sesion == null) {
            throw new RuntimeException("Sesión no encontrada: " + idSesion);
        }

        return transaccionDAO.calcularSaldoFinalDeSesion(idSesion, sesion.getSaldoInicial());
    }
    
    public SesionCaja obtenerSesionCaja(int idUsuario){
        return this.sesionCajaDAO.obtenerSesionPorIdUsuario(idUsuario);
    }

}
