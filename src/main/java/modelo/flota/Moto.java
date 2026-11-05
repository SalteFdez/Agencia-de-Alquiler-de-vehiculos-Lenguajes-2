package modelo.flota;

import excepciones.AlquilerException;
import modelo.clientes.CategoriaLicencia;

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
    public void validarDatosPropios() throws AlquilerException {
        if (cilindrada <= 0) {
            throw new AlquilerException("La cilindrada debe ser mayor a 0");
        }
    }

    @Override
    public CategoriaLicencia getLicenciaRequerida() {
        return CategoriaLicencia.A;
    }

    @Override
    protected double getFactorTarifa() {
        return 0.6;
    }
}
