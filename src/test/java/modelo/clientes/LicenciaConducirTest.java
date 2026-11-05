package modelo.clientes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LicenciaConducirTest {

    @Test
    void unaLicenciaSigueVigenteElDiaDeSuVencimiento() {
        LicenciaConducir licencia = new LicenciaConducir("L123456",
                Set.of(CategoriaLicencia.B), LocalDate.of(2027, 3, 15));

        assertTrue(licencia.estaVigente(LocalDate.of(2027, 3, 15)));
    }

    @Test
    void unaLicenciaYaNoEstaVigenteAlDiaSiguienteDeSuVencimiento() {
        LicenciaConducir licencia = new LicenciaConducir("L123456",
                Set.of(CategoriaLicencia.B), LocalDate.of(2027, 3, 15));

        assertFalse(licencia.estaVigente(LocalDate.of(2027, 3, 16)));
    }
}
