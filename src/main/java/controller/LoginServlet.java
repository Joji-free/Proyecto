package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Usuario;
import services.UserService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si ya existe sesión con un usuario autenticado, redirigir a /productos
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }
        // Mostrar el formulario de login
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Leer parámetros como String
        String usuarioParam = request.getParameter("usuario");
        String contrasenaParam = request.getParameter("contrasena");

        // 2. Validar que no vengan vacíos o nulos
        if (usuarioParam == null || usuarioParam.trim().isEmpty()
                || contrasenaParam == null || contrasenaParam.trim().isEmpty()) {
            // Si falta alguno, reenviar al JSP con mensaje de error y precargar usuario
            request.setAttribute("error", "Debe ingresar usuario y contraseña.");
            request.setAttribute("usuario", usuarioParam != null ? usuarioParam : "");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // 3. Llamar al servicio para validar credenciales
        Usuario u = userService.validarUsuario(usuarioParam.trim(), contrasenaParam.trim());
        if (u != null) {
            // 4. Usuario válido: crear sesión, asignar rol e inicializar carrito
            HttpSession session = request.getSession();
            session.setAttribute("usuario", u.getUsuario());
            if (u.getUsuario().equalsIgnoreCase("admin")) {
                session.setAttribute("rol", "ADMIN");
            } else {
                session.setAttribute("rol", "USER");
            }
            session.setAttribute("cart", new java.util.HashMap<Integer, Integer>());

            response.sendRedirect(request.getContextPath() + "/productos");
        } else {
            // 5. Credenciales incorrectas: reenviar con mensaje y precargar usuario
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.setAttribute("usuario", usuarioParam);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
