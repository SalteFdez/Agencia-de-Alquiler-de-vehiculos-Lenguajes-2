package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import excepciones.AlquilerException;
import modelo.Agencia;
import modelo.flota.Auto;
import modelo.flota.Camioneta;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Moto;
import modelo.flota.TipoCaja;
import modelo.flota.Vehiculo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void unaCantidadDePuertasCeroONegativaLanzaExcepcionYNoRegistra(int puertas) {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, puertas, 5, TipoCaja.MANUAL);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(auto));

        assertTrue(e.getMessage().contains("puertas"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void unaCapacidadDePasajerosCeroONegativaLanzaExcepcionYNoRegistra(int pasajeros) {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, pasajeros, TipoCaja.MANUAL);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(auto));

        assertTrue(e.getMessage().contains("pasajeros"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @Test
    void registrarUnaMotoConDatosValidosLaAgregaALaListaEnEstadoDisponible() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);

        servicio.registrarVehiculo(moto);

        assertEquals(List.of(moto), servicio.listarVehiculos());
        assertEquals(EstadoVehiculo.DISPONIBLE, servicio.listarVehiculos().get(0).getEstado());
    }

    @Test
    void registrarUnaMotoConLaPatenteDeUnAutoLanzaExcepcionYLaListaNoCambia() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Moto moto = new Moto("AB123CD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);
        servicio.registrarVehiculo(auto);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(moto));

        assertTrue(e.getMessage().contains("ya está registrada"));
        assertEquals(List.of(auto), servicio.listarVehiculos());
    }

    @Test
    void unaCilindradaIgualACeroLanzaExcepcionYNoRegistra() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Moto sinCilindrada = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 0, true);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(sinCilindrada));

        assertTrue(e.getMessage().contains("cilindrada debe ser mayor a 0"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @Test
    void unaCilindradaNegativaLanzaExcepcionYNoRegistra() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Moto cilindradaNegativa = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, -125, true);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(cilindradaNegativa));

        assertTrue(e.getMessage().contains("cilindrada debe ser mayor a 0"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @Test
    void registrarUnaCamionetaConDatosValidosLaAgregaALaListaDisponibleYConSuPrecioDiario() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Camioneta camioneta = new Camioneta("AC123DE", "Toyota", "Hilux", "Gris", 2022, 20000,
                10000.0, 1000.0, true);

        servicio.registrarVehiculo(camioneta);

        Vehiculo registrada = servicio.listarVehiculos().get(0);
        assertEquals(List.of(camioneta), servicio.listarVehiculos());
        assertEquals("Camioneta", registrada.getTipo());
        assertEquals(EstadoVehiculo.DISPONIBLE, registrada.getEstado());
        assertEquals(16000.0, registrada.getPrecioDiario(), 0.001);
    }

    @Test
    void registrarUnaCamionetaConLaPatenteDeUnAutoOUnaMotoLanzaExcepcionYLaListaNoCambia() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);
        servicio.registrarVehiculo(auto);
        servicio.registrarVehiculo(moto);
        Camioneta conPatenteDeAuto = new Camioneta("AB123CD", "Toyota", "Hilux", "Gris", 2022,
                20000, 10000.0, 1000.0, false);
        Camioneta conPatenteDeMoto = new Camioneta("A123BCD", "Toyota", "Hilux", "Gris", 2022,
                20000, 10000.0, 1000.0, false);

        AlquilerException errorAuto = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(conPatenteDeAuto));
        AlquilerException errorMoto = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(conPatenteDeMoto));

        assertTrue(errorAuto.getMessage().contains("ya está registrada"));
        assertTrue(errorMoto.getMessage().contains("ya está registrada"));
        assertEquals(List.of(auto, moto), servicio.listarVehiculos());
    }

    @Test
    void unaCapacidadDeCargaIgualACeroLanzaExcepcionYLaCamionetaNoSeRegistra() {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Camioneta sinCapacidad = new Camioneta("AC123DE", "Toyota", "Hilux", "Gris", 2022, 20000,
                10000.0, 0.0, false);

        AlquilerException e = assertThrows(AlquilerException.class,
                () -> servicio.registrarVehiculo(sinCapacidad));

        assertTrue(e.getMessage().contains("capacidad de carga debe ser mayor a 0"));
        assertTrue(servicio.listarVehiculos().isEmpty());
    }

    @Test
    void filtrarSinTipoNiEstadoDevuelveTodaLaFlota() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);
        Camioneta camioneta = new Camioneta("AC123DE", "Toyota", "Hilux", "Gris", 2022, 20000,
                10000.0, 1000.0, false);
        servicio.registrarVehiculo(auto);
        servicio.registrarVehiculo(moto);
        servicio.registrarVehiculo(camioneta);

        assertEquals(List.of(auto, moto, camioneta), servicio.filtrar(null, null));
    }

    @Test
    void filtrarPorTipoDevuelveSoloLosVehiculosDeEseTipo() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);
        Camioneta camioneta = new Camioneta("AC123DE", "Toyota", "Hilux", "Gris", 2022, 20000,
                10000.0, 1000.0, false);
        servicio.registrarVehiculo(auto);
        servicio.registrarVehiculo(moto);
        servicio.registrarVehiculo(camioneta);

        assertEquals(List.of(auto), servicio.filtrar("Auto", null));
        assertEquals(List.of(moto), servicio.filtrar("Moto", null));
        assertEquals(List.of(camioneta), servicio.filtrar("Camioneta", null));
    }

    @Test
    void filtrarPorEstadoDevuelveSoloLosVehiculosConEseEstado() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023, 5000,
                10000.0, 125, true);
        servicio.registrarVehiculo(auto);
        servicio.registrarVehiculo(moto);
        moto.cambiarEstado(EstadoVehiculo.ALQUILADO);

        assertEquals(List.of(auto), servicio.filtrar(null, EstadoVehiculo.DISPONIBLE));
        assertEquals(List.of(moto), servicio.filtrar(null, EstadoVehiculo.ALQUILADO));
    }
}
