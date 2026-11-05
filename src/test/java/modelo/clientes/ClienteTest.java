package modelo.clientes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.Set;
import modelo.flota.Auto;
import modelo.flota.Moto;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;

class ClienteTest {

    private static LicenciaConducir licencia() {
        return new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                LocalDate.of(2030, 1, 1));
    }

    @Test
    void elNombreCompletoEsElNombreYElApellidoSeparadosPorUnEspacio() {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.of(1990, 5, 20), "3874123456", licencia());

        assertEquals("Ana Gomez", cliente.getNombreCompleto());
    }

    @Test
    void unClienteQueCumpleTreintaAniosHoyTieneTreinta() {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.now().minusYears(30), "3874123456", licencia());

        assertEquals(30, cliente.getEdad());
    }

    @Test
    void unClienteAUnDiaDeCumplirDieciochoAniosTieneDiecisiete() {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.now().minusYears(18).plusDays(1), "3874123456", licencia());

        assertEquals(17, cliente.getEdad());
    }

    @Test
    void toStringMuestraElNombreCompletoYElDni() {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.of(1990, 5, 20), "3874123456", licencia());

        assertEquals("Ana Gomez (DNI 30123456)", cliente.toString());
    }

    private static Auto auto() {
        return new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
    }

    @Test
    void unClienteDeDieciochoAniosHoyConLaCategoriaYLaLicenciaVigenteHastaLaFechaPuedeConducir() {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.now().minusYears(18), "3874123456", licencia());

        assertTrue(cliente.puedeConducir(auto(), LocalDate.of(2029, 6, 1)));
    }

    @Test
    void unClienteDeDiecisieteAniosNoPuedeConducir() {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.now().minusYears(18).plusDays(1), "3874123456", licencia());

        assertFalse(cliente.puedeConducir(auto(), LocalDate.of(2029, 6, 1)));
    }

    @Test
    void unClienteConLicenciaBNoPuedeConducirUnaMoto() {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.of(1990, 5, 20), "3874123456", licencia());
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);

        assertFalse(cliente.puedeConducir(moto, LocalDate.of(2029, 6, 1)));
    }

    @Test
    void unClienteConLaLicenciaVencidaAntesDeLaFechaHastaNoPuedeConducir() {
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456",
                LocalDate.of(1990, 5, 20), "3874123456", licencia());

        assertFalse(cliente.puedeConducir(auto(), LocalDate.of(2030, 1, 2)));
    }
}
