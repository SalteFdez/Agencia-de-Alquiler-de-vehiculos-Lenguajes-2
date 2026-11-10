package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import excepciones.AlquilerException;
import excepciones.ConductorNoHabilitadoException;
import excepciones.VehiculoNoDisponibleException;
import excepciones.VehiculoNoDisponibleException;
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
import modelo.flota.Vehiculo;
import modelo.flota.TipoCaja;
import modelo.tarifas.Tarifa;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ServicioAlquilerTest {

    private static Cliente cliente() {
        return new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.now().plusYears(3)));
    }

    private static Cliente clienteCon(LocalDate nacimiento, Set<CategoriaLicencia> categorias,
                                      LocalDate vencimiento) {
        return new Cliente("Ana", "Gomez", "30123456", nacimiento, "3874123456",
                new LicenciaConducir("L123456", categorias, vencimiento));
    }

    private static Moto moto(String patente) {
        return new Moto(patente, "Honda", "CB125", "Rojo", 2023, 5000, 10000.0, 125, true);
    }

    private static Auto auto(String patente) {
        return new Auto(patente, "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
    }

    @Test
    void crearUnContratoValidoLoDevuelveActivoConNumeroUnoYFechaDeInicioHoyYLoGuarda()
            throws AlquilerException {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Cliente cliente = cliente();
        Auto auto = auto("AB123CD");
        Tarifa tarifa = new TarifaDiaria();
        LocalDate fin = LocalDate.now().plusDays(3);

        Contrato contrato = servicio.crearContrato(cliente, auto, tarifa, fin);

        assertEquals(1, contrato.getNumero());
        assertEquals(EstadoContrato.ACTIVO, contrato.getEstado());
        assertEquals(LocalDate.now(), contrato.getFechaInicio());
        assertEquals(fin, contrato.getFechaFinPrevista());
        assertSame(cliente, contrato.getCliente());
        assertSame(auto, contrato.getVehiculo());
        assertSame(tarifa, contrato.getTarifa());
        assertEquals(List.of(contrato), agencia.getContratos());
    }

    @Test
    void elSegundoContratoCreadoRecibeElNumeroDos() throws AlquilerException {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        LocalDate fin = LocalDate.now().plusDays(3);

        Contrato primero = servicio.crearContrato(cliente(), auto("AB123CD"), new TarifaDiaria(), fin);
        Contrato segundo = servicio.crearContrato(cliente(), auto("EF456GH"), new TarifaDiaria(), fin);

        assertEquals(1, primero.getNumero());
        assertEquals(2, segundo.getNumero());
    }

    @Test
    void crearUnContratoDejaElVehiculoAlquilado() throws AlquilerException {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        Auto auto = auto("AB123CD");

        servicio.crearContrato(cliente(), auto, new TarifaDiaria(), LocalDate.now().plusDays(3));

        assertEquals(EstadoVehiculo.ALQUILADO, auto.getEstado());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void unaFechaDeFinIgualOAnteriorAHoyLanzaExcepcionYNoCreaElContrato(int diasAntes) {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        LocalDate fin = LocalDate.now().minusDays(diasAntes);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.crearContrato(cliente(), auto, new TarifaDiaria(), fin));

        assertEquals("La fecha de fin prevista debe ser posterior a hoy", e.getMessage());
        assertTrue(agencia.getContratos().isEmpty());
        assertEquals(EstadoVehiculo.DISPONIBLE, auto.getEstado());
    }

    @Test
    void unVehiculoAlquiladoLanzaVehiculoNoDisponibleYNoCreaElContrato() {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        auto.cambiarEstado(EstadoVehiculo.ALQUILADO);

        VehiculoNoDisponibleException e = assertThrows(VehiculoNoDisponibleException.class,
                () -> servicio.crearContrato(cliente(), auto, new TarifaDiaria(),
                        LocalDate.now().plusDays(3)));

        assertEquals("El vehículo AB123CD no está disponible", e.getMessage());
        assertTrue(agencia.getContratos().isEmpty());
    }

    @Test
    void listarContratosConEstadoNuloDevuelveTodosEnOrdenDeCreacion() throws AlquilerException {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        LocalDate fin = LocalDate.now().plusDays(3);
        Contrato primero = servicio.crearContrato(cliente(), auto("AB123CD"), new TarifaDiaria(), fin);
        Contrato segundo = servicio.crearContrato(cliente(), auto("EF456GH"), new TarifaDiaria(), fin);

        assertEquals(List.of(primero, segundo), servicio.listarContratos(null));
    }

    @Test
    void listarContratosConUnEstadoDevuelveSoloLosDeEseEstado() throws AlquilerException {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        Contrato contrato = servicio.crearContrato(cliente(), auto("AB123CD"), new TarifaDiaria(),
                LocalDate.now().plusDays(3));

        assertEquals(List.of(contrato), servicio.listarContratos(EstadoContrato.ACTIVO));
        assertTrue(servicio.listarContratos(EstadoContrato.FINALIZADO).isEmpty());
    }

    @Test
    void unClienteMenorDeDieciochoAniosLanzaConductorNoHabilitadoYNoCreaElContrato() {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Cliente menor = clienteCon(LocalDate.now().minusYears(17), Set.of(CategoriaLicencia.B),
                LocalDate.now().plusYears(3));

        ConductorNoHabilitadoException e = assertThrows(ConductorNoHabilitadoException.class,
                () -> servicio.crearContrato(menor, auto, new TarifaDiaria(),
                        LocalDate.now().plusDays(3)));

        assertEquals("El cliente no está habilitado para conducir este vehículo: es menor de 18 años",
                e.getMessage());
        assertTrue(agencia.getContratos().isEmpty());
        assertEquals(EstadoVehiculo.DISPONIBLE, auto.getEstado());
    }

    @ParameterizedTest
    @CsvSource({"MOTO,A", "AUTO,B"})
    void unClienteSinLaCategoriaDelVehiculoLanzaConductorNoHabilitadoYNoCreaElContrato(
            String tipo, CategoriaLicencia requerida) {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Vehiculo vehiculo = tipo.equals("MOTO") ? moto("AB123CD") : auto("AB123CD");
        CategoriaLicencia otra = requerida == CategoriaLicencia.A
                ? CategoriaLicencia.B : CategoriaLicencia.A;
        Cliente cliente = clienteCon(LocalDate.of(1990, 5, 20), Set.of(otra),
                LocalDate.now().plusYears(3));

        ConductorNoHabilitadoException e = assertThrows(ConductorNoHabilitadoException.class,
                () -> servicio.crearContrato(cliente, vehiculo, new TarifaDiaria(),
                        LocalDate.now().plusDays(3)));

        assertEquals("El cliente no está habilitado para conducir este vehículo: "
                + "su licencia no tiene la categoría " + requerida, e.getMessage());
        assertTrue(agencia.getContratos().isEmpty());
        assertEquals(EstadoVehiculo.DISPONIBLE, vehiculo.getEstado());
    }

    @Test
    void unaLicenciaQueVenceAntesDelFinLanzaConductorNoHabilitadoYNoCreaElContrato() {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Cliente cliente = clienteCon(LocalDate.of(1990, 5, 20), Set.of(CategoriaLicencia.B),
                LocalDate.of(2099, 3, 14));

        ConductorNoHabilitadoException e = assertThrows(ConductorNoHabilitadoException.class,
                () -> servicio.crearContrato(cliente, auto, new TarifaDiaria(),
                        LocalDate.of(2099, 3, 15)));

        assertEquals("El cliente no está habilitado para conducir este vehículo: "
                + "su licencia no está vigente hasta el 15/03/2099", e.getMessage());
        assertTrue(agencia.getContratos().isEmpty());
        assertEquals(EstadoVehiculo.DISPONIBLE, auto.getEstado());
    }

    @Test
    void unClienteHabilitadoConLaLicenciaVenciendoElDiaDelFinPuedeAlquilar() throws AlquilerException {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        Auto auto = auto("AB123CD");
        LocalDate fin = LocalDate.now().plusDays(3);
        Cliente cliente = clienteCon(LocalDate.of(1990, 5, 20), Set.of(CategoriaLicencia.B), fin);

        Contrato contrato = servicio.crearContrato(cliente, auto, new TarifaDiaria(), fin);

        assertEquals(EstadoContrato.ACTIVO, contrato.getEstado());
        assertEquals(EstadoVehiculo.ALQUILADO, auto.getEstado());
    }

    @Test
    void conVariosProblemasSeInformaPrimeroLaEdadYDespuesLaCategoria() {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        Auto auto = auto("AB123CD");
        Cliente menorSinCategoria = clienteCon(LocalDate.now().minusYears(17),
                Set.of(CategoriaLicencia.A), LocalDate.now().minusDays(1));
        Cliente sinCategoriaYVencida = clienteCon(LocalDate.of(1990, 5, 20),
                Set.of(CategoriaLicencia.A), LocalDate.now().plusDays(1));
        LocalDate fin = LocalDate.now().plusDays(3);

        ConductorNoHabilitadoException primero = assertThrows(ConductorNoHabilitadoException.class,
                () -> servicio.crearContrato(menorSinCategoria, auto, new TarifaDiaria(), fin));
        ConductorNoHabilitadoException segundo = assertThrows(ConductorNoHabilitadoException.class,
                () -> servicio.crearContrato(sinCategoriaYVencida, auto, new TarifaDiaria(), fin));

        assertEquals("El cliente no está habilitado para conducir este vehículo: es menor de 18 años",
                primero.getMessage());
        assertEquals("El cliente no está habilitado para conducir este vehículo: "
                + "su licencia no tiene la categoría B", segundo.getMessage());
    }

    @Test
    void unVehiculoNoDisponibleSeInformaAntesQueUnConductorNoHabilitado() {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        Auto auto = auto("AB123CD");
        auto.cambiarEstado(EstadoVehiculo.ALQUILADO);
        Cliente menor = clienteCon(LocalDate.now().minusYears(17), Set.of(CategoriaLicencia.B),
                LocalDate.now().plusYears(3));

        assertThrows(VehiculoNoDisponibleException.class,
                () -> servicio.crearContrato(menor, auto, new TarifaDiaria(),
                        LocalDate.now().plusDays(3)));
    }

    private static Contrato contratoActivo(Agencia agencia, Auto auto, LocalDate inicio,
                                           LocalDate finPrevisto) {
        Contrato contrato = new Contrato(cliente(), auto, new TarifaDiaria(), inicio, finPrevisto);
        contrato.setNumero(agencia.siguienteNroContrato());
        auto.cambiarEstado(EstadoVehiculo.ALQUILADO);
        agencia.getContratos().add(contrato);
        return contrato;
    }

    @Test
    void registrarUnaDevolucionValidaFinalizaElContratoActualizaElKmDelVehiculoYLoDejaDisponible()
            throws AlquilerException {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Contrato contrato = contratoActivo(agencia, auto, LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(2));

        servicio.registrarDevolucion(contrato, LocalDate.now(), 15400);

        assertEquals(EstadoContrato.FINALIZADO, contrato.getEstado());
        assertEquals(LocalDate.now(), contrato.getFechaDevolucion());
        assertEquals(15400, contrato.getKmFinal());
        assertEquals(15400, auto.getKilometraje());
        assertEquals(EstadoVehiculo.DISPONIBLE, auto.getEstado());
    }

    @Test
    void devolverUnContratoYaFinalizadoLanzaExcepcionYNoCambiaNiElContratoNiElVehiculo() {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Contrato contrato = contratoActivo(agencia, auto, LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(2));
        contrato.finalizar(LocalDate.now().minusDays(1), 15400);

        AlquilerException error = assertThrows(AlquilerException.class,
                () -> servicio.registrarDevolucion(contrato, LocalDate.now(), 16000));

        assertEquals("El contrato 1 ya está finalizado", error.getMessage());
        assertEquals(LocalDate.now().minusDays(1), contrato.getFechaDevolucion());
        assertEquals(15400, contrato.getKmFinal());
        assertEquals(15000, auto.getKilometraje());
        assertEquals(EstadoVehiculo.ALQUILADO, auto.getEstado());
    }

    @Test
    void devolverConFechaAnteriorAlInicioLanzaExcepcionYNoCambiaNada() {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Contrato contrato = contratoActivo(agencia, auto, LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(2));

        AlquilerException error = assertThrows(AlquilerException.class,
                () -> servicio.registrarDevolucion(contrato, LocalDate.now().minusDays(4), 15400));

        assertEquals("La fecha de devolución no puede ser anterior al inicio del contrato",
                error.getMessage());
        assertEquals(EstadoContrato.ACTIVO, contrato.getEstado());
        assertEquals(15000, auto.getKilometraje());
        assertEquals(EstadoVehiculo.ALQUILADO, auto.getEstado());
    }

    @Test
    void devolverConFechaPosteriorAHoyLanzaExcepcionYNoCambiaNada() {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Contrato contrato = contratoActivo(agencia, auto, LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(2));

        AlquilerException error = assertThrows(AlquilerException.class,
                () -> servicio.registrarDevolucion(contrato, LocalDate.now().plusDays(1), 15400));

        assertEquals("La fecha de devolución no puede ser posterior a hoy", error.getMessage());
        assertEquals(EstadoContrato.ACTIVO, contrato.getEstado());
        assertEquals(15000, auto.getKilometraje());
        assertEquals(EstadoVehiculo.ALQUILADO, auto.getEstado());
    }

    @Test
    void devolverConKmFinalMenorAlInicialLanzaExcepcionYNoCambiaNada() {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Contrato contrato = contratoActivo(agencia, auto, LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(2));

        AlquilerException error = assertThrows(AlquilerException.class,
                () -> servicio.registrarDevolucion(contrato, LocalDate.now(), 14999));

        assertEquals("El km final no puede ser menor al km inicial (15000)", error.getMessage());
        assertEquals(EstadoContrato.ACTIVO, contrato.getEstado());
        assertEquals(15000, auto.getKilometraje());
        assertEquals(EstadoVehiculo.ALQUILADO, auto.getEstado());
    }

    @Test
    void devolverConKmFinalIgualAlInicialSeAcepta() throws AlquilerException {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Contrato contrato = contratoActivo(agencia, auto, LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(2));

        servicio.registrarDevolucion(contrato, LocalDate.now(), 15000);

        assertEquals(EstadoContrato.FINALIZADO, contrato.getEstado());
        assertEquals(0, contrato.getKmRecorridos());
    }

    @Test
    void conVariosProblemasSeInformaPrimeroElContratoFinalizadoLuegoLaFechaYDespuesElKm() {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Auto auto = auto("AB123CD");
        Contrato finalizado = contratoActivo(agencia, auto, LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(2));
        finalizado.finalizar(LocalDate.now().minusDays(1), 15400);
        Contrato activo = contratoActivo(agencia, auto("XY987ZW"), LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(2));

        AlquilerException finalizadoYFechaMala = assertThrows(AlquilerException.class,
                () -> servicio.registrarDevolucion(finalizado, LocalDate.now().plusDays(1), 100));
        AlquilerException fechaPosteriorYKmMalo = assertThrows(AlquilerException.class,
                () -> servicio.registrarDevolucion(activo, LocalDate.now().plusDays(1), 100));
        AlquilerException fechaAnteriorYKmMalo = assertThrows(AlquilerException.class,
                () -> servicio.registrarDevolucion(activo, LocalDate.now().minusDays(4), 100));

        assertEquals("El contrato 1 ya está finalizado", finalizadoYFechaMala.getMessage());
        assertEquals("La fecha de devolución no puede ser posterior a hoy",
                fechaPosteriorYKmMalo.getMessage());
        assertEquals("La fecha de devolución no puede ser anterior al inicio del contrato",
                fechaAnteriorYKmMalo.getMessage());
    }

    @Test
    void elTotalEstimadoEsElPrecioDiarioPorLosDiasHastaLaFechaDeFin() throws AlquilerException {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000, 8000.0, 125, true);
        LocalDate fin = LocalDate.now().plusDays(3);

        assertEquals(30000.0, servicio.calcularTotalEstimado(auto("AB123CD"), fin), 0.001);
        assertEquals(14400.0, servicio.calcularTotalEstimado(moto, fin), 0.001);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void elTotalEstimadoConUnaFechaQueNoEsPosteriorAHoyLanzaExcepcion(int dias) {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.calcularTotalEstimado(auto("AB123CD"), LocalDate.now().plusDays(dias)));

        assertEquals("La fecha de fin prevista debe ser posterior a hoy", e.getMessage());
    }

    @Test
    void buscarPorNumeroDevuelveElContratoConEseNumero() throws AlquilerException {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        servicio.crearContrato(cliente(), auto("AB123CD"), new TarifaDiaria(),
                LocalDate.now().plusDays(2));
        Contrato segundo = servicio.crearContrato(cliente(), auto("XY987ZW"), new TarifaDiaria(),
                LocalDate.now().plusDays(3));

        assertSame(segundo, servicio.buscarPorNumero(2).get());
    }

    @Test
    void buscarPorNumeroDevuelveVacioSiNoExisteEseNumero() throws AlquilerException {
        ServicioAlquiler servicio = new ServicioAlquiler(new Agencia());
        servicio.crearContrato(cliente(), auto("AB123CD"), new TarifaDiaria(),
                LocalDate.now().plusDays(2));

        assertTrue(servicio.buscarPorNumero(7).isEmpty());
    }
}
