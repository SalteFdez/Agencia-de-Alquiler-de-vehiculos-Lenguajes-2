package modelo.alquiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.Set;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import modelo.tarifas.Tarifa;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;

class ContratoTest {

    private static Cliente cliente() {
        return new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.of(2035, 1, 1)));
    }

    private static Auto auto(TipoCaja caja) {
        return new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, caja);
    }

    @Test
    void unContratoNuevoQuedaActivoTomaElKmInicialDelVehiculoYGuardaSusDatos() {
        Cliente cliente = cliente();
        Auto auto = auto(TipoCaja.MANUAL);
        Tarifa tarifa = new TarifaDiaria();
        LocalDate inicio = LocalDate.of(2026, 11, 5);
        LocalDate fin = LocalDate.of(2026, 11, 8);

        Contrato contrato = new Contrato(cliente, auto, tarifa, inicio, fin);

        assertEquals(EstadoContrato.ACTIVO, contrato.getEstado());
        assertEquals(15000, contrato.getKmInicial());
        assertSame(cliente, contrato.getCliente());
        assertSame(auto, contrato.getVehiculo());
        assertSame(tarifa, contrato.getTarifa());
        assertEquals(inicio, contrato.getFechaInicio());
        assertEquals(fin, contrato.getFechaFinPrevista());
    }

    @Test
    void losDiasSonLosQueHayEntreElInicioYElFinPrevisto() {
        Contrato contrato = new Contrato(cliente(), auto(TipoCaja.MANUAL), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 8));

        assertEquals(3, contrato.getDias());
    }

    @Test
    void elTotalEsElPrecioDiarioDelVehiculoPorLosDias() {
        Contrato contrato = new Contrato(cliente(), auto(TipoCaja.AUTOMATICA), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 9));

        assertEquals(46000.0, contrato.calcularTotal(), 0.001);
    }

    @Test
    void finalizarGuardaLaFechaDeDevolucionYElKmFinalYDejaElContratoFinalizado() {
        Contrato contrato = new Contrato(cliente(), auto(TipoCaja.MANUAL), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 10));

        contrato.finalizar(LocalDate.of(2026, 11, 7), 15400);

        assertEquals(EstadoContrato.FINALIZADO, contrato.getEstado());
        assertEquals(LocalDate.of(2026, 11, 7), contrato.getFechaDevolucion());
        assertEquals(15400, contrato.getKmFinal());
    }

    @Test
    void losKmRecorridosSonCeroMientrasEstaActivoYElKmFinalMenosElInicialAlFinalizar() {
        Contrato contrato = new Contrato(cliente(), auto(TipoCaja.MANUAL), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 10));

        assertEquals(0, contrato.getKmRecorridos());

        contrato.finalizar(LocalDate.of(2026, 11, 7), 15400);

        assertEquals(400, contrato.getKmRecorridos());
    }

    @Test
    void unContratoDevueltoAntesDelFinPrevistoCobraLosDiasPrevistos() {
        Contrato contrato = new Contrato(cliente(), auto(TipoCaja.MANUAL), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 10));

        contrato.finalizar(LocalDate.of(2026, 11, 7), 15400);

        assertEquals(5, contrato.getDias());
    }

    @Test
    void unContratoDevueltoElDiaDelFinPrevistoCobraLosDiasPrevistos() {
        Contrato contrato = new Contrato(cliente(), auto(TipoCaja.MANUAL), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 10));

        contrato.finalizar(LocalDate.of(2026, 11, 10), 15400);

        assertEquals(5, contrato.getDias());
    }

    @Test
    void unContratoDevueltoDespuesDelFinPrevistoCobraLosDiasReales() {
        Contrato contrato = new Contrato(cliente(), auto(TipoCaja.MANUAL), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 10));

        contrato.finalizar(LocalDate.of(2026, 11, 12), 15400);

        assertEquals(7, contrato.getDias());
    }

    @Test
    void elTotalDeUnContratoFinalizadoUsaLosDiasCobrados() {
        Contrato antes = new Contrato(cliente(), auto(TipoCaja.MANUAL), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 10));
        Contrato despues = new Contrato(cliente(), auto(TipoCaja.MANUAL), new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 10));

        antes.finalizar(LocalDate.of(2026, 11, 7), 15400);
        despues.finalizar(LocalDate.of(2026, 11, 12), 15400);

        assertEquals(50000.0, antes.calcularTotal(), 0.001);
        assertEquals(70000.0, despues.calcularTotal(), 0.001);
    }
}
