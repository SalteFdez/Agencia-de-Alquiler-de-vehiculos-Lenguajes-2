package vista;

import java.awt.Dimension;
import javax.swing.JFrame;
import servicio.ServicioFlota;

public class VentanaKiosco extends JFrame {

    public VentanaKiosco(ServicioFlota servicio, CarpetaFotos fotos) {
        super("Kiosco - Alquiler de vehículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new PanelKiosco(servicio, fotos));
        setMinimumSize(new Dimension(900, 600));
        setSize(1280, 800);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
}
