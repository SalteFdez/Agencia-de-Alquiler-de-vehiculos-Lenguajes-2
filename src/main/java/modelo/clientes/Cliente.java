package modelo.clientes;

import java.time.LocalDate;
import modelo.flota.Vehiculo;
import java.time.Period;

public class Cliente {

    public static final int EDAD_MINIMA = 18;

    private int id;
    private final String nombre;
    private final String apellido;
    private final String dni;
    private final LocalDate fechaNacimiento;
    private final String telefono;
    private final LicenciaConducir licencia;

    public Cliente(String nombre, String apellido, String dni, LocalDate fechaNacimiento,
                   String telefono, LicenciaConducir licencia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.licencia = licencia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public LicenciaConducir getLicencia() {
        return licencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (DNI " + dni + ")";
    }

    public boolean puedeConducir(Vehiculo v, LocalDate hasta) {
        return getEdad() >= EDAD_MINIMA
                && licencia.habilitaPara(v.getLicenciaRequerida(), hasta);
    }

    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
