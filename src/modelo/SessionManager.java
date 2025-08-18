/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private static SessionManager instance;
    private int idUsuario;
    private String nombreUsuario;
    private int idRol;
    private String nombreRol;
    private Set<String> permisosVentanas;
    private Integer idSesionCaja;
    
    private SessionManager() {
        permisosVentanas = new HashSet<>();
    }
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    // Métodos para establecer datos de sesión
    public void iniciarSesion(int idUsuario, String nombreUsuario, int idRol, String nombreRol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }
    
    public void cerrarSesion() {
        this.idUsuario = 0;
        this.nombreUsuario = null;
        this.idRol = 0;
        this.nombreRol = null;
        this.permisosVentanas.clear();
        this.idSesionCaja = 0;
    }
    
    // Métodos para manejar permisos
    public void setPermisos(Set<String> permisos) {
        this.permisosVentanas = permisos;
    }
    
    public boolean tienePermiso(String nombreVentana) {
        return permisosVentanas.contains(nombreVentana);
    }
    
    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public int getIdRol() { return idRol; }
    public String getNombreRol() { return nombreRol; }
    public Set<String> getPermisosVentanas() { return permisosVentanas; }
    public Integer getIdSesionCaja() { return idSesionCaja; }
    
    // Método para verificar si hay sesión activa
    public boolean sesionActiva() {
        return idUsuario > 0 && nombreUsuario != null;
    }

    public void setIdSesionCaja(Integer idSesionCaja) {
        this.idSesionCaja = idSesionCaja;
    }
    
    
}
