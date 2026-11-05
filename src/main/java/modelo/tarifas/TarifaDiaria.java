package modelo.tarifas;

import java.io.Serializable;
import modelo.flota.Vehiculo;

public class TarifaDiaria implements Tarifa, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public double calcular(Vehiculo v, long dias, int km) {
        return v.getPrecioDiario() * dias;
    }

    @Override
    public String getNombre() {
        return "Diaria";
    }
}
