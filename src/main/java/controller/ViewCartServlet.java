package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Producto;
import services.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet("/cart")
public class ViewCartServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int productoId = entry.getKey();
            int cantidad = entry.getValue();
            Producto p = productService.obtenerPorId(productoId);
            if (p != null) {
                BigDecimal subtotal = p.getPrecio().multiply(new BigDecimal(cantidad));
                totalGeneral = totalGeneral.add(subtotal);

                Map<String, Object> item = new HashMap<>();
                item.put("producto", p);
                item.put("cantidad", cantidad);
                item.put("subtotal", subtotal);
                items.add(item);
            }
        }

        request.setAttribute("items", items);
        request.setAttribute("totalGeneral", totalGeneral);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
}
