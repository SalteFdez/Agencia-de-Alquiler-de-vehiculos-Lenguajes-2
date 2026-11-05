package modelo;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Formatos {

    public static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private Formatos() {
    }
}
