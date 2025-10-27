package modelo;

import java.sql.Timestamp;

public class TipoTransaccion {

    private long id_tipo;
    private String descripcion;
    private Boolean estado;
    private Timestamp fecha_creacion;
    private Timestamp fecha_actualizacion;
    private Long id_categoria_transacciones;

    public TipoTransaccion() {
    }

    public TipoTransaccion(long id_tipo, String descripcion, Boolean estado,
            Timestamp fecha_creacion, Timestamp fecha_actualizacion,
            Long id_categoria_transacciones) {
        this.id_tipo = id_tipo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha_creacion = fecha_creacion;
        this.fecha_actualizacion = fecha_actualizacion;
        this.id_categoria_transacciones = id_categoria_transacciones;
    }

    public long getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(long id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public Long getId_categoria_transacciones() {
        return id_categoria_transacciones;
    }

    public void setId_categoria_transacciones(Long id_categoria_transacciones) {
        this.id_categoria_transacciones = id_categoria_transacciones;
    }

    @Override
    public String toString() {
        return descripcion == null ? "" : descripcion;
    }

}
