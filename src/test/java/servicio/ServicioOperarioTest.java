package servicio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import excepciones.AlquilerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;
import modelo.Agencia;
import modelo.alquiler.Contrato;
import modelo.alquiler.EstadoContrato;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import modelo.flota.Auto;
import modelo.flota.EstadoVehiculo;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import persistencia.GestorPersistencia;

class ServicioOperarioTest {

    @TempDir
    Path carpeta;

    private GestorPersistencia gestor() {
        return new GestorPersistencia(new File(carpeta.toFile(), "agencia.dat"));
    }

    private static Cliente ana() {
        return new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.now().plusYears(3)));
    }

    private static Auto cronos() {
        return new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000, 10000.0, 4, 5,
                TipoCaja.MANUAL);
    }

    private GestorPersistencia archivoCon(Cliente cliente, Auto auto) throws AlquilerException {
        Agencia agencia = new Agencia();
        new ServicioClientes(agencia).registrarCliente(cliente);
        new ServicioFlota(agencia).registrarVehiculo(auto);
        GestorPersistencia gestor = gestor();
        gestor.guardar(agencia);
        return gestor;
    }

    @Test
    void recargarTraeALaAgenciaVivaElContratoQueGuardoElKiosco() throws AlquilerException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));

        operario.recargar();

        assertEquals(1, viva.getContratos().size());
        assertEquals(1, viva.getContratos().get(0).getNumero());
        assertEquals(EstadoVehiculo.ALQUILADO, viva.getVehiculos().get(0).getEstado());
    }

    @Test
    void recargarConArchivoAusenteNoCambiaNadaYConArchivoDaniadoLanzaElErrorYLaVistaQuedaIgual()
            throws AlquilerException, IOException {
        Agencia viva = new Agencia();
        viva.getVehiculos().add(cronos());
        GestorPersistencia gestor = gestor();
        ServicioOperario operario = new ServicioOperario(gestor, viva);

        operario.recargar();

        assertEquals(1, viva.getVehiculos().size());

        Files.write(carpeta.resolve("agencia.dat"), new byte[] {1, 2, 3});
        AlquilerException e = assertThrows(AlquilerException.class, operario::recargar);

        assertEquals("No se pudieron cargar los datos", e.getMessage());
        assertEquals(1, viva.getVehiculos().size());
    }

    @Test
    void registrarVehiculoQuedaEnElArchivoAlInstanteYEnLaAgenciaViva() throws AlquilerException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        Auto ka = new Auto("ZZ999ZZ", "Ford", "Ka", "Negro", 2019, 40000, 9000.0, 4, 5, TipoCaja.MANUAL);

        operario.registrarVehiculo(ka);

        Agencia enArchivo = gestor.cargar();
        assertEquals(2, enArchivo.getVehiculos().size());
        assertEquals("ZZ999ZZ", enArchivo.getVehiculos().get(1).getPatente());
        assertEquals(2, viva.getVehiculos().size());
        assertEquals("ZZ999ZZ", viva.getVehiculos().get(1).getPatente());
    }

    @Test
    void registrarClienteQuedaEnElArchivoAlInstanteYElKioscoLoReconocePorSuDni() throws AlquilerException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        ServicioOperario operario = new ServicioOperario(gestor, gestor.cargar());
        Cliente luis = new Cliente("Luis", "Perez", "28999111", LocalDate.of(1985, 3, 10), "3874999999",
                new LicenciaConducir("L777", Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3)));

        operario.registrarCliente(luis);

        assertEquals(2, gestor.cargar().getClientes().size());
        assertEquals(1, new ServicioKiosco(gestor)
                .alquilar("28999111", "AB123CD", LocalDate.now().plusDays(3)).getNumero());
    }

    @Test
    void unCambioDelOperarioConservaElContratoQueElKioscoGuardoDespuesDeAbrirseElOperario()
            throws AlquilerException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));

        operario.registrarVehiculo(new Auto("ZZ999ZZ", "Ford", "Ka", "Negro", 2019, 40000, 9000.0, 4, 5,
                TipoCaja.MANUAL));

        Agencia enArchivo = gestor.cargar();
        assertEquals(1, enArchivo.getContratos().size());
        assertEquals(EstadoVehiculo.ALQUILADO, enArchivo.getVehiculos().get(0).getEstado());
        assertEquals(2, enArchivo.getVehiculos().size());
        assertEquals(1, viva.getContratos().size());
        assertEquals(2, viva.getVehiculos().size());
    }

    private static Cliente luis() {
        return new Cliente("Luis", "Perez", "28999111", LocalDate.of(1985, 3, 10), "3874999999",
                new LicenciaConducir("L777", Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3)));
    }

    private static Auto ka() {
        return new Auto("ZZ999ZZ", "Ford", "Ka", "Negro", 2019, 40000, 9000.0, 4, 5, TipoCaja.MANUAL);
    }

    @Test
    void darDeBajaUnVehiculoYEliminarUnClienteSinContratosConservanElContratoDelKiosco()
            throws AlquilerException {
        Agencia inicial = new Agencia();
        new ServicioClientes(inicial).registrarCliente(ana());
        new ServicioClientes(inicial).registrarCliente(luis());
        new ServicioFlota(inicial).registrarVehiculo(cronos());
        new ServicioFlota(inicial).registrarVehiculo(ka());
        GestorPersistencia gestor = gestor();
        gestor.guardar(inicial);
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));

        operario.eliminarVehiculo("ZZ999ZZ");
        operario.eliminarCliente("28999111");

        Agencia enArchivo = gestor.cargar();
        assertEquals(1, enArchivo.getContratos().size());
        assertEquals(1, enArchivo.getVehiculos().size());
        assertEquals("AB123CD", enArchivo.getVehiculos().get(0).getPatente());
        assertEquals(1, enArchivo.getClientes().size());
        assertEquals("30123456", enArchivo.getClientes().get(0).getDni());
        assertEquals(1, viva.getContratos().size());
        assertEquals(1, viva.getVehiculos().size());
        assertEquals(1, viva.getClientes().size());
    }

    @Test
    void eliminarUnVehiculoOUnClienteQueYaNoEstanEnElArchivoNoFallaYNoCambiaNada()
            throws AlquilerException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);

        operario.eliminarVehiculo("ZZ999ZZ");
        operario.eliminarCliente("11222333");

        assertEquals(1, gestor.cargar().getVehiculos().size());
        assertEquals(1, gestor.cargar().getClientes().size());
        assertEquals(1, viva.getVehiculos().size());
        assertEquals(1, viva.getClientes().size());
    }

    @Test
    void laNumeracionContinuaElContratoQueCreaElOperarioDespuesDelDelKioscoEsElDos()
            throws AlquilerException {
        Agencia inicial = new Agencia();
        new ServicioClientes(inicial).registrarCliente(ana());
        new ServicioClientes(inicial).registrarCliente(luis());
        new ServicioFlota(inicial).registrarVehiculo(cronos());
        new ServicioFlota(inicial).registrarVehiculo(ka());
        GestorPersistencia gestor = gestor();
        gestor.guardar(inicial);
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));

        Contrato delOperario = operario.crearContrato("28999111", "ZZ999ZZ", LocalDate.now().plusDays(2));

        assertEquals(2, delOperario.getNumero());
        Agencia enArchivo = gestor.cargar();
        assertEquals(2, enArchivo.getContratos().size());
        assertEquals(1, enArchivo.getContratos().get(0).getNumero());
        assertEquals(2, enArchivo.getContratos().get(1).getNumero());
        assertEquals(2, viva.getContratos().size());
    }

    private GestorPersistencia archivoConElKioscoYaAlquiladoAna() throws AlquilerException {
        Agencia inicial = new Agencia();
        new ServicioClientes(inicial).registrarCliente(ana());
        new ServicioClientes(inicial).registrarCliente(luis());
        new ServicioFlota(inicial).registrarVehiculo(cronos());
        new ServicioFlota(inicial).registrarVehiculo(ka());
        GestorPersistencia gestor = gestor();
        gestor.guardar(inicial);
        return gestor;
    }

    @Test
    void eliminarUnClienteConUnContratoActivoDelKioscoLanzaElMotivoYNoCambiaNada()
            throws AlquilerException, IOException {
        GestorPersistencia gestor = archivoConElKioscoYaAlquiladoAna();
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException e = assertThrows(AlquilerException.class, () -> operario.eliminarCliente("30123456"));

        assertEquals("No se puede eliminar un cliente con un contrato activo", e.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
        assertEquals(2, viva.getClientes().size());
    }

    @Test
    void darDeBajaUnVehiculoQueElKioscoAlquiloLanzaElMotivoYNoCambiaNada()
            throws AlquilerException, IOException {
        GestorPersistencia gestor = archivoConElKioscoYaAlquiladoAna();
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException e = assertThrows(AlquilerException.class, () -> operario.eliminarVehiculo("AB123CD"));

        assertEquals("No se puede dar de baja un vehículo alquilado", e.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
        assertEquals(2, viva.getVehiculos().size());
    }

    @Test
    void crearUnContratoDeUnVehiculoQueElKioscoYaAlquiloLanzaNoDisponibleYElArchivoQuedaIgual()
            throws AlquilerException, IOException {
        GestorPersistencia gestor = archivoConElKioscoYaAlquiladoAna();
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> operario.crearContrato("28999111", "AB123CD", LocalDate.now().plusDays(2)));

        assertEquals("El vehículo AB123CD no está disponible", e.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
        assertEquals(0, viva.getContratos().size());
    }

    @Test
    void crearUnContratoConUnDniOUnaPatenteQueYaNoEstanEnElArchivoLanzaElMotivoYNoGuardaNada()
            throws AlquilerException, IOException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        ServicioOperario operario = new ServicioOperario(gestor, gestor.cargar());
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException sinCliente = assertThrows(AlquilerException.class,
                () -> operario.crearContrato("11222333", "AB123CD", LocalDate.now().plusDays(2)));
        AlquilerException sinVehiculo = assertThrows(AlquilerException.class,
                () -> operario.crearContrato("30123456", "ZZ999ZZ", LocalDate.now().plusDays(2)));

        assertEquals("No hay ningún cliente registrado con el DNI 11222333", sinCliente.getMessage());
        assertEquals("El vehículo ZZ999ZZ no está disponible", sinVehiculo.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
    }

    @Test
    void registrarLaDevolucionDeUnContratoDelKioscoPorNumeroLoFinalizaYDevuelveElContratoActualizado()
            throws AlquilerException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        Agencia viva = gestor.cargar();
        ServicioOperario operario = new ServicioOperario(gestor, viva);
        new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));

        Contrato devuelto = operario.registrarDevolucion(1, LocalDate.now(), 15500);

        assertEquals(EstadoContrato.FINALIZADO, devuelto.getEstado());
        assertEquals(3, devuelto.getDias());
        assertEquals(500, devuelto.getKmRecorridos());
        assertEquals(30000.0, devuelto.calcularTotal(), 0.001);
        Agencia enArchivo = gestor.cargar();
        assertEquals(EstadoContrato.FINALIZADO, enArchivo.getContratos().get(0).getEstado());
        assertEquals(EstadoVehiculo.DISPONIBLE, enArchivo.getVehiculos().get(0).getEstado());
        assertEquals(15500, enArchivo.getVehiculos().get(0).getKilometraje());
        assertEquals(EstadoContrato.FINALIZADO, viva.getContratos().get(0).getEstado());
    }

    @Test
    void siNoSePuedeGuardarLanzaElErrorYLaAgenciaVivaNoCambia() {
        GestorPersistencia sinCarpeta = new GestorPersistencia(
                new File(new File(carpeta.toFile(), "carpeta-inexistente"), "agencia.dat"));
        Agencia viva = new Agencia();
        ServicioOperario operario = new ServicioOperario(sinCarpeta, viva);

        AlquilerException e = assertThrows(AlquilerException.class, () -> operario.registrarVehiculo(cronos()));

        assertEquals("No se pudieron guardar los datos", e.getMessage());
        assertTrue(viva.getVehiculos().isEmpty());
    }

    @Test
    void conElArchivoAusenteElPrimerCambioLoCrea() throws AlquilerException {
        GestorPersistencia gestor = gestor();
        ServicioOperario operario = new ServicioOperario(gestor, new Agencia());

        operario.registrarVehiculo(cronos());

        assertEquals(1, gestor.cargar().getVehiculos().size());
    }

    @Test
    void conElArchivoDaniadoElCambioLanzaNoSePudieronCargarYNoLoPisa() throws IOException {
        Files.write(carpeta.resolve("agencia.dat"), new byte[] {1, 2, 3});
        Agencia viva = new Agencia();
        ServicioOperario operario = new ServicioOperario(gestor(), viva);

        AlquilerException e = assertThrows(AlquilerException.class, () -> operario.registrarVehiculo(cronos()));

        assertEquals("No se pudieron cargar los datos", e.getMessage());
        assertArrayEquals(new byte[] {1, 2, 3}, Files.readAllBytes(carpeta.resolve("agencia.dat")));
        assertTrue(viva.getVehiculos().isEmpty());
    }
}
