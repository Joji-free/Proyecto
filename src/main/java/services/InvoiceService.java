package services;

import model.DetalleFactura;
import model.Factura;
import model.Producto;
import repository.IProductRepository;
import repository.ProductRepository;
import util.Conexion;

import java.math.BigDecimal;
import java.sql.*;

public class InvoiceService {

    private final IProductRepository productRepo = new ProductRepository();

    /**
     * Inserta una nueva factura y devuelve el ID generado.
     */
    public int insertarFactura(Factura factura) {
        String sql = "INSERT INTO facturas (cliente_nombre, cliente_cedula, total) VALUES (?, ?, ?)";
        ResultSet rs = null;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, factura.getClienteNombre());
            ps.setString(2, factura.getClienteCedula());
            ps.setBigDecimal(3, factura.getTotal());
            int filas = ps.executeUpdate();
            if (filas == 0) return -1;

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return -1;
    }

    /**
     * Inserta un detalle en la tabla detalle_factura.
     */
    public boolean insertarDetalle(DetalleFactura detalle) {
        String sql = "INSERT INTO detalle_factura (factura_id, producto_id, cantidad, precio_unitario, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detalle.getFacturaId());
            ps.setInt(2, detalle.getProductoId());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecioUnitario());
            ps.setBigDecimal(5, detalle.getSubtotal());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Reduce el stock de un producto dado su ID.
     */
    public boolean reducirStock(int productoId, int cantidad) {
        return productRepo.reducirStock(productoId, cantidad);
    }
}
