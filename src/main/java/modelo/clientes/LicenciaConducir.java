package modelo.clientes;

import java.time.LocalDate;
import java.util.Set;

public class LicenciaConducir {

    private final String numero;
    private final Set<CategoriaLicencia> categorias;
    private final LocalDate fechaVencimiento;

    public LicenciaConducir(String numero, Set<CategoriaLicencia> categorias,
                            LocalDate fechaVencimiento) {
        this.numero = numero;
        this.categorias = categorias;
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNumero() {
        return numero;
    }

    public Set<CategoriaLicencia> getCategorias() {
        return categorias;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public boolean estaVigente(LocalDate fecha) {
        return !fecha.isAfter(fechaVencimiento);
    }

    public boolean habilitaPara(CategoriaLicencia c, LocalDate fecha) {
        return estaVigente(fecha) && categorias.contains(c);
    }
}
