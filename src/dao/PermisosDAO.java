/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import util.ConexionDB;

public class PermisosDAO {
   
    
    /**
     * Obtiene los permisos de ventanas para un rol específico
     * @param idRol ID del rol
     * @return Set con nombres de ventanas permitidas
     */
    public Set<String> obtenerPermisosPorRol(int idRol) {
        Set<String> permisos = new HashSet<>();
        String sql = "SELECT nombre_ventana FROM permisos WHERE id_rol = ? AND puede_acceder = TRUE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idRol);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                permisos.add(rs.getString("nombre_ventana"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener permisos: " + e.getMessage());
        }
        
        return permisos;
    }
    
    /**
     * Verifica si un rol tiene permiso para acceder a una ventana específica
     * @param idRol ID del rol
     * @param nombreVentana Nombre de la ventana
     * @return true si tiene permiso, false caso contrario
     */
    public boolean tienePermiso(int idRol, String nombreVentana) {
        String sql = "SELECT puede_acceder FROM permisos WHERE id_rol = ? AND nombre_ventana = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idRol);
            pst.setString(2, nombreVentana);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("puede_acceder");
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar permiso: " + e.getMessage());
        }
        
        return false; // Por defecto, no tiene permiso
    }
}