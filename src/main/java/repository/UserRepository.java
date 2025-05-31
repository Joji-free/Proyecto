// src/main/java/repository/UserRepository.java
package repository;

import model.Usuario;
import util.Conexion;   // ¡de nuevo usamos tu clase Conexion!

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository implements IUserRepository {

    @Override
    public Usuario validarUsuario(String usuario, String contrasena) {
        String sql = "SELECT id, usuario, contrasena FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setContrasena(rs.getString("contrasena"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
