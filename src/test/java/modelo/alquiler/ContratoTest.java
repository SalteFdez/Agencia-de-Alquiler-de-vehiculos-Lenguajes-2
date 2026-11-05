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
}
