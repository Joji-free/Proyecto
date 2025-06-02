package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Producto;
import services.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/addProduct")
public class AddProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1) Verificar que la sesión exista y sea rol ADMIN
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }
        // 2) Simplemente mostramos el formulario
        request.getRequestDispatcher("addProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1) Verificar sesión y rol ADMIN
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        // 2) Leer parámetros como String
        String nombreParam = request.getParameter("nombre");
        String marcaParam  = request.getParameter("marca");
        String precioParam = request.getParameter("precio");
        String stockParam  = request.getParameter("stock");

        // 3) Lista para acumular mensajes de error
        List<String> errores = new ArrayList<>();

        // 4) Validar "nombre": no nulo, no vacío, longitud mínima
        if (nombreParam == null || nombreParam.trim().isEmpty()) {
            errores.add("El nombre del producto es obligatorio.");
        } else if (nombreParam.trim().length() < 2) {
            errores.add("El nombre debe tener al menos 2 caracteres.");
        }

        // 5) Validar "marca": no nulo, no vacío
        if (marcaParam == null || marcaParam.trim().isEmpty()) {
            errores.add("La marca del producto es obligatoria.");
        }

        // 6) Validar "precio": no nulo, convertible a BigDecimal, mayor que 0
        BigDecimal precio = null;
        if (precioParam == null || precioParam.trim().isEmpty()) {
            errores.add("El precio es obligatorio.");
        } else {
            try {
                precio = new BigDecimal(precioParam.trim());
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    errores.add("El precio debe ser un número mayor que 0.");
                }
            } catch (NumberFormatException ex) {
                errores.add("El precio debe ser un número válido (p. ej. 12.50).");
            }
        }

        // 7) Validar "stock": no nulo, convertible a entero, >= 0
        Integer stock = null;
        if (stockParam == null || stockParam.trim().isEmpty()) {
            errores.add("El campo stock es obligatorio.");
        } else {
            try {
                stock = Integer.parseInt(stockParam.trim());
                if (stock < 0) {
                    errores.add("El stock no puede ser negativo.");
                }
            } catch (NumberFormatException ex) {
                errores.add("El stock debe ser un número entero (p. ej. 5).");
            }
        }

        // 8) Si hay errores, reenviar al JSP con mensajes y valores ingresados
        if (!errores.isEmpty()) {
            request.setAttribute("errores", errores);
            request.setAttribute("nombre", nombreParam);
            request.setAttribute("marca", marcaParam);
            request.setAttribute("precio", precioParam);
            request.setAttribute("stock", stockParam);
            request.getRequestDispatcher("addProduct.jsp").forward(request, response);
            return;
        }

        // 9) Si no hay errores, crear objeto Producto y guardarlo
        Producto p = new Producto();
        p.setNombre(nombreParam.trim());
        p.setMarca(marcaParam.trim());
        p.setPrecio(precio);
        p.setStock(stock);

        productService.insertarProducto(p);

        // 10) Redirigir al listado de productos
        response.sendRedirect(request.getContextPath() + "/productos");
    }
}
