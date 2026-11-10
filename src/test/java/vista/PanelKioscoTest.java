package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import modelo.flota.Vehiculo;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import modelo.Agencia;
import modelo.flota.Auto;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Moto;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import servicio.ServicioFlota;

class PanelKioscoTest {

    @TempDir
    Path carpeta;

    private static List<TarjetaVehiculo> tarjetas(Container contenedor) {
        List<TarjetaVehiculo> encontradas = new ArrayList<>();
        for (Component hijo : contenedor.getComponents()) {
            if (hijo instanceof TarjetaVehiculo) {
                encontradas.add((TarjetaVehiculo) hijo);
            } else if (hijo instanceof Container) {
                encontradas.addAll(tarjetas((Container) hijo));
            }
        }
        return encontradas;
    }

    private static Auto cronos() {
        return new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000, 10000.0, 4, 5, TipoCaja.MANUAL);
    }

    private static Moto honda() {
        return new Moto("XY987ZW", "Honda", "CB125", "Rojo", 2023, 5000, 8000.0, 125, true);
    }

    @Test
    void elPanelTieneUnaTarjetaPorCadaVehiculoDisponible() {
        Agencia agencia = new Agencia();
        agencia.getVehiculos().add(cronos());
        agencia.getVehiculos().add(honda());

        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia), new CarpetaFotos(carpeta.toFile()), vehiculo -> { });

        assertEquals(2, tarjetas(panel).size());
    }

    @Test
    void unVehiculoAlquiladoNoApareceEnElPanel() {
        Agencia agencia = new Agencia();
        Auto alquilado = cronos();
        alquilado.cambiarEstado(EstadoVehiculo.ALQUILADO);
        agencia.getVehiculos().add(alquilado);
        agencia.getVehiculos().add(honda());

        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia), new CarpetaFotos(carpeta.toFile()), vehiculo -> { });

        List<TarjetaVehiculo> mostradas = tarjetas(panel);
        assertEquals(1, mostradas.size());
        JLabel marcaModelo = null;
        for (Component hijo : mostradas.get(0).getComponents()) {
            if (hijo instanceof JLabel && "marcaModelo".equals(hijo.getName())) {
                marcaModelo = (JLabel) hijo;
            }
        }
        assertEquals("Honda CB125", marcaModelo.getText());
    }

    @Test
    void unVehiculoSinFotoIgualTieneSuTarjetaConSuTipoYElQueTieneFotoMuestraLaImagen()
            throws IOException {
        Agencia agencia = new Agencia();
        agencia.getVehiculos().add(cronos());
        agencia.getVehiculos().add(honda());
        ImageIO.write(new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB), "png",
                carpeta.resolve("AB123CD.png").toFile());

        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia), new CarpetaFotos(carpeta.toFile()), vehiculo -> { });

        List<TarjetaVehiculo> mostradas = tarjetas(panel);
        assertEquals(2, mostradas.size());
        JLabel imagenAuto = etiquetaImagen(mostradas.get(0));
        JLabel imagenMoto = etiquetaImagen(mostradas.get(1));
        assertEquals(320, imagenAuto.getIcon().getIconWidth());
        assertEquals("", imagenAuto.getText());
        assertEquals("Moto", imagenMoto.getText());
    }

    @Test
    void conPocosVehiculosLasTarjetasConservanSuAltoNaturalYQuedanArriba() {
        Agencia agencia = new Agencia();
        agencia.getVehiculos().add(cronos());
        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia), new CarpetaFotos(carpeta.toFile()), vehiculo -> { });

        panel.setSize(1280, 800);
        acomodar(panel);

        TarjetaVehiculo tarjeta = tarjetas(panel).get(0);
        assertEquals(tarjeta.getPreferredSize().height, tarjeta.getHeight());
        assertTrue(SwingUtilities.convertPoint(tarjeta, 0, 0, panel).y < 100);
    }

    private static void acomodar(Container contenedor) {
        contenedor.doLayout();
        for (Component hijo : contenedor.getComponents()) {
            if (hijo instanceof Container) {
                acomodar((Container) hijo);
            }
        }
    }

    private static JLabel etiquetaImagen(TarjetaVehiculo tarjeta) {
        for (Component hijo : tarjeta.getComponents()) {
            if (hijo instanceof JLabel && "imagen".equals(hijo.getName())) {
                return (JLabel) hijo;
            }
        }
        return null;
    }

    @Test
    void tocarUnaTarjetaAvisaConElVehiculoDeEsaTarjeta() {
        Agencia agencia = new Agencia();
        agencia.getVehiculos().add(cronos());
        Moto honda = honda();
        agencia.getVehiculos().add(honda);
        List<Vehiculo> elegidos = new ArrayList<>();
        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia),
                new CarpetaFotos(carpeta.toFile()), elegidos::add);

        TarjetaVehiculo segunda = tarjetas(panel).get(1);
        segunda.dispatchEvent(new MouseEvent(segunda, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, 10, 10, 1, false));

        assertEquals(1, elegidos.size());
        assertSame(honda, elegidos.get(0));
    }

    private static String modeloDe(TarjetaVehiculo tarjeta) {
        for (Component hijo : tarjeta.getComponents()) {
            if (hijo instanceof JLabel && "marcaModelo".equals(hijo.getName())) {
                return ((JLabel) hijo).getText();
            }
        }
        return null;
    }

    private static Auto ka() {
        return new Auto("ZZ999ZZ", "Ford", "Ka", "Negro", 2019, 40000, 9000.0, 4, 5, TipoCaja.MANUAL);
    }

    @Test
    void actualizarSacaLaTarjetaDelVehiculoQueDejoDeEstarDisponibleYDevuelveTrue() {
        Agencia agencia = new Agencia();
        Auto cronos = cronos();
        Moto honda = honda();
        agencia.getVehiculos().add(cronos);
        agencia.getVehiculos().add(honda);
        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia),
                new CarpetaFotos(carpeta.toFile()), vehiculo -> { });

        boolean cambio = panel.actualizar(List.of(honda));

        assertTrue(cambio);
        List<TarjetaVehiculo> mostradas = tarjetas(panel);
        assertEquals(1, mostradas.size());
        assertEquals("Honda CB125", modeloDe(mostradas.get(0)));
    }

    @Test
    void actualizarAgregaLaTarjetaDeUnVehiculoNuevoEnElOrdenDeLaListaYDevuelveTrue() {
        Agencia agencia = new Agencia();
        Auto cronos = cronos();
        Moto honda = honda();
        agencia.getVehiculos().add(cronos);
        agencia.getVehiculos().add(honda);
        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia),
                new CarpetaFotos(carpeta.toFile()), vehiculo -> { });

        boolean cambio = panel.actualizar(List.of(ka(), cronos, honda));

        assertTrue(cambio);
        List<TarjetaVehiculo> mostradas = tarjetas(panel);
        assertEquals(3, mostradas.size());
        assertEquals("Ford Ka", modeloDe(mostradas.get(0)));
        assertEquals("Fiat Cronos", modeloDe(mostradas.get(1)));
        assertEquals("Honda CB125", modeloDe(mostradas.get(2)));
    }

    @Test
    void sinCambiosActualizarDevuelveFalseYLasTarjetasSonLasMismasInstancias() {
        Agencia agencia = new Agencia();
        Auto cronos = cronos();
        Moto honda = honda();
        agencia.getVehiculos().add(cronos);
        agencia.getVehiculos().add(honda);
        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia),
                new CarpetaFotos(carpeta.toFile()), vehiculo -> { });
        List<TarjetaVehiculo> antes = tarjetas(panel);

        boolean cambio = panel.actualizar(List.of(cronos, honda));

        assertFalse(cambio);
        List<TarjetaVehiculo> despues = tarjetas(panel);
        assertEquals(2, despues.size());
        assertSame(antes.get(0), despues.get(0));
        assertSame(antes.get(1), despues.get(1));
    }

    @Test
    void cuandoLaListaCambiaLasTarjetasQueSiguenSeReutilizanYLaDeUnVehiculoQueVuelveTambien() {
        Agencia agencia = new Agencia();
        Auto cronos = cronos();
        Moto honda = honda();
        agencia.getVehiculos().add(cronos);
        agencia.getVehiculos().add(honda);
        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia),
                new CarpetaFotos(carpeta.toFile()), vehiculo -> { });
        List<TarjetaVehiculo> inicial = tarjetas(panel);

        panel.actualizar(List.of(honda, ka()));
        List<TarjetaVehiculo> trasElCambio = tarjetas(panel);
        assertSame(inicial.get(1), trasElCambio.get(0));

        panel.actualizar(List.of(cronos, honda));
        List<TarjetaVehiculo> trasLaVuelta = tarjetas(panel);
        assertSame(inicial.get(0), trasLaVuelta.get(0));
        assertSame(inicial.get(1), trasLaVuelta.get(1));
    }

    @Test
    void tocarLaTarjetaDeUnVehiculoAgregadoPorActualizarAvisaConEseVehiculo() {
        Agencia agencia = new Agencia();
        agencia.getVehiculos().add(cronos());
        List<Vehiculo> elegidos = new ArrayList<>();
        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia),
                new CarpetaFotos(carpeta.toFile()), elegidos::add);
        Vehiculo ka = ka();

        panel.actualizar(List.of(ka));
        TarjetaVehiculo nueva = tarjetas(panel).get(0);
        nueva.dispatchEvent(new MouseEvent(nueva, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, 10, 10, 1, false));

        assertEquals(1, elegidos.size());
        assertSame(ka, elegidos.get(0));
    }
}
