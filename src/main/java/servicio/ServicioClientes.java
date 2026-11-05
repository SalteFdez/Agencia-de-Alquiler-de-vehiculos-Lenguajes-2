package servicio;

import excepciones.AlquilerException;
import java.util.ArrayList;
import java.util.List;
import modelo.Agencia;
import modelo.clientes.Cliente;

public class ServicioClientes {

    private final Agencia agencia;

    public ServicioClientes(Agencia agencia) {
        this.agencia = agencia;
    }

    public void registrarCliente(Cliente c) throws AlquilerException {
        c.setId(agencia.siguienteIdCliente());
        agencia.getClientes().add(c);
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(agencia.getClientes());
    }
}
