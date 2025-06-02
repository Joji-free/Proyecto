<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Producto, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Editar Producto - Mimir Petshop</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f5f5f5;
    }
    .form-container {
      width: 400px;
      margin: 50px auto;
      padding: 20px;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 4px;
    }
    input[type=text], input[type=number] {
      width: 100%;
      padding: 8px;
      margin: 5px 0 15px 0;
      border: 1px solid #ccc;
      box-sizing: border-box;
    }
    button {
      background-color: #2196F3;
      color: white;
      padding: 10px 0;
      width: 100%;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:hover { background-color: #1976D2; }
    .top-link {
      margin-bottom: 20px;
      display: block;
      text-align: center;
      text-decoration: none;
      color: #333;
    }
    .error-list {
      color: red;
      margin-bottom: 15px;
      list-style-type: disc;
      padding-left: 20px;
    }
  </style>
</head>
<body>
<%
  // 1) Verificar rol ADMIN
  String rol = (String) session.getAttribute("rol");
  if (rol == null || !"ADMIN".equals(rol)) {
    response.sendRedirect("productos");
    return;
  }

  // 2) Recuperar lista de errores, si el servlet la envió
  @SuppressWarnings("unchecked")
  List<String> errores = (List<String>) request.getAttribute("errores");

  // 3) Valores previos (en caso de reenvío con errores)
  String idPrev     = request.getAttribute("id")     != null ? request.getAttribute("id").toString() : null;
  String nombrePrev = (String) request.getAttribute("nombre");
  String marcaPrev  = (String) request.getAttribute("marca");
  String precioPrev = (String) request.getAttribute("precio");
  String stockPrev  = (String) request.getAttribute("stock");

  // 4) Si no hay errores, el servlet habrá puesto "producto" en request:
  Producto producto = (Producto) request.getAttribute("producto");

  // 5) Si no llegaron ni "producto" ni "idPrev", redirigir al listado
  if (producto == null && idPrev == null) {
    response.sendRedirect("productos");
    return;
  }

  // 6) Determinar valores definitivos para cada campo:
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
    <!-- ID oculto -->
    <input type="hidden" name="id" value="<%= idValue %>" />

    <label for="nombre">Nombre:</label>
    <input
            type="text"
            name="nombre"
            id="nombre"
            value="<%= nombreValue %>" />

    <label for="marca">Marca:</label>
    <input
            type="text"
            name="marca"
            id="marca"
            value="<%= marcaValue %>" />

    <label for="precio">Precio:</label>
    <input
            type="text"
            name="precio"
            id="precio"
            value="<%= precioValue %>" />

    <label for="stock">Stock:</label>
    <input
            type="text"
            name="stock"
            id="stock"
            value="<%= stockValue %>" />

    <button type="submit">Actualizar Producto</button>
  </form>
</div>
</body>
</html>
