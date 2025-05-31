package services;

import model.Usuario;
import repository.IUserRepository;
import repository.UserRepository;

public class UserService {
    private final IUserRepository userRepo = new UserRepository();

    public Usuario validarUsuario(String usuario, String contrasena) {
        return userRepo.validarUsuario(usuario, contrasena);
    }
}
