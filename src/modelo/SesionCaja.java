
package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class SesionCaja {
    
    private Integer idSesion;
    private Integer idUsuario;
    private Timestamp horaInicio;
    private Timestamp horaFin;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;
    private String estado;
    private Integer numeroSesionDia;
    private BigDecimal diferenciaTurno;

    public SesionCaja() {
    }

    public SesionCaja(Integer idUsuario, BigDecimal saldoInicial) {
        this.idUsuario = idUsuario;
        this.saldoInicial = saldoInicial;
        this.horaInicio = new Timestamp(System.currentTimeMillis());
        this.estado = "ABIERTA";
        this.diferenciaTurno = BigDecimal.ZERO;
    }

    public Integer getIdSesion() { return idSesion; }
    public void setIdSesion(Integer idSesion) { this.idSesion = idSesion; }
    
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    
    public Timestamp getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Timestamp horaInicio) { this.horaInicio = horaInicio; }
    
    public Timestamp getHoraFin() { return horaFin; }
    public void setHoraFin(Timestamp horaFin) { this.horaFin = horaFin; }
    
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }
    
    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public Integer getNumeroSesionDia() { return numeroSesionDia; }
    public void setNumeroSesionDia(Integer numeroSesionDia) { this.numeroSesionDia = numeroSesionDia; }
    
    public BigDecimal getDiferenciaTurno() { return diferenciaTurno; }
    public void setDiferenciaTurno(BigDecimal diferenciaTurno) { this.diferenciaTurno = diferenciaTurno; }
    
    @Override
    public String toString() {
        return "SesionCaja{" +
                "idSesion=" + idSesion +
                ", idUsuario=" + idUsuario +
                ", horaInicio=" + horaInicio +
                ", estado='" + estado + '\'' +
                ", numeroSesionDia=" + numeroSesionDia +
                ", saldoInicial=" + saldoInicial +
                ", saldoFinal=" + saldoFinal +
                '}';
    }
    
}
