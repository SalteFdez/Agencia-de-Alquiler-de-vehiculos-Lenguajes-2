package vista;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.Timer;
import excepciones.AlquilerException;
import modelo.Agencia;
import persistencia.GestorPersistencia;
import servicio.ServicioAlquiler;
import servicio.ServicioFlota;
import servicio.ServicioKiosco;

public class VentanaKiosco extends JFrame {

    private final ServicioKiosco servicioKiosco;
    private final PanelKiosco panel;
    private final Timer temporizador;

    public VentanaKiosco(GestorPersistencia gestor, CarpetaFotos fotos) {
        super("Kiosco - Alquiler de vehículos");
        this.servicioKiosco = new ServicioKiosco(gestor);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Agencia agencia = Main.cargarAgencia(gestor);
        ServicioAlquiler servicioAlquiler = new ServicioAlquiler(agencia);
        panel = new PanelKiosco(new ServicioFlota(agencia), fotos,
                vehiculo -> new FormularioAlquiler(this, vehiculo, servicioAlquiler,
                        servicioKiosco, this::actualizar).setVisible(true));
        add(panel);

        temporizador = new Timer(Recarga.INTERVALO_MS, e -> actualizar());
        temporizador.start();

        setMinimumSize(new Dimension(900, 600));
        setSize(1280, 800);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void actualizar() {
        try {
            panel.actualizar(servicioKiosco.disponibles());
        } catch (AlquilerException e) {
            // se conserva lo que se ve y se reintenta en el próximo ciclo
        }
    }
}
