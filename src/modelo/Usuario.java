package modelo;

public class Usuario {

    private int id_usuario;
    private String nombre_usuario;
    private String clave_usuario;
    private String nombre_completo;

    public Usuario() {}

    public Usuario(int id_usuario, String nombre_usuario, String clave_usuario, String nombre_completo) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.clave_usuario = clave_usuario;
        this.nombre_completo = nombre_completo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getClave_usuario() {
        return clave_usuario;
    }

    public void setClave_usuario(String clave_usuario) {
        this.clave_usuario = clave_usuario;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }
}
