package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import services.ProductService;

import java.io.IOException;

@WebServlet("/activateProduct")
public class ActivateProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Solo ADMIN puede reactivar
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam.trim());
                productService.activarProducto(id); // pone activo = 1
            } catch (NumberFormatException e) {
                // Ignorar o loguear el error de parseo
            }
        }

        // Redirigir de nuevo a la lista de inactivos
        response.sendRedirect(request.getContextPath() + "/productosInactivos");
    }
}
