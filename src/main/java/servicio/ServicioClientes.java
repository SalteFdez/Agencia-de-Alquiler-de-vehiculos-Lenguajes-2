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
        exigir(c.getNombre(), "Nombre");
        exigir(c.getApellido(), "Apellido");
        exigir(c.getDni(), "DNI");
        exigir(c.getTelefono(), "Teléfono");
        exigir(c.getLicencia().getNumero(), "Número de licencia");
        if (c.getLicencia().getCategorias().isEmpty()) {
            throw new AlquilerException("La licencia debe tener al menos una categoría");
        }
        String dni = c.getDni().trim();
        for (Cliente existente : agencia.getClientes()) {
            if (existente.getDni().trim().equals(dni)) {
                throw new AlquilerException("El DNI " + dni + " ya está registrado");
            }
        }
        c.setId(agencia.siguienteIdCliente());
        agencia.getClientes().add(c);
    }

    private void exigir(String valor, String campo) throws AlquilerException {
        if (valor.trim().isEmpty()) {
            throw new AlquilerException("El campo " + campo + " es obligatorio");
        }
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(agencia.getClientes());
    }
}
