package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final Consumer<Vehiculo> alElegir;
    private final JPanel grilla = new JPanel(new GridLayout(0, 3, 20, 20));
    private final Map<String, TarjetaVehiculo> tarjetas = new HashMap<>();
    private List<String> patentesMostradas = new ArrayList<>();

    public PanelKiosco(ServicioFlota servicio, CarpetaFotos fotos, Consumer<Vehiculo> alElegir) {
        super(new BorderLayout());
        this.servicio = servicio;
        this.fotos = fotos;
        this.alElegir = alElegir;

        grilla.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        actualizar(servicio.filtrar(null, EstadoVehiculo.DISPONIBLE));
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(grilla, BorderLayout.NORTH);
        add(new JScrollPane(arriba), BorderLayout.CENTER);
    }

    public boolean actualizar(List<Vehiculo> disponibles) {
        List<String> patentes = new ArrayList<>();
        for (Vehiculo vehiculo : disponibles) {
            patentes.add(vehiculo.getPatente());
        }
        if (patentes.equals(patentesMostradas)) {
            return false;
        }
        patentesMostradas = patentes;
        grilla.removeAll();
        for (Vehiculo vehiculo : disponibles) {
            grilla.add(tarjetas.computeIfAbsent(vehiculo.getPatente(), patente -> crearTarjeta(vehiculo)));
        }
        grilla.revalidate();
        grilla.repaint();
        return true;
    }

    private TarjetaVehiculo crearTarjeta(Vehiculo vehiculo) {
        TarjetaVehiculo tarjeta = new TarjetaVehiculo(vehiculo, fotos.buscarFoto(vehiculo.getPatente()));
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                alElegir.accept(vehiculo);
            }
        });
        return tarjeta;
    }
}
