package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Producto;
import services.ProductService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/addProduct")
public class AddProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }
        request.getRequestDispatcher("addProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }
        String nombre = request.getParameter("nombre");
        String marca = request.getParameter("marca");
        BigDecimal precio = new BigDecimal(request.getParameter("precio"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setMarca(marca);
        p.setPrecio(precio);
        p.setStock(stock);

        productService.insertarProducto(p);
        response.sendRedirect(request.getContextPath() + "/productos");
    }
}
