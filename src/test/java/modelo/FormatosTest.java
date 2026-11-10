package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FormatosTest {

    @Test
    void precioUsaElFormatoArgentinoConSimboloMilesYDosDecimales() {
        assertEquals("$30.000,00", Formatos.precio(30000));
        assertEquals("$7.500,50", Formatos.precio(7500.5));
    }
}
