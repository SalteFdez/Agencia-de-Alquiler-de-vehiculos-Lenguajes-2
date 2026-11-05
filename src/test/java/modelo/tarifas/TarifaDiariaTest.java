package modelo.tarifas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;

class TarifaDiariaTest {

    private static Auto autoManual() {
        return new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
    }

    @Test
    void calcularDevuelveElPrecioDiarioPorLosDiasEIgnoraLosKm() {
        TarifaDiaria tarifa = new TarifaDiaria();

        assertEquals(30000.0, tarifa.calcular(autoManual(), 3, 500), 0.001);
        assertEquals(30000.0, tarifa.calcular(autoManual(), 3, 0), 0.001);
    }

    @Test
    void elNombreDeLaTarifaEsDiaria() {
        assertEquals("Diaria", new TarifaDiaria().getNombre());
    }
}
