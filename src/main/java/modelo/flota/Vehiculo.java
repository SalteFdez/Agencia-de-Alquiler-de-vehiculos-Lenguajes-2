package modelo.flota;

public abstract class Vehiculo {

    private final String patente;
    private final String marca;
    private final String modelo;
    private final String color;
    private final int anio;
    private final int kilometraje;
    private final double precioBase;
    private EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;

    protected Vehiculo(String patente, String marca, String modelo, String color,
                       int anio, int kilometraje, double precioBase) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.anio = anio;
        this.kilometraje = kilometraje;
        this.precioBase = precioBase;
    }

    public String getPatente() {
        return patente;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    protected abstract double getFactorTarifa();

    public double getPrecioDiario() {
        return precioBase * getFactorTarifa();
    }
}
