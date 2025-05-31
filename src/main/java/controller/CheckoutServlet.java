package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.DetalleFactura;
import model.Factura;
import model.Producto;
import services.InvoiceService;
import services.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("mensajeError", "El carrito está vacío.");
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            return;
        }

        String clienteNombre = request.getParameter("clienteNombre");
        String clienteCedula = request.getParameter("clienteCedula");

        // Recalcular total general
        BigDecimal totalGeneral = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int productoId = entry.getKey();
            int cantidad = entry.getValue();
            Producto p = productService.obtenerPorId(productoId);
            if (p != null) {
                BigDecimal subtotal = p.getPrecio().multiply(new BigDecimal(cantidad));
                totalGeneral = totalGeneral.add(subtotal);
            }
        }

        // Insertar factura
        Factura factura = new Factura();
        factura.setClienteNombre(clienteNombre);
        factura.setClienteCedula(clienteCedula);
        factura.setTotal(totalGeneral);

        int facturaId = invoiceService.insertarFactura(factura);
        if (facturaId == -1) {
            request.setAttribute("mensajeError", "Error al procesar la factura.");
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            return;
        }

        // Insertar los detalles y reducir stock
        boolean todoBien = true;
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int productoId = entry.getKey();
            int cantidad = entry.getValue();
            Producto p = productService.obtenerPorId(productoId);
            if (p != null) {
                BigDecimal precioUnitario = p.getPrecio();
                BigDecimal subtotal = precioUnitario.multiply(new BigDecimal(cantidad));

                DetalleFactura detalle = new DetalleFactura();
                detalle.setFacturaId(facturaId);
                detalle.setProductoId(productoId);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalle.setSubtotal(subtotal);

                boolean okDetalle = invoiceService.insertarDetalle(detalle);
                boolean okStock = invoiceService.reducirStock(productoId, cantidad);

                if (!okDetalle || !okStock) {
                    todoBien = false;
                    break;
                }
            }
        }

        if (!todoBien) {
            request.setAttribute("mensajeError", "Error al guardar detalles de la factura o al actualizar stock.");
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            return;
        }

        // Vaciar carrito en sesión
        session.setAttribute("cart", new java.util.HashMap<Integer, Integer>());

        request.setAttribute("mensajeExito", "Compra realizada con éxito. ID de factura: " + facturaId);
        request.setAttribute("invoiceId", facturaId);
        request.getRequestDispatcher("compraExitosa.jsp").forward(request, response);

    }
}
