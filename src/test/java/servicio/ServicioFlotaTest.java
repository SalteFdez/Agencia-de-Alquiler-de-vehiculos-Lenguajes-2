package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import modelo.Agencia;
import modelo.flota.Auto;
import modelo.flota.EstadoVehiculo;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;

class ServicioFlotaTest {

    @Test
    void registrarUnAutoConDatosValidosLoAgregaALaLista() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);

        servicio.registrarVehiculo(auto);

        assertEquals(List.of(auto), servicio.listarVehiculos());
    }

    @Test
    void elAutoRegistradoQuedaEnEstadoDisponible() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);

        servicio.registrarVehiculo(auto);

        assertEquals(EstadoVehiculo.DISPONIBLE, servicio.listarVehiculos().get(0).getEstado());
    }
}
