package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import excepciones.AlquilerException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import modelo.Agencia;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import org.junit.jupiter.api.Test;

class ServicioClientesTest {

    private static Cliente cliente(String dni) {
        LicenciaConducir licencia = new LicenciaConducir("L123456",
                Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3));
        return new Cliente("Ana", "Gomez", dni, LocalDate.of(1990, 5, 20), "3874123456", licencia);
    }

    @Test
    void registrarUnClienteValidoLoAgregaALaListaConElNumeroUno() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente cliente = cliente("30123456");

        servicio.registrarCliente(cliente);

        assertEquals(List.of(cliente), servicio.listarClientes());
        assertEquals(1, cliente.getId());
    }
}
