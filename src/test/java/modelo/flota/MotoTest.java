package modelo.flota;

import static org.junit.jupiter.api.Assertions.assertEquals;

import modelo.clientes.CategoriaLicencia;
import org.junit.jupiter.api.Test;

class MotoTest {

    @Test
    void precioDiarioDeUnaMotoEsElPrecioBasePorCeroSeis() {
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);

        assertEquals(6000.0, moto.getPrecioDiario(), 0.001);
    }

    @Test
    void unaMotoRequiereLicenciaDeCategoriaA() {
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);

        assertEquals(CategoriaLicencia.A, moto.getLicenciaRequerida());
    }
}
