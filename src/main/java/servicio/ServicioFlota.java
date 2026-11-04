package servicio;

import excepciones.AlquilerException;
import java.util.ArrayList;
import java.util.List;
import modelo.Agencia;
import modelo.flota.EstadoVehiculo;
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
        if (v.getPrecioBaseDiario() < 0) {
            throw new AlquilerException("El precio base no puede ser negativo");
        }
        v.validarDatosPropios();
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

    public List<Vehiculo> filtrar(String tipo, EstadoVehiculo estado) {
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : agencia.getVehiculos()) {
            boolean cumpleTipo = tipo == null || v.getTipo().equals(tipo);
            boolean cumpleEstado = estado == null || v.getEstado() == estado;
            if (cumpleTipo && cumpleEstado) {
                resultado.add(v);
            }
        }
        return resultado;
    }
}
