package modelo;

import java.sql.Timestamp;

public class BancoDeposito {

    private long id_deposito;
    private Timestamp fecha;
    private String nro_voucher;
    private long id_cuenta;
    private double importe;
    private long id_usuario;
    private String terminal;
    private int estado;

    public BancoDeposito() {
    }

    public BancoDeposito(long id_deposito, Timestamp fecha, String nro_voucher, long id_cuenta, double importe, long id_usuario, String terminal, int estado) {
        this.id_deposito = id_deposito;
        this.fecha = fecha;
        this.nro_voucher = nro_voucher;
        this.id_cuenta = id_cuenta;
        this.importe = importe;
        this.id_usuario = id_usuario;
        this.terminal = terminal;
        this.estado = estado;
    }

    public long getId_deposito() {
        return id_deposito;
    }

    public void setId_deposito(long id_deposito) {
        this.id_deposito = id_deposito;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getNro_voucher() {
        return nro_voucher;
    }

    public void setNro_voucher(String nro_voucher) {
        this.nro_voucher = nro_voucher;
    }

    public long getId_cuenta() {
        return id_cuenta;
    }

    public void setId_cuenta(long id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
