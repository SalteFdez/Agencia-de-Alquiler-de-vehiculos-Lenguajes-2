package servicio;

import java.util.ArrayList;
import java.util.List;
import modelo.Agencia;
import modelo.flota.Vehiculo;

public class ServicioFlota {

    private final Agencia agencia;

    public ServicioFlota(Agencia agencia) {
        this.agencia = agencia;
    }

    public void registrarVehiculo(Vehiculo v) {
        agencia.getVehiculos().add(v);
    }

    public List<Vehiculo> listarVehiculos() {
        return new ArrayList<>(agencia.getVehiculos());
    }
}
