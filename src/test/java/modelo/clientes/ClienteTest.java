package modelo.clientes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;
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
}
