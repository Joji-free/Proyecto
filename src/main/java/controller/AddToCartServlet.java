package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("productoId");
        String cantidadParam = request.getParameter("cantidad");

        if (idParam == null || cantidadParam == null) {
            response.sendRedirect(request.getContextPath() + "/productos?error=missingParams");
            return;
        }

        int productoId;
        int cantidad;

        try {
            productoId = Integer.parseInt(idParam.trim());
            cantidad = Integer.parseInt(cantidadParam.trim());

            if (cantidad <= 0) {
                throw new NumberFormatException("Cantidad debe ser mayor a 0");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/productos?error=invalidInput");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new java.util.HashMap<>();
        }

        int cantidadPrevia = cart.getOrDefault(productoId, 0);
        cart.put(productoId, cantidadPrevia + cantidad);

        session.setAttribute("cart", cart);
        response.sendRedirect(request.getContextPath() + "/productos?success=added");
    }
}
