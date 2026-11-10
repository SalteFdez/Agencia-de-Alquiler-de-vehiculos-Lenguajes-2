package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import modelo.alquiler.Contrato;
import modelo.clientes.Cliente;
import modelo.flota.Vehiculo;

public class Agencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Contrato> contratos = new ArrayList<>();
    private int ultimoIdCliente;
    private int ultimoNroContrato;

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Contrato> getContratos() {
        return contratos;
    }

    public int siguienteIdCliente() {
        return ++ultimoIdCliente;
    }

    public int siguienteNroContrato() {
        return ++ultimoNroContrato;
    }

    public void reemplazarCon(Agencia otra) {
        vehiculos.clear();
        vehiculos.addAll(otra.vehiculos);
        clientes.clear();
        clientes.addAll(otra.clientes);
        contratos.clear();
        contratos.addAll(otra.contratos);
        ultimoIdCliente = otra.ultimoIdCliente;
        ultimoNroContrato = otra.ultimoNroContrato;
    }
}
