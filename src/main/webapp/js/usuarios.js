// usuarios.js

// 1) Mostrar / ocultar formulario de creación
function showCreateForm() {
    document.getElementById("formCrear").classList.remove("oculto");
    document.getElementById("btnShowCreate").disabled = true;
}

function hideCreateForm() {
    document.getElementById("formCrear").classList.add("oculto");
    document.getElementById("btnShowCreate").disabled = false;
}

// 2) Apertura y cierre de Modal de Edición
function abrirEditarModal(id, usuario, contrasena) {
    document.getElementById("editId").value = id;
    document.getElementById("editUsuario").value = usuario;
    document.getElementById("editContrasena").value = contrasena;
    document.getElementById("modalEditarUsuario").classList.remove("oculto");
}

function cerrarEditarModal() {
    document.getElementById("modalEditarUsuario").classList.add("oculto");
}

// 3) Apertura y cierre de Modal de Eliminación
function abrirEliminarModal(id) {
    document.getElementById("deleteId").value = id;
    document.getElementById("modalEliminarUsuario").classList.remove("oculto");
}

function cerrarEliminarModal() {
    document.getElementById("modalEliminarUsuario").classList.add("oculto");
}
