package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.util.Optional;
import javax.swing.JLabel;
import modelo.flota.Auto;
import modelo.flota.Camioneta;
import modelo.flota.Moto;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TarjetaVehiculoTest {

    @TempDir
    Path carpeta;

    private static JLabel etiqueta(Container contenedor, String nombre) {
        for (Component hijo : contenedor.getComponents()) {
            if (hijo instanceof JLabel && nombre.equals(hijo.getName())) {
                return (JLabel) hijo;
            }
            if (hijo instanceof Container) {
                JLabel encontrada = etiqueta((Container) hijo, nombre);
                if (encontrada != null) {
                    return encontrada;
                }
            }
        }
        return null;
    }

    private static Auto cronos(TipoCaja caja) {
        return new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000, 10000.0, 4, 5, caja);
    }

    @Test
    void laTarjetaMuestraMarcaYModeloDelVehiculo() {
        TarjetaVehiculo tarjeta = new TarjetaVehiculo(cronos(TipoCaja.MANUAL), Optional.empty());

        assertEquals("Fiat Cronos", etiqueta(tarjeta, "marcaModelo").getText());
    }

    @Test
    void laTarjetaMuestraElPrecioPorDiaDelVehiculo() {
        TarjetaVehiculo tarjeta = new TarjetaVehiculo(cronos(TipoCaja.AUTOMATICA), Optional.empty());

        assertEquals("$11.500,00 por día", etiqueta(tarjeta, "precio").getText());
    }

    @Test
    void conFotoLaTarjetaMuestraLaImagenEscaladaYNoElTipo() throws IOException {
        File foto = carpeta.resolve("AB123CD.png").toFile();
        ImageIO.write(new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB), "png", foto);

        TarjetaVehiculo tarjeta = new TarjetaVehiculo(cronos(TipoCaja.MANUAL), Optional.of(foto));

        JLabel imagen = etiqueta(tarjeta, "imagen");
        assertNotNull(imagen.getIcon());
        assertEquals(320, imagen.getIcon().getIconWidth());
        assertEquals(200, imagen.getIcon().getIconHeight());
        assertEquals("", imagen.getText());
    }

    @Test
    void sinFotoLaTarjetaMuestraElTipoDelVehiculoEnLugarDeLaImagen() {
        Moto moto = new Moto("XY987ZW", "Honda", "CB125", "Rojo", 2023, 5000, 8000.0, 125, true);
        Camioneta camioneta = new Camioneta("CA555AA", "Toyota", "Hilux", "Gris", 2021, 42000,
                20000.0, 1000.0, true);

        JLabel deAuto = etiqueta(new TarjetaVehiculo(cronos(TipoCaja.MANUAL), Optional.empty()), "imagen");
        JLabel deMoto = etiqueta(new TarjetaVehiculo(moto, Optional.empty()), "imagen");
        JLabel deCamioneta = etiqueta(new TarjetaVehiculo(camioneta, Optional.empty()), "imagen");

        assertEquals("Auto", deAuto.getText());
        assertEquals("Moto", deMoto.getText());
        assertEquals("Camioneta", deCamioneta.getText());
        assertNull(deAuto.getIcon());
    }

    @Test
    void unaFotoIlegibleSeTrataComoSinFotoYMuestraElTipo() throws IOException {
        File foto = carpeta.resolve("AB123CD.png").toFile();
        Files.write(foto.toPath(), "esto no es una imagen".getBytes());

        TarjetaVehiculo tarjeta = new TarjetaVehiculo(cronos(TipoCaja.MANUAL), Optional.of(foto));

        JLabel imagen = etiqueta(tarjeta, "imagen");
        assertNull(imagen.getIcon());
        assertEquals("Auto", imagen.getText());
    }
}
