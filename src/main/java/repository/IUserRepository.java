package repository;

import model.Usuario;
import java.util.List;

public interface IUserRepository {
    Usuario validarUsuario(String usuario, String contrasena);

    Usuario buscarPorUsuario(String usuario);

    boolean registrarUsuario(Usuario usuario);

    // ===========================
    // NUEVOS MÉTODOS PARA CRUD
    // ===========================
    /**
     * Retorna todos los usuarios de la tabla 'usuarios'.
     */
    List<Usuario> obtenerTodos();

    /**
     * Busca un usuario por su ID.
     * Devuelve el Usuario si existe, o null si no existe.
     */
    Usuario buscarPorId(int id);

    /**
     * Actualiza un usuario existente (nombre + contraseña).
     * Devuelve true si se actualizó correctamente.
     */
    boolean actualizarUsuario(Usuario usuario);

    /**
     * Elimina (o desactiva) un usuario por su ID.
     * Devuelve true si se eliminó/desactivó correctamente.
     */
    boolean eliminarUsuario(int id);
}
