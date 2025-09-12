
package modelo;

import java.sql.Timestamp;


public class Reposicion {
    
    private int id_reposicionamiento;
    private Transaccion transaccion;
    private int aprobado_por;
    private boolean mostrado_en_pantalla;
    private Timestamp fecha_creacion;
    private Timestamp fecha_aprobacion;

    public Reposicion() {
    }

    public Reposicion(int id_reposicionamiento, Transaccion transaccion, int aprobado_por, boolean mostrado_en_pantalla, Timestamp fecha_creacion, Timestamp fecha_aprobacion) {
        this.id_reposicionamiento = id_reposicionamiento;
        this.transaccion = transaccion;
        this.aprobado_por = aprobado_por;
        this.mostrado_en_pantalla = mostrado_en_pantalla;
        this.fecha_creacion = fecha_creacion;
        this.fecha_aprobacion = fecha_aprobacion;
    }

    public int getId_reposicionamiento() {
        return id_reposicionamiento;
    }

    public void setId_reposicionamiento(int id_reposicionamiento) {
        this.id_reposicionamiento = id_reposicionamiento;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    public int getAprobado_por() {
        return aprobado_por;
    }

    public void setAprobado_por(int aprobado_por) {
        this.aprobado_por = aprobado_por;
    }

    public boolean isMostrado_en_pantalla() {
        return mostrado_en_pantalla;
    }

    public void setMostrado_en_pantalla(boolean mostrado_en_pantalla) {
        this.mostrado_en_pantalla = mostrado_en_pantalla;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getFecha_aprobacion() {
        return fecha_aprobacion;
    }

    public void setFecha_aprobacion(Timestamp fecha_aprobacion) {
        this.fecha_aprobacion = fecha_aprobacion;
    }
    
    @Override
    public String toString() {
        return "Reposicion{" + "id_reposicionamiento=" + id_reposicionamiento + ", transaccion=" + transaccion + ", aprobado_por=" + aprobado_por + ", mostrado_en_pantalla=" + mostrado_en_pantalla + ", fecha_creacion=" + fecha_creacion + '}';
    }
    
    
}
