package modelo.tarifas;

import modelo.flota.Vehiculo;

public class TarifaDiaria implements Tarifa {

    @Override
    public double calcular(Vehiculo v, long dias, int km) {
        return v.getPrecioDiario() * dias;
    }

    @Override
    public String getNombre() {
        return "Diaria";
    }
}
