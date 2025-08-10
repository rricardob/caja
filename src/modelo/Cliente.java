package modelo;

public class Cliente {

    private int id_cliente;
    private String nombre_completo;
    private String direccion;
    private String doc_identidad;

    public Cliente() {
    }

    public Cliente(int id_cliente, String nombre_completo, String direccion, String doc_identidad) {
        this.id_cliente = id_cliente;
        this.nombre_completo = nombre_completo;
        this.direccion = direccion;
        this.doc_identidad = doc_identidad;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDoc_identidad() {
        return doc_identidad;
    }

    public void setDoc_identidad(String doc_identidad) {
        this.doc_identidad = doc_identidad;
    }
}
