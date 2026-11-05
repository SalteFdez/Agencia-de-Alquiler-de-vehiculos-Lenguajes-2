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
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ServicioClientesTest {

    private static Cliente cliente(String dni) {
        LicenciaConducir licencia = new LicenciaConducir("L123456",
                Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3));
        return new Cliente("Ana", "Gomez", dni, LocalDate.of(1990, 5, 20), "3874123456", licencia);
    }

    private static Cliente cliente(String nombre, String apellido, String dni) {
        LicenciaConducir licencia = new LicenciaConducir("L123456",
                Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3));
        return new Cliente(nombre, apellido, dni, LocalDate.of(1990, 5, 20), "3874123456", licencia);
    }

    private static Auto auto(String patente) {
        return new Auto(patente, "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
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

    @Test
    void unClienteSinCategoriasLanzaExcepcionYNoSeRegistra() {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        LicenciaConducir sinCategorias = new LicenciaConducir("L123456",
                Set.of(), LocalDate.now().plusYears(3));
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.of(1990, 5, 20), "3874123456", sinCategorias);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarCliente(cliente));

        assertEquals("La licencia debe tener al menos una categoría", e.getMessage());
        assertTrue(servicio.listarClientes().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"30123456", " 30123456 "})
    void unDniYaRegistradoLanzaExcepcionYLaListaNoCambia(String dniRepetido) throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente original = cliente("30123456");
        servicio.registrarCliente(original);
        Cliente repetido = cliente(dniRepetido);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarCliente(repetido));

        assertEquals("El DNI 30123456 ya está registrado", e.getMessage());
        assertEquals(List.of(original), servicio.listarClientes());
    }

    @Test
    void unClienteMenorDeDieciochoAniosSeRegistraIgual() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        LicenciaConducir licencia = new LicenciaConducir("L123456",
                Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3));
        Cliente menor = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.now().minusYears(17), "3874123456", licencia);

        servicio.registrarCliente(menor);

        assertEquals(List.of(menor), servicio.listarClientes());
    }

    @Test
    void unClienteConLaLicenciaVencidaSeRegistraIgual() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        LicenciaConducir vencida = new LicenciaConducir("L123456",
                Set.of(CategoriaLicencia.B), LocalDate.now().minusDays(1));
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.of(1990, 5, 20), "3874123456", vencida);

        servicio.registrarCliente(cliente);

        assertEquals(List.of(cliente), servicio.listarClientes());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void buscarConTextoNuloVacioOEnBlancoDevuelveTodosEnOrdenDeRegistro(String texto)
            throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente primero = cliente("30123456");
        Cliente segundo = cliente("27987654");
        servicio.registrarCliente(primero);
        servicio.registrarCliente(segundo);

        assertEquals(List.of(primero, segundo), servicio.buscarPorTexto(texto));
    }

    @Test
    void buscarPorParteDelNombreDevuelveSoloLosQueCoinciden() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente ana = cliente("Ana", "Gomez", "30123456");
        Cliente luis = cliente("Luis", "Perez", "27987654");
        servicio.registrarCliente(ana);
        servicio.registrarCliente(luis);

        assertEquals(List.of(luis), servicio.buscarPorTexto("lu"));
    }

    @Test
    void buscarPorParteDelApellidoDevuelveSoloLosQueCoinciden() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente ana = cliente("Ana", "Gomez", "30123456");
        Cliente luis = cliente("Luis", "Perez", "27987654");
        servicio.registrarCliente(ana);
        servicio.registrarCliente(luis);

        assertEquals(List.of(luis), servicio.buscarPorTexto("ere"));
    }

    @Test
    void buscarPorParteDelDniDevuelveSoloLosQueCoinciden() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente primero = cliente("30123456");
        Cliente segundo = cliente("27987654");
        servicio.registrarCliente(primero);
        servicio.registrarCliente(segundo);

        assertEquals(List.of(segundo), servicio.buscarPorTexto("9876"));
    }

    @Test
    void buscarPorParteDelNombreCompletoEncuentraAlCliente() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente gomez = cliente("Ana", "Gomez", "30123456");
        Cliente lopez = cliente("Ana", "Lopez", "27987654");
        servicio.registrarCliente(gomez);
        servicio.registrarCliente(lopez);

        assertEquals(List.of(gomez), servicio.buscarPorTexto("ana g"));
    }

    @Test
    void laBusquedaNoDistingueMayusculasDeMinusculas() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente ana = cliente("Ana", "Gomez", "30123456");
        Cliente luis = cliente("Luis", "Perez", "27987654");
        servicio.registrarCliente(ana);
        servicio.registrarCliente(luis);

        assertEquals(List.of(ana), servicio.buscarPorTexto("ANA"));
        assertEquals(List.of(luis), servicio.buscarPorTexto("pErEz"));
    }

    @Test
    void laBusquedaIgnoraLosEspaciosAlPrincipioYAlFinalDelTexto() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente ana = cliente("Ana", "Gomez", "30123456");
        Cliente luis = cliente("Luis", "Perez", "27987654");
        servicio.registrarCliente(ana);
        servicio.registrarCliente(luis);

        assertEquals(List.of(ana), servicio.buscarPorTexto("  ana "));
    }

    @Test
    void siNingunClienteCoincideLaListaQuedaVacia() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(cliente("Ana", "Gomez", "30123456"));
        servicio.registrarCliente(cliente("Luis", "Perez", "27987654"));

        assertTrue(servicio.buscarPorTexto("zzz").isEmpty());
    }

    @Test
    void eliminarUnClienteLoSacaDeLaListaYLosDemasSiguenEnOrden() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente primero = cliente("Ana", "Gomez", "30123456");
        Cliente segundo = cliente("Luis", "Perez", "27987654");
        Cliente tercero = cliente("Eva", "Diaz", "25111222");
        servicio.registrarCliente(primero);
        servicio.registrarCliente(segundo);
        servicio.registrarCliente(tercero);

        servicio.eliminarCliente(segundo);

        assertEquals(List.of(primero, tercero), servicio.listarClientes());
    }

    @Test
    void unClienteRegistradoDespuesDeEliminarOtroNoReutilizaSuNumero() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente primero = cliente("Ana", "Gomez", "30123456");
        Cliente segundo = cliente("Luis", "Perez", "27987654");
        servicio.registrarCliente(primero);
        servicio.registrarCliente(segundo);
        servicio.eliminarCliente(segundo);

        Cliente nuevo = cliente("Eva", "Diaz", "25111222");
        servicio.registrarCliente(nuevo);

        assertEquals(3, nuevo.getId());
    }

    @Test
    void eliminarUnClienteConUnContratoActivoLanzaExcepcionYElClienteSigueEnLaLista()
            throws AlquilerException {
        Agencia agencia = new Agencia();
        ServicioClientes servicio = new ServicioClientes(agencia);
        Cliente ana = cliente("Ana", "Gomez", "30123456");
        servicio.registrarCliente(ana);
        new ServicioAlquiler(agencia).crearContrato(ana, auto("AB123CD"), new TarifaDiaria(),
                LocalDate.now().plusDays(3));

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.eliminarCliente(ana));

        assertEquals("No se puede eliminar un cliente con un contrato activo", e.getMessage());
        assertEquals(List.of(ana), servicio.listarClientes());
    }

    @Test
    void eliminarUnClienteSinContratosFuncionaAunqueOtroTengaUnContratoActivo()
            throws AlquilerException {
        Agencia agencia = new Agencia();
        ServicioClientes servicio = new ServicioClientes(agencia);
        Cliente ana = cliente("Ana", "Gomez", "30123456");
        Cliente luis = cliente("Luis", "Perez", "27987654");
        servicio.registrarCliente(ana);
        servicio.registrarCliente(luis);
        new ServicioAlquiler(agencia).crearContrato(ana, auto("AB123CD"), new TarifaDiaria(),
                LocalDate.now().plusDays(3));

        servicio.eliminarCliente(luis);

        assertEquals(List.of(ana), servicio.listarClientes());
    }
}
