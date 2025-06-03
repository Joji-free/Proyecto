<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login - Mimir Petshop</title>
  <link rel="stylesheet" href="css/login.css" />
</head>
<body>
<div class="login-container">
  <h2>Iniciar Sesión</h2>

  <%
    String error = (String) request.getAttribute("error");
    if (error != null) {
  %>
  <div class="error"><%= error %></div>
  <% } %>

  <form action="login" method="post">
    <label for="usuario">Usuario</label>
    <%
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
