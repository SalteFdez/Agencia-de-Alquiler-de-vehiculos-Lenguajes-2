package servicio;

import excepciones.AlquilerException;
import excepciones.ConductorNoHabilitadoException;
import excepciones.VehiculoNoDisponibleException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import modelo.Agencia;
import modelo.Formatos;
import modelo.alquiler.Contrato;
import modelo.alquiler.EstadoContrato;
import modelo.clientes.Cliente;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Vehiculo;
import modelo.tarifas.Tarifa;
import modelo.tarifas.TarifaDiaria;

public class ServicioAlquiler {

    private final Agencia agencia;

    public ServicioAlquiler(Agencia agencia) {
        this.agencia = agencia;
    }

    public Contrato crearContrato(Cliente c, Vehiculo v, Tarifa t, LocalDate fin)
            throws AlquilerException {
        exigirFinPosteriorAHoy(fin);
        if (!v.estaDisponible()) {
            throw new VehiculoNoDisponibleException(v.getPatente());
        }
        if (!c.puedeConducir(v, fin)) {
            throw new ConductorNoHabilitadoException(motivoDeRechazo(c, v, fin));
        }
        Contrato contrato = new Contrato(c, v, t, LocalDate.now(), fin);
        contrato.setNumero(agencia.siguienteNroContrato());
        v.cambiarEstado(EstadoVehiculo.ALQUILADO);
        agencia.getContratos().add(contrato);
        return contrato;
    }

    public double calcularTotalEstimado(Vehiculo v, LocalDate fin) throws AlquilerException {
        exigirFinPosteriorAHoy(fin);
        return new TarifaDiaria().calcular(v, ChronoUnit.DAYS.between(LocalDate.now(), fin), 0);
    }

    private void exigirFinPosteriorAHoy(LocalDate fin) throws AlquilerException {
        if (!fin.isAfter(LocalDate.now())) {
            throw new AlquilerException("La fecha de fin prevista debe ser posterior a hoy");
        }
    }

    private String motivoDeRechazo(Cliente c, Vehiculo v, LocalDate fin) {
        if (c.getEdad() < Cliente.EDAD_MINIMA) {
            return "es menor de " + Cliente.EDAD_MINIMA + " años";
        }
        if (!c.getLicencia().getCategorias().contains(v.getLicenciaRequerida())) {
            return "su licencia no tiene la categoría " + v.getLicenciaRequerida();
        }
        return "su licencia no está vigente hasta el " + fin.format(Formatos.FECHA);
    }

    public void registrarDevolucion(Contrato c, LocalDate fecha, int kmFinal)
            throws AlquilerException {
        if (c.getEstado() == EstadoContrato.FINALIZADO) {
            throw new AlquilerException("El contrato " + c.getNumero() + " ya está finalizado");
        }
        if (fecha.isBefore(c.getFechaInicio())) {
            throw new AlquilerException(
                    "La fecha de devolución no puede ser anterior al inicio del contrato");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new AlquilerException("La fecha de devolución no puede ser posterior a hoy");
        }
        if (kmFinal < c.getKmInicial()) {
            throw new AlquilerException(
                    "El km final no puede ser menor al km inicial (" + c.getKmInicial() + ")");
        }
        c.finalizar(fecha, kmFinal);
        c.getVehiculo().actualizarKilometraje(kmFinal);
        c.getVehiculo().cambiarEstado(EstadoVehiculo.DISPONIBLE);
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

    public Optional<Contrato> buscarPorNumero(int numero) {
        for (Contrato c : agencia.getContratos()) {
            if (c.getNumero() == numero) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
