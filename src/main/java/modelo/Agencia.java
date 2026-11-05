package modelo;

import java.util.ArrayList;
import java.util.List;
import modelo.clientes.Cliente;
import modelo.flota.Vehiculo;

public class Agencia {

    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private int ultimoIdCliente;

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public int siguienteIdCliente() {
        return ++ultimoIdCliente;
    }
}
