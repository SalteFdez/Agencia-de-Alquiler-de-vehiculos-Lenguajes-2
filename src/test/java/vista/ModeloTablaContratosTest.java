package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import excepciones.AlquilerException;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import modelo.Agencia;
import modelo.alquiler.Contrato;
import modelo.alquiler.EstadoContrato;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;
import servicio.ServicioAlquiler;

class ModeloTablaContratosTest {

    @Test
    void laFilaDeUnContratoMuestraNumeroClienteVehiculoFechasTarifaEstadoYTotal() {
        Agencia agencia = new Agencia();
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20),
                "3874123456", new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.of(2035, 1, 1)));
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Contrato contrato = new Contrato(cliente, auto, new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 8));
        contrato.setNumero(1);
        agencia.getContratos().add(contrato);

        ModeloTablaContratos modelo = new ModeloTablaContratos(new ServicioAlquiler(agencia));

        assertEquals(1, modelo.getRowCount());
        assertEquals(8, modelo.getColumnCount());
        assertEquals("Nº", modelo.getColumnName(0));
        assertEquals("Cliente", modelo.getColumnName(1));
        assertEquals("Vehículo", modelo.getColumnName(2));
        assertEquals("Inicio", modelo.getColumnName(3));
        assertEquals("Fin prevista", modelo.getColumnName(4));
        assertEquals("Tarifa", modelo.getColumnName(5));
        assertEquals("Estado", modelo.getColumnName(6));
        assertEquals("Total", modelo.getColumnName(7));
        assertEquals(1, modelo.getValueAt(0, 0));
        assertEquals("Ana Gomez", modelo.getValueAt(0, 1));
        assertEquals("AB123CD", modelo.getValueAt(0, 2));
        assertEquals("05/11/2026", modelo.getValueAt(0, 3));
        assertEquals("08/11/2026", modelo.getValueAt(0, 4));
        assertEquals("Diaria", modelo.getValueAt(0, 5));
        assertEquals("ACTIVO", modelo.getValueAt(0, 6));
        assertEquals(30000.0, (Double) modelo.getValueAt(0, 7), 0.001);
    }

    private static Contrato contrato(int numero, String patente, LocalDate inicio, LocalDate fin) {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20),
                "3874123456", new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.of(2035, 1, 1)));
        Auto auto = new Auto(patente, "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Contrato contrato = new Contrato(cliente, auto, new TarifaDiaria(), inicio, fin);
        contrato.setNumero(numero);
        return contrato;
    }

    @Test
    void getContratoEnDevuelveElContratoDeEsaFila() {
        Agencia agencia = new Agencia();
        Contrato primero = contrato(1, "AB123CD", LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 8));
        Contrato segundo = contrato(2, "XY987ZW", LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 9));
        agencia.getContratos().add(primero);
        agencia.getContratos().add(segundo);
        ModeloTablaContratos modelo = new ModeloTablaContratos(new ServicioAlquiler(agencia));

        assertSame(primero, modelo.getContratoEn(0));
        assertSame(segundo, modelo.getContratoEn(1));
    }

    @Test
    void laFilaDeUnContratoFinalizadoMuestraEstadoFinalizadoYElTotalConLosDiasCobrados() {
        Agencia agencia = new Agencia();
        Contrato contrato = contrato(1, "AB123CD", LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 10));
        contrato.finalizar(LocalDate.of(2026, 11, 12), 15400);
        agencia.getContratos().add(contrato);
        ModeloTablaContratos modelo = new ModeloTablaContratos(new ServicioAlquiler(agencia));

        assertEquals("FINALIZADO", modelo.getValueAt(0, 6));
        assertEquals(70000.0, (Double) modelo.getValueAt(0, 7), 0.001);
    }

    private static ModeloTablaContratos modeloConUnActivoYUnFinalizado(Agencia agencia) {
        Contrato activo = contrato(1, "AB123CD", LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 8));
        Contrato finalizado = contrato(2, "XY987ZW", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 4));
        finalizado.finalizar(LocalDate.of(2026, 11, 4), 15300);
        agencia.getContratos().add(activo);
        agencia.getContratos().add(finalizado);
        return new ModeloTablaContratos(new ServicioAlquiler(agencia));
    }

    @Test
    void conUnEstadoElegidoLaTablaMuestraSoloLosContratosConEseEstado() {
        ModeloTablaContratos modelo = modeloConUnActivoYUnFinalizado(new Agencia());

        modelo.setFiltro(EstadoContrato.ACTIVO);

        assertEquals(1, modelo.getRowCount());
        assertEquals(1, modelo.getValueAt(0, 0));
        assertEquals("ACTIVO", modelo.getValueAt(0, 6));

        modelo.setFiltro(EstadoContrato.FINALIZADO);

        assertEquals(1, modelo.getRowCount());
        assertEquals(2, modelo.getValueAt(0, 0));
        assertEquals("FINALIZADO", modelo.getValueAt(0, 6));
    }

    @Test
    void volverASinFiltroMuestraTodosLosContratos() {
        ModeloTablaContratos modelo = modeloConUnActivoYUnFinalizado(new Agencia());
        modelo.setFiltro(EstadoContrato.ACTIVO);

        modelo.setFiltro(null);

        assertEquals(2, modelo.getRowCount());
        assertEquals(1, modelo.getValueAt(0, 0));
        assertEquals(2, modelo.getValueAt(1, 0));
    }

    @Test
    void cambiarElFiltroAvisaALaTablaQueLosDatosCambiaron() {
        ModeloTablaContratos modelo = new ModeloTablaContratos(new ServicioAlquiler(new Agencia()));
        AtomicInteger avisos = new AtomicInteger();
        modelo.addTableModelListener(e -> avisos.incrementAndGet());

        modelo.setFiltro(EstadoContrato.ACTIVO);

        assertEquals(1, avisos.get());
    }

    @Test
    void getContratoEnRespetaElFiltroElegido() {
        Agencia agencia = new Agencia();
        Contrato finalizado = contrato(1, "AB123CD", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 4));
        finalizado.finalizar(LocalDate.of(2026, 11, 4), 15300);
        Contrato activo = contrato(2, "XY987ZW", LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 8));
        agencia.getContratos().add(finalizado);
        agencia.getContratos().add(activo);
        ModeloTablaContratos modelo = new ModeloTablaContratos(new ServicioAlquiler(agencia));

        modelo.setFiltro(EstadoContrato.ACTIVO);

        assertSame(activo, modelo.getContratoEn(0));
    }

    @Test
    void conElFiltroActivoUnContratoRecienDevueltoDesapareceDeLaTabla() throws AlquilerException {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Contrato primero = contrato(1, "AB123CD", LocalDate.now().minusDays(3), LocalDate.now().plusDays(2));
        Contrato segundo = contrato(2, "XY987ZW", LocalDate.now().minusDays(2), LocalDate.now().plusDays(3));
        agencia.getContratos().add(primero);
        agencia.getContratos().add(segundo);
        ModeloTablaContratos modelo = new ModeloTablaContratos(servicio);
        modelo.setFiltro(EstadoContrato.ACTIVO);
        assertEquals(2, modelo.getRowCount());

        servicio.registrarDevolucion(primero, LocalDate.now(), 15400);

        assertEquals(1, modelo.getRowCount());
        assertSame(segundo, modelo.getContratoEn(0));
        modelo.setFiltro(EstadoContrato.FINALIZADO);
        assertEquals(1, modelo.getRowCount());
        assertSame(primero, modelo.getContratoEn(0));
    }

    @Test
    void unContratoNuevoApareceConElFiltroActivoOTodosYNoConElFiltroFinalizado()
            throws AlquilerException {
        Agencia agencia = new Agencia();
        ServicioAlquiler servicio = new ServicioAlquiler(agencia);
        Contrato finalizado = contrato(1, "AB123CD", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 4));
        finalizado.finalizar(LocalDate.of(2026, 11, 4), 15300);
        agencia.getContratos().add(finalizado);
        agencia.siguienteNroContrato();
        ModeloTablaContratos modelo = new ModeloTablaContratos(servicio);
        modelo.setFiltro(EstadoContrato.FINALIZADO);

        Contrato nuevo = servicio.crearContrato(finalizado.getCliente(),
                new Auto("NV456EW", "Fiat", "Cronos", "Blanco", 2022, 15000, 10000.0, 4, 5, TipoCaja.MANUAL),
                new TarifaDiaria(), LocalDate.now().plusDays(3));

        assertEquals(1, modelo.getRowCount());
        assertSame(finalizado, modelo.getContratoEn(0));
        modelo.setFiltro(EstadoContrato.ACTIVO);
        assertEquals(1, modelo.getRowCount());
        assertSame(nuevo, modelo.getContratoEn(0));
        modelo.setFiltro(null);
        assertEquals(2, modelo.getRowCount());
    }
}
