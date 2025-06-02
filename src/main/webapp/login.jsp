<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login - Mimir Petshop</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f5f5f5;
    }
    .login-container {
      margin: 100px auto;
      width: 300px;
      padding: 20px;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 4px;
    }
    input[type=text], input[type=password] {
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
    .error {
      color: red;
      margin-bottom: 15px;
      text-align: center;
    }
  </style>
</head>
<body>
<div class="login-container">
  <h2>Iniciar Sesión</h2>

  <%
    // 1) Obtener mensaje de error que haya puesto el servlet
    String error = (String) request.getAttribute("error");
    if (error != null) {
  %>
  <div class="error"><%= error %></div>
  <% } %>

  <form action="login" method="post">
    <label for="usuario">Usuario</label>
    <%
      // 2) Precargar el campo "usuario" si el servlet lo envió
      String usuarioPrevio = (String) request.getAttribute("usuario");
      if (usuarioPrevio == null) {
        usuarioPrevio = "";
      }
    %>
    <input
            type="text"
            name="usuario"
            id="usuario"
            value="<%= usuarioPrevio %>" />

    <label for="contrasena">Contraseña</label>
    <input type="password" name="contrasena" id="contrasena" />

    <button type="submit">Entrar</button>
  </form>
</div>
</body>
</html>
