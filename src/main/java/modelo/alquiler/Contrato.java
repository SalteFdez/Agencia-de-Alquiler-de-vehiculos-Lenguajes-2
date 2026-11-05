package modelo.alquiler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import modelo.clientes.Cliente;
import modelo.flota.Vehiculo;
import modelo.tarifas.Tarifa;

public class Contrato {

    private int numero;
    private final Cliente cliente;
    private final Vehiculo vehiculo;
    private final Tarifa tarifa;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFinPrevista;
    private LocalDate fechaDevolucion;
    private final int kmInicial;
    private int kmFinal;
    private EstadoContrato estado = EstadoContrato.ACTIVO;

    public Contrato(Cliente cliente, Vehiculo vehiculo, Tarifa tarifa,
                    LocalDate fechaInicio, LocalDate fechaFinPrevista) {
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.tarifa = tarifa;
        this.fechaInicio = fechaInicio;
        this.fechaFinPrevista = fechaFinPrevista;
        this.kmInicial = vehiculo.getKilometraje();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFinPrevista() {
        return fechaFinPrevista;
    }

    public int getKmInicial() {
        return kmInicial;
    }

    public long getDias() {
        long diasPrevistos = ChronoUnit.DAYS.between(fechaInicio, fechaFinPrevista);
        if (estado == EstadoContrato.FINALIZADO) {
            return Math.max(diasPrevistos, ChronoUnit.DAYS.between(fechaInicio, fechaDevolucion));
        }
        return diasPrevistos;
    }

    public int getKmRecorridos() {
        if (estado == EstadoContrato.FINALIZADO) {
            return kmFinal - kmInicial;
        }
        return 0;
    }

    public double calcularTotal() {
        return tarifa.calcular(vehiculo, getDias(), 0);
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public void finalizar(LocalDate fecha, int kmFinal) {
        this.fechaDevolucion = fecha;
        this.kmFinal = kmFinal;
        this.estado = EstadoContrato.FINALIZADO;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public int getKmFinal() {
        return kmFinal;
    }
}
