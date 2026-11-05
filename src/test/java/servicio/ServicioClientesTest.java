package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import excepciones.AlquilerException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import modelo.Agencia;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ServicioClientesTest {

    private static Cliente cliente(String dni) {
        LicenciaConducir licencia = new LicenciaConducir("L123456",
                Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3));
        return new Cliente("Ana", "Gomez", dni, LocalDate.of(1990, 5, 20), "3874123456", licencia);
    }

    private static Cliente clienteConCampo(String campo, String valor) {
        String nombre = campo.equals("Nombre") ? valor : "Ana";
        String apellido = campo.equals("Apellido") ? valor : "Gomez";
        String dni = campo.equals("DNI") ? valor : "30123456";
        String telefono = campo.equals("Teléfono") ? valor : "3874123456";
        String numeroLicencia = campo.equals("Número de licencia") ? valor : "L123456";
        LicenciaConducir licencia = new LicenciaConducir(numeroLicencia,
                Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3));
        return new Cliente(nombre, apellido, dni, LocalDate.of(1990, 5, 20), telefono, licencia);
    }

    @Test
    void registrarUnClienteValidoLoAgregaALaListaConElNumeroUno() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente cliente = cliente("30123456");

        servicio.registrarCliente(cliente);

        assertEquals(List.of(cliente), servicio.listarClientes());
        assertEquals(1, cliente.getId());
    }

    @Test
    void elSegundoClienteRegistradoRecibeElNumeroDos() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente primero = cliente("30123456");
        Cliente segundo = cliente("27987654");

        servicio.registrarCliente(primero);
        servicio.registrarCliente(segundo);

        assertEquals(List.of(primero, segundo), servicio.listarClientes());
        assertEquals(1, primero.getId());
        assertEquals(2, segundo.getId());
    }

    @ParameterizedTest
    @CsvSource({
        "Nombre, ''", "Nombre, '   '",
        "Apellido, ''", "Apellido, '   '",
        "DNI, ''", "DNI, '   '",
        "Teléfono, ''", "Teléfono, '   '",
        "Número de licencia, ''", "Número de licencia, '   '"
    })
    void unDatoObligatorioVacioOEnBlancoLanzaExcepcionYNoRegistra(String campo, String valor) {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente incompleto = clienteConCampo(campo, valor);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarCliente(incompleto));

        assertEquals("El campo " + campo + " es obligatorio", e.getMessage());
        assertTrue(servicio.listarClientes().isEmpty());
    }
}
