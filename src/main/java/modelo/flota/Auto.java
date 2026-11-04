package modelo.flota;

import excepciones.AlquilerException;

public class Auto extends Vehiculo {

    private final int cantidadPuertas;
    private final int capacidadPasajeros;
    private final TipoCaja tipoCaja;

    public Auto(String patente, String marca, String modelo, String color, int anio,
                int kilometraje, double precioBaseDiario, int cantidadPuertas, int capacidadPasajeros,
                TipoCaja tipoCaja) {
        super(patente, marca, modelo, color, anio, kilometraje, precioBaseDiario);
        this.cantidadPuertas = cantidadPuertas;
        this.capacidadPasajeros = capacidadPasajeros;
        this.tipoCaja = tipoCaja;
    }

    @Override
    public String getTipo() {
        return "Auto";
    }

    @Override
    public void validarDatosPropios() throws AlquilerException {
        if (cantidadPuertas <= 0) {
            throw new AlquilerException("La cantidad de puertas debe ser mayor a 0");
        }
        if (capacidadPasajeros <= 0) {
            throw new AlquilerException("La capacidad de pasajeros debe ser mayor a 0");
        }
    }

    @Override
    protected double getFactorTarifa() {
        return tipoCaja == TipoCaja.AUTOMATICA ? 1.15 : 1.0;
    }
}
