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
  @SuppressWarnings("unchecked")
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

  <form action="addProduct" method="post">
    <!-- Campo NOMBRE -->
    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" id="nombre" value="<%= nombrePrev %>" />
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
    <input type="text" name="marca" id="marca" value="<%= marcaPrev %>" />
    <% if (errores != null) {
      for (String err : errores) {
        if (err.toLowerCase().contains("marca")) { %>
    <span class="error-field"><%= err %></span>
    <%       }
    }
    }
    %>

    <!-- Campo PRECIO -->
    <label for="precio">Precio:</label>
    <input type="text" name="precio" id="precio" value="<%= precioPrev %>" />
    <% if (errores != null) {
      for (String err : errores) {
        if (err.toLowerCase().contains("precio")) { %>
    <span class="error-field"><%= err %></span>
    <%       }
    }
    }
    %>

    <!-- Campo STOCK -->
    <label for="stock">Stock:</label>
    <input type="text" name="stock" id="stock" value="<%= stockPrev %>" />
    <% if (errores != null) {
      for (String err : errores) {
        if (err.toLowerCase().contains("stock")) { %>
    <span class="error-field"><%= err %></span>
    <%       }
    }
    }
    %>

    <button type="submit">Guardar Producto</button>

    <!-- Errores generales (que no contengan ninguna de las palabras clave anteriores) -->
    <% if (errores != null) {
      for (String err : errores) {
        String low = err.toLowerCase();
        boolean esCampo = low.contains("nombre")
                || low.contains("marca")
                || low.contains("precio")
                || low.contains("stock");
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
