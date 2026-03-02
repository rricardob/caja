package modelo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Cheque {

    private long id_cheque;
    private long id_usuario;
    private String nro_cheque;
    private Date fecha_emision;
    private BigDecimal saldo_anterior;
    private BigDecimal total_cheque;
    private Timestamp fecha_creacion;
    private Timestamp fecha_actualizacion;

    // Campo adicional para auditoría (nombre del usuario que lo creó)
    private transient String nombre_usuario;

    public Cheque() {
    }

    public Cheque(long id_usuario, String nro_cheque, Date fecha_emision, BigDecimal saldo_anterior,
            BigDecimal total_cheque) {
        this.id_usuario = id_usuario;
        this.nro_cheque = nro_cheque;
        this.fecha_emision = fecha_emision;
        this.saldo_anterior = saldo_anterior;
        this.total_cheque = total_cheque;
    }

    // Getters and Setters
    public long getId_cheque() {
        return id_cheque;
    }

    public void setId_cheque(long id_cheque) {
        this.id_cheque = id_cheque;
    }

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNro_cheque() {
        return nro_cheque;
    }

    public void setNro_cheque(String nro_cheque) {
        this.nro_cheque = nro_cheque;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Date fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public BigDecimal getSaldo_anterior() {
        return saldo_anterior;
    }

    public void setSaldo_anterior(BigDecimal saldo_anterior) {
        this.saldo_anterior = saldo_anterior;
    }

    public BigDecimal getTotal_cheque() {
        return total_cheque;
    }

    public void setTotal_cheque(BigDecimal total_cheque) {
        this.total_cheque = total_cheque;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(Timestamp fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }
}
