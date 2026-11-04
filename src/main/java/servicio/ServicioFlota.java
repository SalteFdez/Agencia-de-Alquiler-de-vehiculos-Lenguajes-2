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
        for (Vehiculo existente : agencia.getVehiculos()) {
            if (existente.getPatente().equals(v.getPatente())) {
                throw new AlquilerException("La patente " + v.getPatente() + " ya está registrada");
            }
        }
        agencia.getVehiculos().add(v);
    }

    public List<Vehiculo> listarVehiculos() {
        return new ArrayList<>(agencia.getVehiculos());
    }
}
