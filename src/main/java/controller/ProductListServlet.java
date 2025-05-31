package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Producto;
import services.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet("/productos")
public class ProductListServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Producto> productos = productService.listarProductos();
        request.setAttribute("productos", productos);

        String rol = (String) session.getAttribute("rol");
        request.setAttribute("rol", rol);

        request.getRequestDispatcher("productos.jsp").forward(request, response);
    }
}
