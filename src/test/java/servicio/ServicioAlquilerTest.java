package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import excepciones.AlquilerException;
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
import modelo.flota.TipoCaja;
import modelo.tarifas.Tarifa;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ServicioAlquilerTest {

    private static Cliente cliente() {
        return new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.now().plusYears(3)));
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
}
