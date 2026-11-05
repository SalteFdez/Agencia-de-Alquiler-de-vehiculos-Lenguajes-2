package vista;

import excepciones.AlquilerException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import javax.swing.JTextField;

public class FormularioCliente {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    static LocalDate fecha(String etiqueta, JTextField campo) throws AlquilerException {
        try {
            return LocalDate.parse(campo.getText().trim(), FORMATO_FECHA);
        } catch (DateTimeParseException ex) {
            throw new AlquilerException("La fecha " + etiqueta + " debe tener el formato dd/mm/aaaa");
        }
    }
}
