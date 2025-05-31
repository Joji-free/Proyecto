package model;

import java.math.BigDecimal;
import java.util.Date;

public class Factura {
    private int id;
    private Date fecha;
    private String clienteNombre;
    private String clienteCedula;
    private BigDecimal total;

    public Factura() { }

    public Factura(int id, Date fecha, String clienteNombre, String clienteCedula, BigDecimal total) {
        this.id = id;
        this.fecha = fecha;
        this.clienteNombre = clienteNombre;
        this.clienteCedula = clienteCedula;
        this.total = total;
    }

    // Getters y setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteCedula() {
        return clienteCedula;
    }
    public void setClienteCedula(String clienteCedula) {
        this.clienteCedula = clienteCedula;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
