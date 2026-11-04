package modelo.flota;

public class Moto extends Vehiculo {

    private final int cilindrada;
    private final boolean incluyeCasco;

    public Moto(String patente, String marca, String modelo, String color, int anio,
                int kilometraje, double precioBaseDiario, int cilindrada, boolean incluyeCasco) {
        super(patente, marca, modelo, color, anio, kilometraje, precioBaseDiario);
        this.cilindrada = cilindrada;
        this.incluyeCasco = incluyeCasco;
    }

    @Override
    public String getTipo() {
        return "Moto";
    }

    @Override
    protected double getFactorTarifa() {
        return 0.6;
    }
}
