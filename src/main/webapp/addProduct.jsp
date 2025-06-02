<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Añadir Producto - Mimir Petshop</title>
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
      background-color: #4CAF50;
      color: white;
      padding: 10px 0;
      width: 100%;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:hover { background-color: #45a049; }
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
  // 1) Verificar que la sesión exista y sea rol ADMIN
  String rol = (String) session.getAttribute("rol");
  if (rol == null || !"ADMIN".equals(rol)) {
    response.sendRedirect("productos");
    return;
  }

  // 2) Leer lista de errores (List<String>) que pueda haber puesto el servlet
  java.util.List<String> errores = (java.util.List<String>) request.getAttribute("errores");

  // 3) Obtener valores previos para recargar (en caso de haber errores)
  String nombrePrev = request.getAttribute("nombre") != null
          ? (String) request.getAttribute("nombre")
          : "";
  String marcaPrev = request.getAttribute("marca") != null
          ? (String) request.getAttribute("marca")
          : "";
  String precioPrev = request.getAttribute("precio") != null
          ? (String) request.getAttribute("precio")
          : "";
  String stockPrev = request.getAttribute("stock") != null
          ? (String) request.getAttribute("stock")
          : "";
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
    <input
            type="text"
            name="nombre"
            id="nombre"
            value="<%= nombrePrev %>" />

    <label for="marca">Marca:</label>
    <input
            type="text"
            name="marca"
            id="marca"
            value="<%= marcaPrev %>" />

    <label for="precio">Precio:</label>
    <input
            type="text"
            name="precio"
            id="precio"
            value="<%= precioPrev %>" />

    <label for="stock">Stock:</label>
    <input
            type="text"
            name="stock"
            id="stock"
            value="<%= stockPrev %>" />

    <button type="submit">Guardar Producto</button>
  </form>
</div>
</body>
</html>
