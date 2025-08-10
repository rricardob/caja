package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaccion {

    private int id_transaccion;
    private int id_sesion;
    private int id_usuario;
    private int id_tipo;
    private int id_cliente;
    private BigDecimal importe;
    private String descripcion;
    private Timestamp fecha_creacion;

    public Transaccion() {
    }

    public Transaccion(int id_sesion, int id_usuario, int id_tipo, int id_cliente, BigDecimal importe, String descripcion) {
        this.id_sesion = id_sesion;
        this.id_usuario = id_usuario;
        this.id_tipo = id_tipo;
        this.id_cliente = id_cliente;
        this.importe = importe;
        this.descripcion = descripcion;
    }

    // getters & setters
    public int getId_transaccion() {
        return id_transaccion;
    }

    public void setId_transaccion(int id_transaccion) {
        this.id_transaccion = id_transaccion;
    }

    public int getId_sesion() {
        return id_sesion;
    }

    public void setId_sesion(int id_sesion) {
        this.id_sesion = id_sesion;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
