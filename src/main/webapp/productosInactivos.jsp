<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Producto" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Productos Inactivos - Mimir Petshop</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f9f9f9;
    }
    .top-bar {
      width: 90%;
      margin: 20px auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .btn {
      padding: 6px 12px;
      border: none;
      border-radius: 3px;
      cursor: pointer;
      font-size: 0.9rem;
    }
    .btn-reactivar { background-color: #4CAF50; color: white; }
    .btn-volver    { background-color: #555; color: white; }
    table {
      width: 90%;
      margin: 20px auto;
      border-collapse: collapse;
      background-color: white;
    }
    th, td {
      border: 1px solid #ddd;
      padding: 10px;
      text-align: center;
    }
    th { background-color: #f2f2f2; }

    /* Modal personalizado */
    #modalReactivar {
      display: none;
      position: fixed;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      z-index: 1000;
      justify-content: center;
      align-items: center;
    }

    #modalReactivar .modal-content {
      background: white;
      padding: 20px;
      border-radius: 8px;
      width: 300px;
      text-align: center;
    }

    #modalReactivar .modal-content h3 {
      margin-top: 0;
    }
  </style>
</head>
<body>
<%
  String rol = (String) session.getAttribute("rol");
  String usuario = (String) session.getAttribute("usuario");
  if (usuario == null || !"ADMIN".equals(rol)) {
    response.sendRedirect("productos");
    return;
  }

  List<Producto> inactivos = (List<Producto>) request.getAttribute("productosInactivos");
%>

<div class="top-bar">
  <div>
    <a href="productos"><button class="btn btn-volver">← Volver a Productos Activos</button></a>
  </div>
  <div>
    Bienvenido, <strong><%= usuario %></strong> (<%= rol %>)
  </div>
</div>

<h2 style="text-align: center; margin-top: 20px;">Productos Inactivos</h2>

<table>
  <thead>
  <tr>
    <th>Nombre</th>
    <th>Marca</th>
    <th>Precio</th>
    <th>Stock</th>
    <th>Acción</th>
  </tr>
  </thead>
  <tbody>
  <%
    if (inactivos != null && !inactivos.isEmpty()) {
      for (Producto p : inactivos) {
  %>
  <tr>
    <td><%= p.getNombre() %></td>
    <td><%= p.getMarca() %></td>
    <td>$ <%= p.getPrecio() %></td>
    <td><%= p.getStock() %></td>
    <td>
      <button class="btn btn-reactivar" onclick="confirmarReactivacion(<%= p.getId() %>)">
        Reactivar
      </button>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="5">No hay productos inactivos.</td>
  </tr>
  <% } %>
  </tbody>
</table>

<!-- Modal Reactivar -->
<div id="modalReactivar">
  <div class="modal-content">
    <h3>¿Reactivar producto?</h3>
    <p>El producto volverá a estar disponible para la venta.</p>
    <div style="margin-top: 20px;">
      <button onclick="reactivarProducto()" style="background-color:#4CAF50; color:white; padding:8px 16px; border:none; border-radius:4px;">Reactivar</button>
      <button onclick="cerrarModal()" style="margin-left:10px; padding:8px 16px; border:none; border-radius:4px;">Cancelar</button>
    </div>
  </div>
</div>

<script>
  let productoIdAReactivar = null;

  function confirmarReactivacion(id) {
    productoIdAReactivar = id;
    document.getElementById("modalReactivar").style.display = "flex";
  }

  function cerrarModal() {
    productoIdAReactivar = null;
    document.getElementById("modalReactivar").style.display = "none";
  }

  function reactivarProducto() {
    if (productoIdAReactivar !== null) {
      window.location.href = "activateProduct?id=" + productoIdAReactivar;
    }
  }
</script>

</body>
</html>
