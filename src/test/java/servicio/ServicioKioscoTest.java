package servicio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import excepciones.AlquilerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import modelo.Agencia;
import modelo.alquiler.Contrato;
import modelo.alquiler.EstadoContrato;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import modelo.flota.Auto;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Moto;
import modelo.flota.TipoCaja;
import modelo.flota.Vehiculo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import persistencia.GestorPersistencia;

class ServicioKioscoTest {

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
    void alquilarConUnClienteHabilitadoCreaUnContratoActivoYLoGuardaEnElArchivo()
            throws AlquilerException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        LocalDate fin = LocalDate.now().plusDays(3);

        Contrato contrato = new ServicioKiosco(gestor).alquilar("30123456", "AB123CD", fin);

        assertEquals(1, contrato.getNumero());
        Agencia guardada = gestor.cargar();
        assertEquals(1, guardada.getContratos().size());
        Contrato leido = guardada.getContratos().get(0);
        assertEquals(1, leido.getNumero());
        assertEquals(EstadoContrato.ACTIVO, leido.getEstado());
        assertEquals("30123456", leido.getCliente().getDni());
        assertEquals(fin, leido.getFechaFinPrevista());
        assertEquals(EstadoVehiculo.ALQUILADO, guardada.getVehiculos().get(0).getEstado());
    }

    @Test
    void laNumeracionContinuaDespuesDeLosContratosYaGuardados() throws AlquilerException {
        Agencia agencia = new Agencia();
        new ServicioClientes(agencia).registrarCliente(ana());
        ServicioFlota flota = new ServicioFlota(agencia);
        ServicioAlquiler alquiler = new ServicioAlquiler(agencia);
        String[] patentes = {"AA111AA", "BB222BB", "CC333CC", "DD444DD"};
        for (String patente : patentes) {
            flota.registrarVehiculo(new Auto(patente, "Fiat", "Cronos", "Blanco", 2022, 15000,
                    10000.0, 4, 5, TipoCaja.MANUAL));
        }
        for (int i = 0; i < 3; i++) {
            alquiler.crearContrato(agencia.getClientes().get(0), agencia.getVehiculos().get(i),
                    new modelo.tarifas.TarifaDiaria(), LocalDate.now().plusDays(2));
        }
        GestorPersistencia gestor = gestor();
        gestor.guardar(agencia);

        Contrato nuevo = new ServicioKiosco(gestor)
                .alquilar("30123456", "DD444DD", LocalDate.now().plusDays(3));

        assertEquals(4, nuevo.getNumero());
        assertEquals(4, gestor.cargar().getContratos().size());
        assertEquals(4, gestor.cargar().getContratos().get(3).getNumero());
    }

    @Test
    void conUnDniNoRegistradoLanzaElMotivoYElArchivoQuedaIgual() throws AlquilerException, IOException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException e = assertThrows(AlquilerException.class, () -> new ServicioKiosco(gestor)
                .alquilar("11222333", "AB123CD", LocalDate.now().plusDays(3)));

        assertEquals("No hay ningún cliente registrado con el DNI 11222333", e.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void conElDniVacioPideEscribirloYNoGuardaNada(String dni) throws AlquilerException, IOException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException e = assertThrows(AlquilerException.class, () -> new ServicioKiosco(gestor)
                .alquilar(dni, "AB123CD", LocalDate.now().plusDays(3)));

        assertEquals("Hay que escribir el DNI", e.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
    }

    @Test
    void conUnClienteNoHabilitadoLanzaElMotivoYElArchivoQuedaIgual() throws AlquilerException, IOException {
        Cliente menor = new Cliente("Beto", "Paz", "50111222", LocalDate.now().minusYears(17), "3874000000",
                new LicenciaConducir("L999", Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3)));
        GestorPersistencia gestor = archivoCon(menor, cronos());
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException e = assertThrows(AlquilerException.class, () -> new ServicioKiosco(gestor)
                .alquilar("50111222", "AB123CD", LocalDate.now().plusDays(3)));

        assertEquals("El cliente no está habilitado para conducir este vehículo: es menor de 18 años",
                e.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
    }

    @Test
    void siElOperarioYaAlquiloElVehiculoMientrasElKioscoEstabaAbiertoLanzaNoDisponibleYNoGuarda()
            throws AlquilerException, IOException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        ServicioKiosco kiosco = new ServicioKiosco(gestor);
        Agencia delOperario = gestor.cargar();
        Cliente otro = new Cliente("Luis", "Perez", "28999111", LocalDate.of(1985, 3, 10), "3874999999",
                new LicenciaConducir("L777", Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3)));
        new ServicioClientes(delOperario).registrarCliente(otro);
        new ServicioAlquiler(delOperario).crearContrato(otro, delOperario.getVehiculos().get(0),
                new modelo.tarifas.TarifaDiaria(), LocalDate.now().plusDays(5));
        gestor.guardar(delOperario);
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> kiosco.alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3)));

        assertEquals("El vehículo AB123CD no está disponible", e.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
    }

    @Test
    void siElOperarioDioDeBajaElVehiculoLanzaNoDisponibleYNoGuarda() throws AlquilerException, IOException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        Agencia delOperario = gestor.cargar();
        new ServicioFlota(delOperario).eliminarVehiculo(delOperario.getVehiculos().get(0));
        gestor.guardar(delOperario);
        byte[] antes = Files.readAllBytes(carpeta.resolve("agencia.dat"));

        AlquilerException e = assertThrows(AlquilerException.class, () -> new ServicioKiosco(gestor)
                .alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3)));

        assertEquals("El vehículo AB123CD no está disponible", e.getMessage());
        assertArrayEquals(antes, Files.readAllBytes(carpeta.resolve("agencia.dat")));
    }

    @Test
    void loQueOtroProcesoGuardoDespuesDeAbrirElKioscoNoSePierde() throws AlquilerException {
        GestorPersistencia gestor = archivoCon(ana(), cronos());
        ServicioKiosco kiosco = new ServicioKiosco(gestor);
        Agencia delOperario = gestor.cargar();
        Cliente luis = new Cliente("Luis", "Perez", "28999111", LocalDate.of(1985, 3, 10), "3874999999",
                new LicenciaConducir("L777", Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3)));
        new ServicioClientes(delOperario).registrarCliente(luis);
        new ServicioFlota(delOperario).registrarVehiculo(new Auto("ZZ999ZZ", "Ford", "Ka", "Negro", 2019,
                40000, 9000.0, 4, 5, TipoCaja.MANUAL));
        gestor.guardar(delOperario);

        kiosco.alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));

        Agencia guardada = gestor.cargar();
        assertEquals(2, guardada.getClientes().size());
        assertEquals("28999111", guardada.getClientes().get(1).getDni());
        assertEquals(2, guardada.getVehiculos().size());
        assertEquals(EstadoVehiculo.DISPONIBLE, guardada.getVehiculos().get(1).getEstado());
        assertEquals(1, guardada.getContratos().size());
    }

    @Test
    void siElArchivoFaltaOEstaDanadoLanzaNoSePudieronCargarLosDatosYNoCreaNiPisaNada()
            throws IOException {
        GestorPersistencia gestor = gestor();
        ServicioKiosco kiosco = new ServicioKiosco(gestor);

        AlquilerException faltante = assertThrows(AlquilerException.class,
                () -> kiosco.alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3)));
        assertEquals("No se pudieron cargar los datos", faltante.getMessage());
        assertFalse(Files.exists(carpeta.resolve("agencia.dat")));

        Files.write(carpeta.resolve("agencia.dat"), new byte[] {1, 2, 3});
        AlquilerException danado = assertThrows(AlquilerException.class,
                () -> kiosco.alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3)));
        assertEquals("No se pudieron cargar los datos", danado.getMessage());
        assertArrayEquals(new byte[] {1, 2, 3}, Files.readAllBytes(carpeta.resolve("agencia.dat")));
    }

    private static Moto honda() {
        return new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000, 8000.0, 125, true);
    }

    private static Auto ka() {
        return new Auto("ZZ999ZZ", "Ford", "Ka", "Negro", 2019, 40000, 9000.0, 4, 5, TipoCaja.MANUAL);
    }

    private static List<String> patentes(List<Vehiculo> vehiculos) {
        List<String> patentes = new java.util.ArrayList<>();
        for (Vehiculo v : vehiculos) {
            patentes.add(v.getPatente());
        }
        return patentes;
    }

    @Test
    void disponiblesDevuelveSoloLosVehiculosDisponiblesDelArchivoEnElOrdenDeLaFlota()
            throws AlquilerException {
        Agencia agencia = new Agencia();
        Moto alquilada = honda();
        alquilada.cambiarEstado(EstadoVehiculo.ALQUILADO);
        new ServicioFlota(agencia).registrarVehiculo(cronos());
        new ServicioFlota(agencia).registrarVehiculo(alquilada);
        new ServicioFlota(agencia).registrarVehiculo(ka());
        GestorPersistencia gestor = gestor();
        gestor.guardar(agencia);

        List<Vehiculo> disponibles = new ServicioKiosco(gestor).disponibles();

        assertEquals(List.of("AB123CD", "ZZ999ZZ"), patentes(disponibles));
    }

    @Test
    void disponiblesYaNoIncluyeElVehiculoQueElOperarioAlquiloOLoDioDeBajaDespues()
            throws AlquilerException {
        Agencia agencia = new Agencia();
        new ServicioClientes(agencia).registrarCliente(ana());
        new ServicioFlota(agencia).registrarVehiculo(cronos());
        new ServicioFlota(agencia).registrarVehiculo(honda());
        new ServicioFlota(agencia).registrarVehiculo(ka());
        GestorPersistencia gestor = gestor();
        gestor.guardar(agencia);
        ServicioKiosco kiosco = new ServicioKiosco(gestor);
        assertEquals(List.of("AB123CD", "A123BCD", "ZZ999ZZ"), patentes(kiosco.disponibles()));

        Agencia delOperario = gestor.cargar();
        new ServicioAlquiler(delOperario).crearContrato(delOperario.getClientes().get(0),
                delOperario.getVehiculos().get(0), new modelo.tarifas.TarifaDiaria(),
                LocalDate.now().plusDays(2));
        new ServicioFlota(delOperario).eliminarVehiculo(delOperario.getVehiculos().get(2));
        gestor.guardar(delOperario);

        assertEquals(List.of("A123BCD"), patentes(kiosco.disponibles()));
    }

    @Test
    void disponiblesIncluyeElVehiculoNuevoYElDevueltoQueElOperarioGuardoDespues()
            throws AlquilerException {
        Agencia agencia = new Agencia();
        new ServicioClientes(agencia).registrarCliente(ana());
        new ServicioFlota(agencia).registrarVehiculo(cronos());
        GestorPersistencia gestor = gestor();
        gestor.guardar(agencia);
        ServicioKiosco kiosco = new ServicioKiosco(gestor);
        kiosco.alquilar("30123456", "AB123CD", LocalDate.now().plusDays(3));
        assertEquals(List.of(), patentes(kiosco.disponibles()));

        Agencia delOperario = gestor.cargar();
        new ServicioFlota(delOperario).registrarVehiculo(ka());
        new ServicioAlquiler(delOperario).registrarDevolucion(delOperario.getContratos().get(0),
                LocalDate.now(), 15200);
        gestor.guardar(delOperario);

        assertEquals(List.of("AB123CD", "ZZ999ZZ"), patentes(kiosco.disponibles()));
    }

    @Test
    void disponiblesConElArchivoAusenteODaniadoLanzaNoSePudieronCargarLosDatos() throws IOException {
        ServicioKiosco kiosco = new ServicioKiosco(gestor());

        AlquilerException ausente = assertThrows(AlquilerException.class, kiosco::disponibles);
        Files.write(carpeta.resolve("agencia.dat"), new byte[] {1, 2, 3});
        AlquilerException danado = assertThrows(AlquilerException.class, kiosco::disponibles);

        assertEquals("No se pudieron cargar los datos", ausente.getMessage());
        assertEquals("No se pudieron cargar los datos", danado.getMessage());
    }
}
