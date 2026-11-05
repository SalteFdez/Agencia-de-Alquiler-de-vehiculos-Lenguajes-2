package modelo.clientes;

import java.time.LocalDate;

public class Cliente {

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

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
