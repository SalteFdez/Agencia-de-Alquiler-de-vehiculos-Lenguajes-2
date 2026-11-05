package modelo.tarifas;

import modelo.flota.Vehiculo;

public interface Tarifa {

    double calcular(Vehiculo v, long dias, int km);

    String getNombre();
}
