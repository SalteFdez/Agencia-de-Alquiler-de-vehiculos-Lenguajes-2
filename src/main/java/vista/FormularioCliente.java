package vista;

import excepciones.AlquilerException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JTextField;

public class FormularioCliente {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static LocalDate fecha(String etiqueta, JTextField campo) throws AlquilerException {
        return LocalDate.parse(campo.getText().trim(), FORMATO_FECHA);
    }
}
