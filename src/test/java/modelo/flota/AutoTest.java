package modelo.flota;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AutoTest {

    @Test
    void precioDiarioDeUnAutoManualEsElPrecioBasePorUno() {
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);

        assertEquals(10000.0, auto.getPrecioDiario(), 0.001);
    }

    @Test
    void precioDiarioDeUnAutoAutomaticoEsElPrecioBasePorUnoQuince() {
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.AUTOMATICA);

        assertEquals(11500.0, auto.getPrecioDiario(), 0.001);
    }
}
