package modelo.flota;

import excepciones.AlquilerException;

public abstract class Vehiculo {

    private final String patente;
    private final String marca;
    private final String modelo;
    private final String color;
    private final int anio;
    private final int kilometraje;
    private final double precioBaseDiario;
    private EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;

    protected Vehiculo(String patente, String marca, String modelo, String color,
                       int anio, int kilometraje, double precioBaseDiario) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.anio = anio;
        this.kilometraje = kilometraje;
        this.precioBaseDiario = precioBaseDiario;
    }

    public String getPatente() {
        return patente;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public double getPrecioBaseDiario() {
        return precioBaseDiario;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public abstract String getTipo();

    public void validarDatosPropios() throws AlquilerException {
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return patente + " - " + marca + " " + modelo;
    }

    public boolean estaDisponible() {
        return estado == EstadoVehiculo.DISPONIBLE;
    }

    public void cambiarEstado(EstadoVehiculo e) {
        this.estado = e;
    }

    protected abstract double getFactorTarifa();

    public double getPrecioDiario() {
        return precioBaseDiario * getFactorTarifa();
    }
}
