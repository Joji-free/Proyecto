package services;

import model.Usuario;
import repository.IUserRepository;
import repository.UserRepository;

import java.util.List;

public class UserService {
    private final IUserRepository userRepo = new UserRepository();

    public Usuario validarUsuario(String usuario, String contrasena) {
        return userRepo.validarUsuario(usuario, contrasena);
    }

    public boolean registrarUsuario(Usuario nuevoUsuario) {
        // 1. Verificar si ya existe un usuario con ese nombre
        Usuario existente = userRepo.buscarPorUsuario(nuevoUsuario.getUsuario());
        if (existente != null) {
            return false; // Ya existe el usuario
        }
        // 2. Guardar usuario
        return userRepo.registrarUsuario(nuevoUsuario);
    }

    // ============================
    // NUEVOS MÉTODOS PARA CRUD
    // ============================
    public List<Usuario> obtenerTodosLosUsuarios() {
        return userRepo.obtenerTodos();
    }

    public Usuario buscarPorId(int id) {
        return userRepo.buscarPorId(id);
    }

    public boolean actualizarUsuario(Usuario u) {
        return userRepo.actualizarUsuario(u);
    }

    public boolean eliminarUsuarioPorId(int id) {
        return userRepo.eliminarUsuario(id);
    }
}
