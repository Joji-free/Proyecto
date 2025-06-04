<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Producto, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Editar Producto - Mimir Petshop</title>
  <link rel="stylesheet" href="css/editproduct.css">
  <style>
    /* Estilo para los mensajes de error debajo de cada input */
    .error-field {
      display: block;
      color: #c0392b;
      font-size: 0.9em;
      margin-top: 4px;
      margin-bottom: 12px;
    }
    /* Si quieres un mensaje genérico que aparezca al final */
    .error-general {
      display: block;
      color: #c0392b;
      font-size: 1em;
      margin-top: 12px;
      margin-bottom: 12px;
      text-align: center;
    }
  </style>
</head>
<body>
<%
  String rol = (String) session.getAttribute("rol");
  if (rol == null || !"ADMIN".equals(rol)) {
    response.sendRedirect("productos");
    return;
  }

  @SuppressWarnings("unchecked")
  List<String> errores = (List<String>) request.getAttribute("errores");

  String idPrev     = request.getAttribute("id")     != null ? request.getAttribute("id").toString() : null;
  String nombrePrev = (String) request.getAttribute("nombre");
  String marcaPrev  = (String) request.getAttribute("marca");
  String precioPrev = (String) request.getAttribute("precio");
  String stockPrev  = (String) request.getAttribute("stock");

  Producto producto = (Producto) request.getAttribute("producto");

  if (producto == null && idPrev == null) {
    response.sendRedirect("productos");
    return;
  }

  String idValue     = idPrev != null ? idPrev : String.valueOf(producto.getId());
  String nombreValue = nombrePrev != null ? nombrePrev : producto.getNombre();
  String marcaValue  = marcaPrev  != null ? marcaPrev  : producto.getMarca();
  String precioValue = precioPrev != null ? precioPrev : producto.getPrecio().toString();
  String stockValue  = stockPrev  != null ? stockPrev  : String.valueOf(producto.getStock());
%>

<div class="form-container">
  <a href="productos" class="top-link">← Regresar a Productos</a>
  <h2>Editar Producto</h2>

  <form action="editProduct" method="post">
    <input type="hidden" name="id" value="<%= idValue %>" />

    <!-- Campo NOMBRE -->
    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" id="nombre" value="<%= nombreValue %>" />
    <% if (errores != null) {
      for (String err : errores) {
        if (err.toLowerCase().contains("nombre")) { %>
    <span class="error-field"><%= err %></span>
    <%       }
    }
    }
    %>

    <!-- Campo MARCA -->
    <label for="marca">Marca:</label>
    <input type="text" name="marca" id="marca" value="<%= marcaValue %>" />
    <% if (errores != null) {
      for (String err : errores) {
        // Asumimos que los mensajes de error relacionados a 'marca' contienen esa palabra
        if (err.toLowerCase().contains("marca")) { %>
    <span class="error-field"><%= err %></span>
    <%       }
    }
    }
    %>

    <!-- Campo PRECIO -->
    <label for="precio">Precio:</label>
    <input type="text" name="precio" id="precio" value="<%= precioValue %>" />
    <% if (errores != null) {
      for (String err : errores) {
        // Mensajes que contienen 'precio'
        if (err.toLowerCase().contains("precio")) { %>
    <span class="error-field"><%= err %></span>
    <%       }
    }
    }
    %>

    <!-- Campo STOCK -->
    <label for="stock">Stock:</label>
    <input type="text" name="stock" id="stock" value="<%= stockValue %>" />
    <% if (errores != null) {
      for (String err : errores) {
        // Mensajes que contienen 'stock'
        if (err.toLowerCase().contains("stock")) { %>
    <span class="error-field"><%= err %></span>
    <%       }
    }
    }
    %>

    <button type="submit">Actualizar Producto</button>

    <!-- Mostrar aquí cualquier error que no se haya categorizado por campo,
         por ejemplo: "Tienes que actualizar algo si vas a editar." -->
    <% if (errores != null) {
      for (String err : errores) {
        String low = err.toLowerCase();
        boolean esCampo = low.contains("nombre") || low.contains("marca")
                || low.contains("precio") || low.contains("stock");
        if (!esCampo) { %>
    <span class="error-general"><%= err %></span>
    <%       }
    }
    }
    %>
  </form>
</div>
</body>
</html>
