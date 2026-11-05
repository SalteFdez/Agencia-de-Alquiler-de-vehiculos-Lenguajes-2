package persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import excepciones.AlquilerException;
import java.io.File;
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
import modelo.flota.Camioneta;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Moto;
import modelo.flota.TipoCaja;
import modelo.flota.Vehiculo;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GestorPersistenciaTest {

    @TempDir
    Path carpeta;

    @Test
    void existeArchivoEsFalsoSiNoHayArchivoYVerdaderoDespuesDeGuardar() throws AlquilerException {
        GestorPersistencia gestor = new GestorPersistencia(new File(carpeta.toFile(), "agencia.dat"));

        assertFalse(gestor.existeArchivo());

        gestor.guardar(new Agencia());

        assertTrue(gestor.existeArchivo());
    }

    @Test
    void guardarYCargarDevuelveLosMismosVehiculosConSusDatosEstadosYKilometrajes()
            throws AlquilerException {
        GestorPersistencia gestor = new GestorPersistencia(new File(carpeta.toFile(), "agencia.dat"));
        Agencia agencia = new Agencia();
        agencia.getVehiculos().add(new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.AUTOMATICA));
        Moto moto = new Moto("XY987ZW", "Honda", "CB125", "Rojo", 2023, 5000, 8000.0, 125, true);
        moto.cambiarEstado(EstadoVehiculo.ALQUILADO);
        agencia.getVehiculos().add(moto);
        agencia.getVehiculos().add(new Camioneta("CA555AA", "Toyota", "Hilux", "Gris", 2021, 42000,
                20000.0, 1000.0, true));
        gestor.guardar(agencia);

        Agencia cargada = gestor.cargar();

        assertEquals(3, cargada.getVehiculos().size());
        Vehiculo auto = cargada.getVehiculos().get(0);
        assertEquals("Auto", auto.getTipo());
        assertEquals("AB123CD", auto.getPatente());
        assertEquals("Fiat", auto.getMarca());
        assertEquals("Cronos", auto.getModelo());
        assertEquals(2022, auto.getAnio());
        assertEquals(15000, auto.getKilometraje());
        assertEquals(EstadoVehiculo.DISPONIBLE, auto.getEstado());
        assertEquals(11500.0, auto.getPrecioDiario(), 0.001);
        Vehiculo motoCargada = cargada.getVehiculos().get(1);
        assertEquals("Moto", motoCargada.getTipo());
        assertEquals("XY987ZW", motoCargada.getPatente());
        assertEquals(5000, motoCargada.getKilometraje());
        assertEquals(EstadoVehiculo.ALQUILADO, motoCargada.getEstado());
        Vehiculo camioneta = cargada.getVehiculos().get(2);
        assertEquals("Camioneta", camioneta.getTipo());
        assertEquals("CA555AA", camioneta.getPatente());
        assertEquals(42000, camioneta.getKilometraje());
    }

    @Test
    void guardarYCargarDevuelveLosMismosClientesConSuLicencia() throws AlquilerException {
        GestorPersistencia gestor = new GestorPersistencia(new File(carpeta.toFile(), "agencia.dat"));
        Agencia agencia = new Agencia();
        Cliente ana = new Cliente("Ana", "Gomez", "30123456", LocalDate.now().minusYears(30), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.A, CategoriaLicencia.B),
                        LocalDate.of(2035, 1, 1)));
        ana.setId(agencia.siguienteIdCliente());
        agencia.getClientes().add(ana);
        gestor.guardar(agencia);

        Agencia cargada = gestor.cargar();

        assertEquals(1, cargada.getClientes().size());
        Cliente cliente = cargada.getClientes().get(0);
        assertEquals(1, cliente.getId());
        assertEquals("Ana", cliente.getNombre());
        assertEquals("Gomez", cliente.getApellido());
        assertEquals("30123456", cliente.getDni());
        assertEquals(30, cliente.getEdad());
        assertEquals("3874123456", cliente.getTelefono());
        assertEquals("L123456", cliente.getLicencia().getNumero());
        assertEquals(Set.of(CategoriaLicencia.A, CategoriaLicencia.B), cliente.getLicencia().getCategorias());
        assertEquals(LocalDate.of(2035, 1, 1), cliente.getLicencia().getFechaVencimiento());
    }

    @Test
    void guardarYCargarDevuelveLosContratosActivosYFinalizadosConSusEstadosFechasKmYTotales()
            throws AlquilerException {
        GestorPersistencia gestor = new GestorPersistencia(new File(carpeta.toFile(), "agencia.dat"));
        Agencia agencia = new Agencia();
        Cliente ana = new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B), LocalDate.of(2035, 1, 1)));
        Auto primero = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Auto segundo = new Auto("XY987ZW", "VW", "Gol", "Azul", 2020, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Contrato activo = new Contrato(ana, primero, new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 8));
        activo.setNumero(agencia.siguienteNroContrato());
        Contrato finalizado = new Contrato(ana, segundo, new TarifaDiaria(),
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 4));
        finalizado.setNumero(agencia.siguienteNroContrato());
        finalizado.finalizar(LocalDate.of(2026, 11, 6), 15400);
        agencia.getContratos().add(activo);
        agencia.getContratos().add(finalizado);
        gestor.guardar(agencia);

        Agencia cargada = gestor.cargar();

        assertEquals(2, cargada.getContratos().size());
        Contrato a = cargada.getContratos().get(0);
        assertEquals(1, a.getNumero());
        assertEquals(EstadoContrato.ACTIVO, a.getEstado());
        assertEquals(LocalDate.of(2026, 11, 5), a.getFechaInicio());
        assertEquals(LocalDate.of(2026, 11, 8), a.getFechaFinPrevista());
        assertEquals("Diaria", a.getTarifa().getNombre());
        assertEquals(15000, a.getKmInicial());
        assertEquals(30000.0, a.calcularTotal(), 0.001);
        Contrato f = cargada.getContratos().get(1);
        assertEquals(2, f.getNumero());
        assertEquals(EstadoContrato.FINALIZADO, f.getEstado());
        assertEquals(LocalDate.of(2026, 11, 6), f.getFechaDevolucion());
        assertEquals(15400, f.getKmFinal());
        assertEquals(400, f.getKmRecorridos());
        assertEquals(50000.0, f.calcularTotal(), 0.001);
    }

    @Test
    void trasCargarCadaContratoApuntaAlMismoVehiculoYAlMismoClienteQueLaAgencia()
            throws AlquilerException {
        GestorPersistencia gestor = new GestorPersistencia(new File(carpeta.toFile(), "agencia.dat"));
        Agencia agencia = new Agencia();
        Cliente ana = new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B), LocalDate.of(2035, 1, 1)));
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        agencia.getClientes().add(ana);
        agencia.getVehiculos().add(auto);
        Contrato contrato = new Contrato(ana, auto, new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 8));
        agencia.getContratos().add(contrato);
        gestor.guardar(agencia);

        Agencia cargada = gestor.cargar();

        assertSame(cargada.getVehiculos().get(0), cargada.getContratos().get(0).getVehiculo());
        assertSame(cargada.getClientes().get(0), cargada.getContratos().get(0).getCliente());
    }

    @Test
    void trasCargarLaNumeracionContinuaConDosClientesYTresContratosElSiguienteEsElTresYElCuatro()
            throws AlquilerException {
        GestorPersistencia gestor = new GestorPersistencia(new File(carpeta.toFile(), "agencia.dat"));
        Agencia agencia = new Agencia();
        agencia.siguienteIdCliente();
        agencia.siguienteIdCliente();
        agencia.siguienteNroContrato();
        agencia.siguienteNroContrato();
        agencia.siguienteNroContrato();
        gestor.guardar(agencia);

        Agencia cargada = gestor.cargar();

        assertEquals(3, cargada.siguienteIdCliente());
        assertEquals(4, cargada.siguienteNroContrato());
    }
}
