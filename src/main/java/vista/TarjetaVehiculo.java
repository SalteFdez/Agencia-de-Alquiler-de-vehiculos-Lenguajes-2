package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import modelo.flota.Vehiculo;

public class TarjetaVehiculo extends JPanel {

    private static final int ANCHO_FOTO = 320;
    private static final int ALTO_FOTO = 200;

    private final JLabel etiquetaImagen = new JLabel();
    private final JLabel etiquetaMarcaModelo = new JLabel();
    private final JLabel etiquetaPrecio = new JLabel();

    public TarjetaVehiculo(Vehiculo vehiculo, Optional<File> foto) {
        super(new BorderLayout(0, 8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 2, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        etiquetaImagen.setName("imagen");
        etiquetaImagen.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaImagen.setPreferredSize(new Dimension(ANCHO_FOTO, ALTO_FOTO));
        etiquetaImagen.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        etiquetaImagen.setForeground(new Color(110, 110, 110));
        BufferedImage original = null;
        if (foto.isPresent()) {
            try {
                original = ImageIO.read(foto.get());
            } catch (IOException e) {
                original = null;
            }
        }
        if (original != null) {
            double escala = Math.min((double) ANCHO_FOTO / original.getWidth(),
                    (double) ALTO_FOTO / original.getHeight());
            int ancho = (int) Math.round(original.getWidth() * escala);
            int alto = (int) Math.round(original.getHeight() * escala);
            BufferedImage escalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = escalada.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(original, 0, 0, ancho, alto, null);
            g.dispose();
            etiquetaImagen.setIcon(new ImageIcon(escalada));
        } else {
            etiquetaImagen.setText(vehiculo.getTipo());
        }
        add(etiquetaImagen, BorderLayout.NORTH);
        etiquetaMarcaModelo.setName("marcaModelo");
        etiquetaMarcaModelo.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaMarcaModelo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        etiquetaMarcaModelo.setText(vehiculo.getMarca() + " " + vehiculo.getModelo());
        add(etiquetaMarcaModelo, BorderLayout.CENTER);
        etiquetaPrecio.setName("precio");
        etiquetaPrecio.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaPrecio.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        etiquetaPrecio.setText(String.format(Locale.forLanguageTag("es-AR"), "$%,.2f por día",
                vehiculo.getPrecioDiario()));
        add(etiquetaPrecio, BorderLayout.SOUTH);
    }
}
