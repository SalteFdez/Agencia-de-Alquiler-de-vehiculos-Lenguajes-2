package servicio;

import excepciones.AlquilerException;
import excepciones.VehiculoNoDisponibleException;
import java.time.LocalDate;
import modelo.Agencia;
import modelo.alquiler.Contrato;
import modelo.clientes.Cliente;
import modelo.flota.Vehiculo;
import modelo.tarifas.TarifaDiaria;
import persistencia.GestorPersistencia;

public class ServicioKiosco {

    private final GestorPersistencia gestor;

    public ServicioKiosco(GestorPersistencia gestor) {
        this.gestor = gestor;
    }

    public Contrato alquilar(String dni, String patente, LocalDate fin) throws AlquilerException {
        if (dni.isBlank()) {
            throw new AlquilerException("Hay que escribir el DNI");
        }
        Agencia agencia = gestor.cargar();
        Cliente cliente = new ServicioClientes(agencia).buscarPorDni(dni).orElseThrow(
                () -> new AlquilerException("No hay ningún cliente registrado con el DNI " + dni));
        Vehiculo vehiculo = new ServicioFlota(agencia).buscarPorPatente(patente)
                .orElseThrow(() -> new VehiculoNoDisponibleException(patente));
        Contrato contrato = new ServicioAlquiler(agencia)
                .crearContrato(cliente, vehiculo, new TarifaDiaria(), fin);
        gestor.guardar(agencia);
        return contrato;
    }
}
