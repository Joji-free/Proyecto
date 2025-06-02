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

@WebServlet("/editProduct")
public class EditProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1) Verificar sesión y rol ADMIN
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        // 2) Leer y parsear el parámetro "id"
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        // 3) Obtener el producto de la BD
        Producto p = productService.obtenerPorId(id);
        if (p == null) {
            response.sendRedirect(request.getContextPath() + "/productos");
            return;
        }

        // 4) Enviar el producto al JSP
        request.setAttribute("producto", p);
        request.getRequestDispatcher("editProduct.jsp").forward(request, response);
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
        String idParam     = request.getParameter("id");
        String nombreParam = request.getParameter("nombre");
        String marcaParam  = request.getParameter("marca");
        String precioParam = request.getParameter("precio");
        String stockParam  = request.getParameter("stock");

        List<String> errores = new ArrayList<>();

        // 3) Validar ID y obtener producto original
        Integer id = null;
        if (idParam == null || idParam.trim().isEmpty()) {
            errores.add("Identificador de producto faltante.");
        } else {
            try {
                id = Integer.parseInt(idParam.trim());
                if (id <= 0) {
                    errores.add("Identificador de producto inválido.");
                }
            } catch (NumberFormatException e) {
                errores.add("Identificador de producto inválido.");
            }
        }

        Producto original = null;
        if (id != null) {
            original = productService.obtenerPorId(id);
            if (original == null) {
                errores.add("El producto no existe.");
            }
        }

        // 4) Validar "nombre"
        if (nombreParam == null || nombreParam.trim().isEmpty()) {
            errores.add("El nombre del producto es obligatorio.");
        } else if (nombreParam.trim().length() < 2) {
            errores.add("El nombre debe tener al menos 2 caracteres.");
        }

        // 5) Validar "marca"
        if (marcaParam == null || marcaParam.trim().isEmpty()) {
            errores.add("La marca del producto es obligatoria.");
        }

        // 6) Validar "precio"
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

        // 7) Validar "stock"
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

        // 8) Validación EXTRA: si no hay errores previos y existe 'original',
        //    verificar que al menos uno de los campos haya cambiado
        if (errores.isEmpty() && original != null) {
            boolean mismoNombre = nombreParam.trim().equals(original.getNombre());
            boolean mismaMarca  = marcaParam.trim().equals(original.getMarca());
            boolean mismoPrecio = (precio != null) && precio.compareTo(original.getPrecio()) == 0;
            boolean mismoStock  = (stock != null) && stock.equals(original.getStock());

            if (mismoNombre && mismaMarca && mismoPrecio && mismoStock) {
                errores.add("Tienes que actualizar algo si vas a editar.");
            }
        }

        // 9) Si hay errores, reenviar a editProduct.jsp con mensajes y valores previos
        if (!errores.isEmpty()) {
            request.setAttribute("errores", errores);
            request.setAttribute("id", idParam);
            request.setAttribute("nombre", nombreParam);
            request.setAttribute("marca", marcaParam);
            request.setAttribute("precio", precioParam);
            request.setAttribute("stock", stockParam);
            // No hace falta volver a setear "producto", porque JSP usará los valores anteriores
            request.getRequestDispatcher("editProduct.jsp").forward(request, response);
            return;
        }

        // 10) Si no hay errores ni requerimiento de cambiar algo, actualizar el producto
        Producto actualizado = new Producto();
        actualizado.setId(id);
        actualizado.setNombre(nombreParam.trim());
        actualizado.setMarca(marcaParam.trim());
        actualizado.setPrecio(precio);
        actualizado.setStock(stock);

        productService.actualizarProducto(actualizado);

        // 11) Redirigir al listado de productos
        response.sendRedirect(request.getContextPath() + "/productos");
    }
}
