package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import excepciones.AlquilerException;
import modelo.Agencia;
import modelo.flota.Auto;
import modelo.flota.EstadoVehiculo;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;

class ServicioFlotaTest {

    @Test
    void registrarUnAutoConDatosValidosLoAgregaALaLista() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);

        servicio.registrarVehiculo(auto);

        assertEquals(List.of(auto), servicio.listarVehiculos());
    }

    @Test
    void elAutoRegistradoQuedaEnEstadoDisponible() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);

        servicio.registrarVehiculo(auto);

        assertEquals(EstadoVehiculo.DISPONIBLE, servicio.listarVehiculos().get(0).getEstado());
    }

    @Test
    void registrarUnaPatenteYaExistenteLanzaExcepcionYLaListaNoCambia() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto original = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Auto repetido = new Auto("AB123CD", "Renault", "Logan", "Gris", 2020, 40000,
                9000.0, 4, 5, TipoCaja.MANUAL);
        servicio.registrarVehiculo(original);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(repetido));

        assertTrue(e.getMessage().contains("ya está registrada"));
        assertEquals(List.of(original), servicio.listarVehiculos());
    }

    @Test
    void registrarUnaPatenteRepetidaEnDistintoCasoLanzaExcepcionYLaListaNoCambia() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto original = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Auto repetido = new Auto("ab123cd", "Renault", "Logan", "Gris", 2020, 40000,
                9000.0, 4, 5, TipoCaja.MANUAL);
        servicio.registrarVehiculo(original);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(repetido));

        assertTrue(e.getMessage().contains("ya está registrada"));
        assertEquals(List.of(original), servicio.listarVehiculos());
    }

    @Test
    void unaPatenteVaciaLanzaExcepcionDeValidacionYNoRegistra() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto sinPatente = new Auto("", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(sinPatente));

        assertTrue(e.getMessage().contains("patente"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @Test
    void unaPatenteConSoloEspaciosCuentaComoVaciaYNoRegistra() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto patenteEnBlanco = new Auto("   ", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(patenteEnBlanco));

        assertTrue(e.getMessage().contains("patente"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @Test
    void unKilometrajeNegativoLanzaExcepcionDeValidacionYNoRegistra() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto kmNegativo = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, -1,
                10000.0, 4, 5, TipoCaja.MANUAL);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(kmNegativo));

        assertTrue(e.getMessage().contains("kilometraje"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @Test
    void unPrecioBaseNegativoLanzaExcepcionDeValidacionYNoRegistra() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto precioNegativo = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                -1.0, 4, 5, TipoCaja.MANUAL);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(precioNegativo));

        assertTrue(e.getMessage().contains("precio"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @Test
    void unKilometrajeCeroEsValido() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto kmCero = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 0,
                10000.0, 4, 5, TipoCaja.MANUAL);

        servicio.registrarVehiculo(kmCero);

        assertEquals(List.of(kmCero), servicio.listarVehiculos());
    }

    @Test
    void unPrecioBaseCeroEsValido() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto precioCero = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                0.0, 4, 5, TipoCaja.MANUAL);

        servicio.registrarVehiculo(precioCero);

        assertEquals(List.of(precioCero), servicio.listarVehiculos());
    }
}
