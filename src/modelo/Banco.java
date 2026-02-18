
package modelo;


public class Banco {
    
    private long id_banco;
    private String descripcion;
    private int estado;

    public Banco() {
    }

    public Banco(long id_cliente, String descripcion, int estado) {
        this.id_banco = id_cliente;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public long getId_banco() {
        return id_banco;
    }

    public void setId_banco(long id_banco) {
        this.id_banco = id_banco;
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
    
}
