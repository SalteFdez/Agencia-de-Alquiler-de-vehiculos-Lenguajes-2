package servicio;

import excepciones.AlquilerException;
import java.util.ArrayList;
import java.util.List;
import modelo.Agencia;
import modelo.flota.Vehiculo;

public class ServicioFlota {

    private final Agencia agencia;

    public ServicioFlota(Agencia agencia) {
        this.agencia = agencia;
    }

    public void registrarVehiculo(Vehiculo v) throws AlquilerException {
        if (v.getPatente().trim().isEmpty()) {
            throw new AlquilerException("La patente no puede estar vacía");
        }
        if (v.getKilometraje() < 0) {
            throw new AlquilerException("El kilometraje no puede ser negativo");
        }
        for (Vehiculo existente : agencia.getVehiculos()) {
            if (existente.getPatente().equalsIgnoreCase(v.getPatente())) {
                throw new AlquilerException("La patente " + v.getPatente() + " ya está registrada");
            }
        }
        agencia.getVehiculos().add(v);
    }

    public List<Vehiculo> listarVehiculos() {
        return new ArrayList<>(agencia.getVehiculos());
    }
}
