package modelo;

import java.util.ArrayList;
import java.util.List;
import modelo.flota.Vehiculo;

public class Agencia {

    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private int ultimoIdCliente;

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public int siguienteIdCliente() {
        return ++ultimoIdCliente;
    }
}
