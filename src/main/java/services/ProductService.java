package services;

import model.Producto;
import repository.IProductRepository;
import repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final IProductRepository productRepo = new ProductRepository();

    public List<Producto> listarProductos() {
        return productRepo.listarTodos();
    }

    public Producto obtenerPorId(int id) {
        return productRepo.obtenerPorId(id);
    }

    public boolean insertarProducto(Producto p) {
        return productRepo.insertar(p);
    }

    public boolean actualizarProducto(Producto p) {
        return productRepo.actualizar(p);
    }

    public boolean eliminarProducto(int id) {
        return productRepo.eliminar(id);
    }

    public boolean reducirStock(int productoId, int cantidad) {
        return productRepo.reducirStock(productoId, cantidad);
    }
}
