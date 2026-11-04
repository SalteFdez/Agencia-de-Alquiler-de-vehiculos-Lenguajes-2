package modelo.flota;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CamionetaTest {

    @Test
    void precioDiarioDeUnaCamionetaSinTraccion4x4EsElPrecioBasePorUnoCuatro() {
        Camioneta camioneta = new Camioneta("AC123DE", "Toyota", "Hilux", "Gris", 2022, 20000,
                10000.0, 1000.0, false);

        assertEquals(14000.0, camioneta.getPrecioDiario(), 0.001);
    }
}
