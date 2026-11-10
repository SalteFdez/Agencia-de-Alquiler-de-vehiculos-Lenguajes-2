package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
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

        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia), new CarpetaFotos(carpeta.toFile()));

        assertEquals(2, tarjetas(panel).size());
    }

    @Test
    void unVehiculoAlquiladoNoApareceEnElPanel() {
        Agencia agencia = new Agencia();
        Auto alquilado = cronos();
        alquilado.cambiarEstado(EstadoVehiculo.ALQUILADO);
        agencia.getVehiculos().add(alquilado);
        agencia.getVehiculos().add(honda());

        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia), new CarpetaFotos(carpeta.toFile()));

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

        PanelKiosco panel = new PanelKiosco(new ServicioFlota(agencia), new CarpetaFotos(carpeta.toFile()));

        List<TarjetaVehiculo> mostradas = tarjetas(panel);
        assertEquals(2, mostradas.size());
        JLabel imagenAuto = etiquetaImagen(mostradas.get(0));
        JLabel imagenMoto = etiquetaImagen(mostradas.get(1));
        assertEquals(320, imagenAuto.getIcon().getIconWidth());
        assertEquals("", imagenAuto.getText());
        assertEquals("Moto", imagenMoto.getText());
    }

    private static JLabel etiquetaImagen(TarjetaVehiculo tarjeta) {
        for (Component hijo : tarjeta.getComponents()) {
            if (hijo instanceof JLabel && "imagen".equals(hijo.getName())) {
                return (JLabel) hijo;
            }
        }
        return null;
    }
}
