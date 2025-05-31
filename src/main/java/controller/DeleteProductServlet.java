package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import services.ProductService;

import java.io.IOException;

@WebServlet("/deleteProduct")
public class DeleteProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));
        productService.eliminarProducto(id);
        response.sendRedirect(request.getContextPath() + "/productos");
    }
}
