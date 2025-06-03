<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Producto, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Editar Producto - Mimir Petshop</title>
  <link rel="stylesheet" href="css/editproduct.css">
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

  <% if (errores != null && !errores.isEmpty()) { %>
  <ul class="error-list">
    <% for (String err : errores) { %>
    <li><%= err %></li>
    <% } %>
  </ul>
  <% } %>

  <form action="editProduct" method="post">
    <input type="hidden" name="id" value="<%= idValue %>" />

    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" id="nombre" value="<%= nombreValue %>" />

    <label for="marca">Marca:</label>
    <input type="text" name="marca" id="marca" value="<%= marcaValue %>" />

    <label for="precio">Precio:</label>
    <input type="text" name="precio" id="precio" value="<%= precioValue %>" />

    <label for="stock">Stock:</label>
    <input type="text" name="stock" id="stock" value="<%= stockValue %>" />

    <button type="submit">Actualizar Producto</button>
  </form>
</div>
</body>
</html>
