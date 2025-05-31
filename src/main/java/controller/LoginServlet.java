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
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        Usuario u = userService.validarUsuario(usuario, contrasena);
        if (u != null) {
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
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
