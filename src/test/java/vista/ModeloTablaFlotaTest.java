package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;

import excepciones.AlquilerException;
import java.util.concurrent.atomic.AtomicInteger;
import modelo.Agencia;
import modelo.flota.Auto;
import modelo.flota.Camioneta;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Moto;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;
import servicio.ServicioFlota;

class ModeloTablaFlotaTest {

    @Test
    void laFilaDeUnAutoMuestraSusDatosYElPrecioDiarioYaMultiplicado() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        servicio.registrarVehiculo(new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022,
                15000, 10000.0, 4, 5, TipoCaja.AUTOMATICA));

        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);

        assertEquals(1, modelo.getRowCount());
        assertEquals(8, modelo.getColumnCount());
        assertEquals("Patente", modelo.getColumnName(0));
        assertEquals("Tipo", modelo.getColumnName(1));
        assertEquals("Marca", modelo.getColumnName(2));
        assertEquals("Modelo", modelo.getColumnName(3));
        assertEquals("Año", modelo.getColumnName(4));
        assertEquals("Km", modelo.getColumnName(5));
        assertEquals("Estado", modelo.getColumnName(6));
        assertEquals("Precio diario", modelo.getColumnName(7));
        assertEquals("AB123CD", modelo.getValueAt(0, 0));
        assertEquals("Auto", modelo.getValueAt(0, 1));
        assertEquals("Fiat", modelo.getValueAt(0, 2));
        assertEquals("Cronos", modelo.getValueAt(0, 3));
        assertEquals(2022, modelo.getValueAt(0, 4));
        assertEquals(15000, modelo.getValueAt(0, 5));
        assertEquals("DISPONIBLE", modelo.getValueAt(0, 6));
        assertEquals(11500.0, (double) modelo.getValueAt(0, 7), 0.001);
    }

    @Test
    void laFilaDeUnaMotoMuestraTipoMotoEstadoDisponibleYPrecioPorCeroSeis() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        servicio.registrarVehiculo(new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023,
                5000, 10000.0, 125, true));

        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);

        assertEquals(1, modelo.getRowCount());
        assertEquals("A123BCD", modelo.getValueAt(0, 0));
        assertEquals("Moto", modelo.getValueAt(0, 1));
        assertEquals("Honda", modelo.getValueAt(0, 2));
        assertEquals("CB125", modelo.getValueAt(0, 3));
        assertEquals(2023, modelo.getValueAt(0, 4));
        assertEquals(5000, modelo.getValueAt(0, 5));
        assertEquals("DISPONIBLE", modelo.getValueAt(0, 6));
        assertEquals(6000.0, (double) modelo.getValueAt(0, 7), 0.001);
    }

    @Test
    void conUnFiltroElegidoLaTablaMuestraSoloLasFilasQueLoCumplen() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        servicio.registrarVehiculo(new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022,
                15000, 10000.0, 4, 5, TipoCaja.MANUAL));
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023,
                5000, 10000.0, 125, true);
        servicio.registrarVehiculo(moto);
        servicio.registrarVehiculo(new Camioneta("AC123DE", "Toyota", "Hilux", "Gris", 2022,
                20000, 10000.0, 1000.0, false));
        moto.cambiarEstado(EstadoVehiculo.ALQUILADO);
        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);

        modelo.setFiltro("Moto", null);

        assertEquals(1, modelo.getRowCount());
        assertEquals("A123BCD", modelo.getValueAt(0, 0));

        modelo.setFiltro(null, EstadoVehiculo.DISPONIBLE);

        assertEquals(2, modelo.getRowCount());
        assertEquals("AB123CD", modelo.getValueAt(0, 0));
        assertEquals("AC123DE", modelo.getValueAt(1, 0));
    }

    @Test
    void volverASinFiltroMuestraLaFlotaCompleta() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        servicio.registrarVehiculo(new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022,
                15000, 10000.0, 4, 5, TipoCaja.MANUAL));
        servicio.registrarVehiculo(new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023,
                5000, 10000.0, 125, true));
        servicio.registrarVehiculo(new Camioneta("AC123DE", "Toyota", "Hilux", "Gris", 2022,
                20000, 10000.0, 1000.0, false));
        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);
        modelo.setFiltro("Moto", EstadoVehiculo.DISPONIBLE);

        modelo.setFiltro(null, null);

        assertEquals(3, modelo.getRowCount());
    }

    @Test
    void cambiarElFiltroAvisaALaTablaQueLosDatosCambiaron() {
        ModeloTablaFlota modelo = new ModeloTablaFlota(new ServicioFlota(new Agencia()));
        AtomicInteger avisos = new AtomicInteger();
        modelo.addTableModelListener(e -> avisos.incrementAndGet());

        modelo.setFiltro("Moto", null);

        assertEquals(1, avisos.get());
    }

    @Test
    void registrarUnVehiculoQueNoCumpleElFiltroNoCambiaLaTabla() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        servicio.registrarVehiculo(new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022,
                15000, 10000.0, 4, 5, TipoCaja.MANUAL));
        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);
        modelo.setFiltro("Auto", null);

        servicio.registrarVehiculo(new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023,
                5000, 10000.0, 125, true));

        assertEquals(1, modelo.getRowCount());
        assertEquals("AB123CD", modelo.getValueAt(0, 0));
    }

    @Test
    void registrarUnVehiculoQueCumpleElFiltroApareceEnLaTablaFiltrada() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        servicio.registrarVehiculo(new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022,
                15000, 10000.0, 4, 5, TipoCaja.MANUAL));
        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);
        modelo.setFiltro("Auto", null);

        servicio.registrarVehiculo(new Auto("AC456EF", "Fiat", "Argo", "Negro", 2021,
                30000, 10000.0, 4, 5, TipoCaja.MANUAL));

        assertEquals(2, modelo.getRowCount());
        assertEquals("AC456EF", modelo.getValueAt(1, 0));
    }

    @Test
    void getVehiculoEnDevuelveElVehiculoDeEsaFilaSinFiltro() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022,
                15000, 10000.0, 4, 5, TipoCaja.MANUAL);
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023,
                5000, 10000.0, 125, true);
        servicio.registrarVehiculo(auto);
        servicio.registrarVehiculo(moto);
        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);

        assertEquals(auto, modelo.getVehiculoEn(0));
        assertEquals(moto, modelo.getVehiculoEn(1));
    }

    @Test
    void getVehiculoEnRespetaElFiltroElegido() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        servicio.registrarVehiculo(new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022,
                15000, 10000.0, 4, 5, TipoCaja.MANUAL));
        Moto moto = new Moto("A123BCD", "Honda", "CB125", "Rojo", 2023,
                5000, 10000.0, 125, true);
        servicio.registrarVehiculo(moto);
        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);

        modelo.setFiltro("Moto", null);

        assertEquals(moto, modelo.getVehiculoEn(0));
    }
}
