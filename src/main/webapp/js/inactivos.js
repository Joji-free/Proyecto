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
