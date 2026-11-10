package servicio;

import excepciones.AlquilerException;
import excepciones.VehiculoNoDisponibleException;
import java.time.LocalDate;
import java.util.Optional;
import modelo.Agencia;
import modelo.alquiler.Contrato;
import modelo.clientes.Cliente;
import modelo.flota.Vehiculo;
import modelo.tarifas.TarifaDiaria;
import persistencia.GestorPersistencia;

public class ServicioOperario {

    private final GestorPersistencia gestor;
    private final Agencia agencia;

    public ServicioOperario(GestorPersistencia gestor, Agencia agencia) {
        this.gestor = gestor;
        this.agencia = agencia;
    }

    public void recargar() throws AlquilerException {
        if (!gestor.existeArchivo()) {
            return;
        }
        agencia.reemplazarCon(gestor.cargar());
    }

    public void registrarVehiculo(Vehiculo v) throws AlquilerException {
        aplicar(fresca -> {
            new ServicioFlota(fresca).registrarVehiculo(v);
            return null;
        });
    }

    public void eliminarVehiculo(String patente) throws AlquilerException {
        aplicar(fresca -> {
            ServicioFlota flota = new ServicioFlota(fresca);
            Optional<Vehiculo> vehiculo = flota.buscarPorPatente(patente);
            if (vehiculo.isPresent()) {
                flota.eliminarVehiculo(vehiculo.get());
            }
            return null;
        });
    }

    public void registrarCliente(Cliente c) throws AlquilerException {
        aplicar(fresca -> {
            new ServicioClientes(fresca).registrarCliente(c);
            return null;
        });
    }

    public void eliminarCliente(String dni) throws AlquilerException {
        aplicar(fresca -> {
            ServicioClientes clientes = new ServicioClientes(fresca);
            Optional<Cliente> cliente = clientes.buscarPorDni(dni);
            if (cliente.isPresent()) {
                clientes.eliminarCliente(cliente.get());
            }
            return null;
        });
    }

    public Contrato crearContrato(String dni, String patente, LocalDate fin) throws AlquilerException {
        return aplicar(fresca -> {
            Cliente cliente = new ServicioClientes(fresca).buscarPorDni(dni).orElseThrow(
                    () -> new AlquilerException("No hay ningún cliente registrado con el DNI " + dni));
            Vehiculo vehiculo = new ServicioFlota(fresca).buscarPorPatente(patente)
                    .orElseThrow(() -> new VehiculoNoDisponibleException(patente));
            return new ServicioAlquiler(fresca).crearContrato(cliente, vehiculo, new TarifaDiaria(), fin);
        });
    }

    public Contrato registrarDevolucion(int numero, LocalDate fecha, int kmFinal)
            throws AlquilerException {
        return aplicar(fresca -> {
            ServicioAlquiler alquiler = new ServicioAlquiler(fresca);
            Contrato contrato = alquiler.buscarPorNumero(numero).get();
            alquiler.registrarDevolucion(contrato, fecha, kmFinal);
            return contrato;
        });
    }

    private <T> T aplicar(Cambio<T> cambio) throws AlquilerException {
        Agencia fresca = gestor.existeArchivo() ? gestor.cargar() : new Agencia();
        T resultado = cambio.aplicarSobre(fresca);
        gestor.guardar(fresca);
        agencia.reemplazarCon(fresca);
        return resultado;
    }

    private interface Cambio<T> {
        T aplicarSobre(Agencia fresca) throws AlquilerException;
    }
}
