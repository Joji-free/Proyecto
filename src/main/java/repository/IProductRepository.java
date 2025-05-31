package repository;

import java.util.List;
import model.Producto;

/**
 * Contrato para todas las operaciones CRUD de la tabla 'productos'.
 */
public interface IProductRepository {
    List<Producto> listarTodos();
    Producto obtenerPorId(int id);
    boolean insertar(Producto producto);
    boolean actualizar(Producto producto);
    boolean eliminar(int id);
    boolean reducirStock(int productoId, int cantidad);
}
