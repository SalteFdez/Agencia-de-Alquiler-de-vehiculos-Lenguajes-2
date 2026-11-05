package servicio;

import excepciones.AlquilerException;
import excepciones.VehiculoNoDisponibleException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Agencia;
import modelo.alquiler.Contrato;
import modelo.alquiler.EstadoContrato;
import modelo.clientes.Cliente;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Vehiculo;
import modelo.tarifas.Tarifa;

public class ServicioAlquiler {

    private final Agencia agencia;

    public ServicioAlquiler(Agencia agencia) {
        this.agencia = agencia;
    }

    public Contrato crearContrato(Cliente c, Vehiculo v, Tarifa t, LocalDate fin)
            throws AlquilerException {
        if (!fin.isAfter(LocalDate.now())) {
            throw new AlquilerException("La fecha de fin prevista debe ser posterior a hoy");
        }
        if (!v.estaDisponible()) {
            throw new VehiculoNoDisponibleException(v.getPatente());
        }
        Contrato contrato = new Contrato(c, v, t, LocalDate.now(), fin);
        contrato.setNumero(agencia.siguienteNroContrato());
        v.cambiarEstado(EstadoVehiculo.ALQUILADO);
        agencia.getContratos().add(contrato);
        return contrato;
    }

    public List<Contrato> listarContratos(EstadoContrato estado) {
        List<Contrato> encontrados = new ArrayList<>();
        for (Contrato c : agencia.getContratos()) {
            if (estado == null || c.getEstado() == estado) {
                encontrados.add(c);
            }
        }
        return encontrados;
    }
}
