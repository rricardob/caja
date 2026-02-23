
package modelo;

import java.sql.Timestamp;


public class BancoCuenta {
    
     private long id_cuenta;
     private Timestamp fecha;
     private String nro_cuenta;
     private String moneda;
     private String descripcion;
     private int estado;
     private long id_banco;

    public BancoCuenta() {
    }

    public BancoCuenta(long id_cuenta, Timestamp fecha, String nro_cuenta, String moneda, String descripcion, int estado, long id_banco) {
        this.id_cuenta = id_cuenta;
        this.fecha = fecha;
        this.nro_cuenta = nro_cuenta;
        this.moneda = moneda;
        this.descripcion = descripcion;
        this.estado = estado;
        this.id_banco = id_banco;
    }

    public long getId_cuenta() {
        return id_cuenta;
    }

    public void setId_cuenta(long id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getNro_cuenta() {
        return nro_cuenta;
    }

    public void setNro_cuenta(String nro_cuenta) {
        this.nro_cuenta = nro_cuenta;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public long getId_banco() {
        return id_banco;
    }

    public void setId_banco(long id_banco) {
        this.id_banco = id_banco;
    }
    
     
    @Override
    public String toString() {
        return descripcion == null ? "" : descripcion + " ("+ nro_cuenta + ")";
    }
     
    
}
