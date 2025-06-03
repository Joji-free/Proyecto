<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Producto" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Productos - Mimir Petshop</title>
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
    .btn-comprar   { background-color: #4CAF50; color: white; }
    .btn-editar    { background-color: #2196F3; color: white; }
    .btn-eliminar  { background-color: #f44336; color: white; }
    .btn-add       { background-color: #555;  color: white; }
    .btn-inactivos { background-color: #888;  color: white; margin-left: 10px; }
    .action-bar {
      width: 90%;
      margin: 0 auto 15px auto;
      display: flex;
      justify-content: flex-start;
    }
    table {
      width: 90%;
      margin: 0 auto 30px auto;
      border-collapse: collapse;
      background-color: white;
    }
    th, td {
      border: 1px solid #ddd;
      padding: 10px;
      text-align: center;
    }
    th { background-color: #f2f2f2; }

    /* Modal para eliminar producto */
    #modalEliminar {
      display: none;
      position: fixed;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      z-index: 1000;
      justify-content: center;
      align-items: center;
    }

    #modalEliminar .modal-content {
      background: white;
      padding: 20px;
      border-radius: 8px;
      width: 300px;
      text-align: center;
    }

    #modalEliminar .modal-content h3 {
      margin-top: 0;
    }

    /* Modal para error de stock */
    #modalStockError {
      display: none;
      position: fixed;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      z-index: 1001;
      justify-content: center;
      align-items: center;
    }
    #modalStockError > div {
      background: white;
      padding: 20px;
      border-radius: 8px;
      width: 320px;
      text-align: center;
    }
    #modalStockError button {
      margin-top: 20px;
      padding: 8px 16px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
  </style>
</head>
<body>
<%
  List<Producto> productos = (List<Producto>) request.getAttribute("productos");
  String rol     = (String) session.getAttribute("rol");
  String usuario = (String) session.getAttribute("usuario");
  String error   = request.getParameter("error");

  if (usuario == null) {
    response.sendRedirect("login");
    return;
  }
%>

<div class="top-bar">
  <div>
    Bienvenido, <strong><%= usuario %></strong> (<%= rol %>)
  </div>
  <div>
    <a href="cart"><button class="btn">Ver Carrito</button></a>
    <a href="logout"><button class="btn">Cerrar Sesión</button></a>
  </div>
</div>

<% if ("ADMIN".equals(rol)) { %>
<div class="action-bar">
  <a href="addProduct"><button class="btn btn-add">+ Añadir Producto</button></a>
  <a href="productosInactivos"><button class="btn btn-inactivos">Ver Inactivos</button></a>
</div>
<% } %>

<% if (error != null) { %>
<p style="color: red; text-align: center;">
  <%=
  "missingParams".equals(error) ? "Faltan datos del formulario." :
          "invalidInput".equals(error) ? "Datos inválidos. Verifica la cantidad." :
                  "Ocurrió un error."
  %>
</p>
<% } else if ("added".equals(request.getParameter("success"))) { %>
<p style="color: green; text-align: center;">
  Producto añadido al carrito correctamente.
</p>
<% } %>

<table>
  <thead>
  <tr>
    <th>Nombre</th>
    <th>Marca</th>
    <th>Precio</th>
    <th>Stock</th>
    <th>Acciones</th>
  </tr>
  </thead>
  <tbody>
  <%
    if (productos != null) {
      for (Producto prod : productos) {
  %>
  <tr>
    <td><%= prod.getNombre() %></td>
    <td><%= prod.getMarca() %></td>
    <td>$ <%= prod.getPrecio() %></td>
    <td><%= prod.getStock() %></td>
    <td>
      <form action="addToCart" method="post" style="display:inline-block;" onsubmit="return validarCantidad(this);">
        <input type="hidden" name="productoId" value="<%= prod.getId() %>" />
        <input type="number" name="cantidad" value="1" min="1" data-stock="<%= prod.getStock() %>" style="width: 50px;" />
        <button type="submit" class="btn btn-comprar">Comprar</button>
      </form>
      <% if ("ADMIN".equals(rol)) { %>
      <a href="editProduct?id=<%= prod.getId() %>">
        <button class="btn btn-editar">Editar</button>
      </a>
      <button type="button" class="btn btn-eliminar" onclick="confirmarEliminacion(<%= prod.getId() %>)">
        Eliminar
      </button>
      <% } %>
    </td>
  </tr>
  <%
      }
    }
  %>
  </tbody>
</table>

<!-- Modal para eliminar producto -->
<div id="modalEliminar">
  <div class="modal-content">
    <h3>¿Estás seguro?</h3>
    <p>Esta acción desactivará el producto.</p>
    <div style="margin-top: 20px;">
      <button onclick="eliminarProducto()" style="background-color:#f44336; color:white; padding:8px 16px; border:none; border-radius:4px;">Eliminar</button>
      <button onclick="cerrarModal()" style="margin-left:10px; padding:8px 16px; border:none; border-radius:4px;">Cancelar</button>
    </div>
  </div>
</div>

<!-- Modal para error de stock -->
<div id="modalStockError">
  <div>
    <h3>No puedes superar la cantidad que tenemos disponible</h3>
    <button onclick="cerrarModalStock()">Cerrar</button>
  </div>
</div>

<script>
  let productoIdAEliminar = null;

  function confirmarEliminacion(id) {
    productoIdAEliminar = id;
    document.getElementById('modalEliminar').style.display = 'flex';
  }

  function cerrarModal() {
    productoIdAEliminar = null;
    document.getElementById('modalEliminar').style.display = 'none';
  }

  function eliminarProducto() {
    if (productoIdAEliminar !== null) {
      window.location.href = 'deleteProduct?id=' + productoIdAEliminar;
    }
  }

  // Validar cantidad antes de enviar el formulario
  function validarCantidad(form) {
    const cantidadInput = form.querySelector('input[name="cantidad"]');
    const cantidad = parseInt(cantidadInput.value);
    const stock = parseInt(cantidadInput.getAttribute('data-stock'));

    if (isNaN(cantidad) || cantidad < 1) {
      alert("Ingresa una cantidad válida (mayor o igual a 1).");
      return false;
    }

    if (cantidad > stock) {
      document.getElementById('modalStockError').style.display = 'flex';
      return false;
    }
    return true;
  }

  function cerrarModalStock() {
    document.getElementById('modalStockError').style.display = 'none';
  }
</script>

</body>
</html>
