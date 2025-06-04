package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Usuario;
import services.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuariosServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Verificar que exista sesión y sea ADMIN
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String rol = (String) session.getAttribute("rol");
        if (!"ADMIN".equals(rol)) {
            // Redirigir a /productos si no es admin
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        // 2. Obtener lista de usuarios desde la base de datos
        List<Usuario> listaUsuarios = userService.obtenerTodosLosUsuarios();
        request.setAttribute("usuarios", listaUsuarios);

        // 3. Forward al JSP que mostrará la tabla de usuarios
        // NOTA: aquí no se antepone request.getContextPath()
        request.getRequestDispatcher("/usuarios.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("crear".equalsIgnoreCase(action)) {
            crearUsuario(request, response);
        } else if ("editar".equalsIgnoreCase(action)) {
            editarUsuario(request, response);
        } else if ("eliminar".equalsIgnoreCase(action)) {
            eliminarUsuario(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios");
        }
    }

    private void crearUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener parámetros del formulario
        String nombreUsuario = request.getParameter("usuario");
        String contrasena     = request.getParameter("contrasena");

        // Validar parámetros mínimos
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("errorCrear", "Debe completar usuario y contraseña.");
            doGet(request, response);
            return;
        }

        // Crear nuevo Usuario
        Usuario nuevo = new Usuario();
        nuevo.setUsuario(nombreUsuario.trim());
        nuevo.setContrasena(contrasena.trim());
        boolean creado = userService.registrarUsuario(nuevo);

        if (!creado) {
            request.setAttribute("errorCrear", "No se pudo crear usuario (quizá ya existe).");
        } else {
            request.setAttribute("msgCrear", "Usuario creado correctamente.");
        }
        doGet(request, response);
    }

    private void editarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nuevoUsuario     = request.getParameter("usuario");
        String nuevaContrasena  = request.getParameter("contrasena");

        // Obtener el usuario actual
        Usuario existente = userService.buscarPorId(id);
        if (existente == null) {
            request.setAttribute("errorEditar", "El usuario no existe.");
            doGet(request, response);
            return;
        }

        // Actualizar datos
        existente.setUsuario(nuevoUsuario.trim());
        existente.setContrasena(nuevaContrasena.trim());
        boolean actualizado = userService.actualizarUsuario(existente);

        if (!actualizado) {
            request.setAttribute("errorEditar", "No se pudo actualizar datos del usuario.");
        } else {
            request.setAttribute("msgEditar", "Usuario actualizado correctamente.");
        }
        doGet(request, response);
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        boolean eliminado = userService.eliminarUsuarioPorId(id);
        if (!eliminado) {
            request.setAttribute("errorEliminar", "No se pudo eliminar el usuario.");
        } else {
            request.setAttribute("msgEliminar", "Usuario eliminado correctamente.");
        }
        doGet(request, response);
    }
}