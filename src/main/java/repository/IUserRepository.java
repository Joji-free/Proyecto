// src/main/java/repository/IUserRepository.java
package repository;

import model.Usuario;

public interface IUserRepository {
    /**
     * Valida si existe un usuario con esa contraseña en la tabla 'usuarios'.
     * Devuelve un Usuario si coincide, o null si no existe.
     */
    Usuario validarUsuario(String usuario, String contrasena);
}
