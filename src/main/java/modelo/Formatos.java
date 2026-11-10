package modelo;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class Formatos {

    public static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public static String precio(double valor) {
        return String.format(Locale.forLanguageTag("es-AR"), "$%,.2f", valor);
    }

    private Formatos() {
    }
}
