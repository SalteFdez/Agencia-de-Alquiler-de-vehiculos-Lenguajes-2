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

    public boolean estaVigente(LocalDate fecha) {
        return true;
    }
}
