package vista;

import excepciones.AlquilerException;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import servicio.ServicioAlquiler;
import servicio.ServicioClientes;
import servicio.ServicioFlota;
import servicio.ServicioOperario;

public class VentanaPrincipal extends JFrame {

    private static final int INTERVALO_MS = 3000;

    private final Timer temporizador;

    public VentanaPrincipal(ServicioFlota servicioFlota, ServicioClientes servicioClientes,
                            ServicioAlquiler servicioAlquiler, ServicioOperario servicioOperario) {
        super("Agencia de alquiler de vehículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        PanelFlota panelFlota = new PanelFlota(this, servicioFlota, servicioOperario);
        PanelClientes panelClientes = new PanelClientes(this, servicioClientes, servicioOperario);
        PanelContratos panelContratos = new PanelContratos(this, servicioAlquiler,
                servicioClientes, servicioFlota, servicioOperario, panelFlota::refrescar);
        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Flota", panelFlota);
        pestanias.addTab("Clientes", panelClientes);
        pestanias.addTab("Contratos", panelContratos);
        add(pestanias);

        temporizador = new Timer(INTERVALO_MS, e -> {
            try {
                servicioOperario.recargar();
            } catch (AlquilerException ex) {
                return;
            }
            panelFlota.refrescar();
            panelClientes.refrescar();
            panelContratos.refrescar();
        });
        temporizador.start();

        setSize(900, 500);
        setLocationRelativeTo(null);
    }
}
