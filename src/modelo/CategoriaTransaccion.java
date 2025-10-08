package modelo;

public class CategoriaTransaccion {

    private Long id_categoria_transacciones;
    private String descripcion;

    public CategoriaTransaccion() {
    }

    public CategoriaTransaccion(Long id, String descripcion) {
        this.id_categoria_transacciones = id;
        this.descripcion = descripcion;
    }

    public Long getId_categoria_transacciones() {
        return id_categoria_transacciones;
    }

    public void setId_categoria_transacciones(Long id) {
        this.id_categoria_transacciones = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion == null ? "" : descripcion;
    }

}
