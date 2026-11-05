package excepciones;

public class VehiculoNoDisponibleException extends AlquilerException {

    public VehiculoNoDisponibleException(String patente) {
        super("El vehículo " + patente + " no está disponible");
    }
}
