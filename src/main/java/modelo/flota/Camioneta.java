package modelo.flota;

import excepciones.AlquilerException;
import modelo.clientes.CategoriaLicencia;

public class Camioneta extends Vehiculo {

    private final double capacidadCargaKg;
    private final boolean traccion4x4;

    public Camioneta(String patente, String marca, String modelo, String color, int anio,
                     int kilometraje, double precioBaseDiario, double capacidadCargaKg,
                     boolean traccion4x4) {
        super(patente, marca, modelo, color, anio, kilometraje, precioBaseDiario);
        this.capacidadCargaKg = capacidadCargaKg;
        this.traccion4x4 = traccion4x4;
    }

    @Override
    public String getTipo() {
        return "Camioneta";
    }

    @Override
    public void validarDatosPropios() throws AlquilerException {
        if (capacidadCargaKg <= 0) {
            throw new AlquilerException("La capacidad de carga debe ser mayor a 0");
        }
    }

    @Override
    public CategoriaLicencia getLicenciaRequerida() {
        return CategoriaLicencia.B;
    }

    @Override
    protected double getFactorTarifa() {
        return traccion4x4 ? 1.6 : 1.4;
    }
}
