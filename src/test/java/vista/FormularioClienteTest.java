package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import excepciones.AlquilerException;
import java.time.LocalDate;
import javax.swing.JTextField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FormularioClienteTest {

    @Test
    void unaFechaEnFormatoDiaMesAnioSeConvierteALaFechaCorrespondiente() throws AlquilerException {
        JTextField campo = new JTextField("15/03/2030");

        assertEquals(LocalDate.of(2030, 3, 15), FormularioCliente.fecha("Fecha de vencimiento", campo));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2030-03-15", "15-03-2030", "31/02/2020", "abc", "", "   "})
    void unaFechaConOtroFormatoInexistenteOVaciaLanzaExcepcion(String texto) {
        JTextField campo = new JTextField(texto);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> FormularioCliente.fecha("de nacimiento", campo));

        assertEquals("La fecha de nacimiento debe tener el formato dd/mm/aaaa", e.getMessage());
    }
}
