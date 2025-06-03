package repository;

import model.Producto;
import util.Conexion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Única clase que concentra el CRUD completo de 'productos',
 * ahora adaptada para soft‐delete (campo 'activo') y reactivación.
 */
public class ProductRepository implements IProductRepository {

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        // Solo productos activos (activo = 1)
        String sql = "SELECT id, nombre, marca, precio, stock, activo FROM productos WHERE activo = 1";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setMarca(rs.getString("marca"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getInt("stock"));
                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Producto obtenerPorId(int id) {
        // Solo devolver si está activo
        String sql = "SELECT id, nombre, marca, precio, stock, activo FROM productos WHERE id = ? AND activo = 1";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setMarca(rs.getString("marca"));
                    p.setPrecio(rs.getBigDecimal("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setActivo(rs.getBoolean("activo"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertar(Producto producto) {
        // Se omite campo 'activo' porque en BD tiene DEFAULT 1
        String sql = "INSERT INTO productos (nombre, marca, precio, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getMarca());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(Producto producto) {
        // No se toca 'activo'; solo los campos editables
        String sql = "UPDATE productos SET nombre = ?, marca = ?, precio = ?, stock = ? WHERE id = ? AND activo = 1";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getMarca());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getId());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // Soft‐delete: marcar 'activo' = 0 en lugar de borrar
        String sql = "UPDATE productos SET activo = 0 WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean reducirStock(int productoId, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ? AND activo = 1";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, productoId);
            ps.setInt(3, cantidad);

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Reactiva (soft‐undelete) un producto marcándolo como activo = 1.
     */
    public boolean activar(int id) {
        String sql = "UPDATE productos SET activo = 1 WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Devuelve todos los productos inactivos (activo = 0).
     */
    public List<Producto> listarInactivos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, marca, precio, stock, activo FROM productos WHERE activo = 0";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setMarca(rs.getString("marca"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getInt("stock"));
                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
