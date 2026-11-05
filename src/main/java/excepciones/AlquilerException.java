package excepciones;

public class AlquilerException extends Exception {

    public AlquilerException(String mensaje) {
        super(mensaje);
    }

    public AlquilerException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
