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
