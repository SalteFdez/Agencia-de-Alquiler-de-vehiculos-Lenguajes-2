package excepciones;

public class ConductorNoHabilitadoException extends AlquilerException {

    public ConductorNoHabilitadoException(String motivo) {
        super("El cliente no está habilitado para conducir este vehículo: " + motivo);
    }
}
