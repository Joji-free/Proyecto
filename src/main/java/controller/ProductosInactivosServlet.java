package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Producto;
import services.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet("/productosInactivos")
public class ProductosInactivosServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Solo ADMIN puede ver inactivos
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        // Obtener lista de inactivos
        List<Producto> inactivos = productService.listarProductosInactivos();
        request.setAttribute("productosInactivos", inactivos);

        // Enviar al JSP correspondiente
        request.getRequestDispatcher("productosInactivos.jsp").forward(request, response);
    }
}
