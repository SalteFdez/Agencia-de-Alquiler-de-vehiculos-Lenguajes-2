package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Vehiculo;
import servicio.ServicioFlota;

public class PanelKiosco extends JPanel {

    private final ServicioFlota servicio;
    private final CarpetaFotos fotos;

    public PanelKiosco(ServicioFlota servicio, CarpetaFotos fotos, Consumer<Vehiculo> alElegir) {
        super(new BorderLayout());
        this.servicio = servicio;
        this.fotos = fotos;

        JPanel grilla = new JPanel(new GridLayout(0, 3, 20, 20));
        grilla.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        for (Vehiculo vehiculo : servicio.filtrar(null, EstadoVehiculo.DISPONIBLE)) {
            TarjetaVehiculo tarjeta = new TarjetaVehiculo(vehiculo, fotos.buscarFoto(vehiculo.getPatente()));
            tarjeta.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    alElegir.accept(vehiculo);
                }
            });
            grilla.add(tarjeta);
        }
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(grilla, BorderLayout.NORTH);
        add(new JScrollPane(arriba), BorderLayout.CENTER);
    }
}
