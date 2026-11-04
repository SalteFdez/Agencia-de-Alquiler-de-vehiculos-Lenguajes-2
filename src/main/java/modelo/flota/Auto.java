package modelo.flota;

public class Auto extends Vehiculo {

    private final int puertas;
    private final int capacidadPasajeros;
    private final TipoCaja tipoCaja;

    public Auto(String patente, String marca, String modelo, String color, int anio,
                int kilometraje, double precioBase, int puertas, int capacidadPasajeros,
                TipoCaja tipoCaja) {
        super(patente, marca, modelo, color, anio, kilometraje, precioBase);
        this.puertas = puertas;
        this.capacidadPasajeros = capacidadPasajeros;
        this.tipoCaja = tipoCaja;
    }

    @Override
    public String getTipo() {
        return "Auto";
    }

    @Override
    protected double getFactorTarifa() {
        return tipoCaja == TipoCaja.AUTOMATICA ? 1.15 : 1.0;
    }
}
