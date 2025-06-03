<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Añadir Producto - Mimir Petshop</title>
  <link rel="stylesheet" href="css/addProduct.css">
</head>
<body>
<%
  // Verificar que la sesión exista y el rol sea ADMIN
  String rol = (String) session.getAttribute("rol");
  if (rol == null || !"ADMIN".equals(rol)) {
    response.sendRedirect("productos");
    return;
  }

  // Lista de errores enviada por el servlet
  java.util.List<String> errores = (java.util.List<String>) request.getAttribute("errores");

  // Valores previos para repoblar campos en caso de error
  String nombrePrev = request.getAttribute("nombre") != null ? (String) request.getAttribute("nombre") : "";
  String marcaPrev  = request.getAttribute("marca")  != null ? (String) request.getAttribute("marca")  : "";
  String precioPrev = request.getAttribute("precio") != null ? (String) request.getAttribute("precio") : "";
  String stockPrev  = request.getAttribute("stock")  != null ? (String) request.getAttribute("stock")  : "";
%>

<div class="form-container">
  <a href="productos" class="top-link">← Regresar a Productos</a>
  <h2>Añadir Nuevo Producto</h2>

  <% if (errores != null && !errores.isEmpty()) { %>
  <ul class="error-list">
    <% for (String err : errores) { %>
    <li><%= err %></li>
    <% } %>
  </ul>
  <% } %>

  <form action="addProduct" method="post">
    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" id="nombre" value="<%= nombrePrev %>" />

    <label for="marca">Marca:</label>
    <input type="text" name="marca" id="marca" value="<%= marcaPrev %>" />

    <label for="precio">Precio:</label>
    <input type="text" name="precio" id="precio" value="<%= precioPrev %>" />

    <label for="stock">Stock:</label>
    <input type="text" name="stock" id="stock" value="<%= stockPrev %>" />

    <button type="submit">Guardar Producto</button>
  </form>
</div>
</body>
</html>
