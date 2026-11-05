package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;

import excepciones.AlquilerException;
import java.time.LocalDate;
import javax.swing.JTextField;
import org.junit.jupiter.api.Test;

class FormularioClienteTest {

    @Test
    void unaFechaEnFormatoDiaMesAnioSeConvierteALaFechaCorrespondiente() throws AlquilerException {
        JTextField campo = new JTextField("15/03/2030");

        assertEquals(LocalDate.of(2030, 3, 15), FormularioCliente.fecha("Fecha de vencimiento", campo));
    }
}
