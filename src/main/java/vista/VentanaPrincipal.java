package vista;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import servicio.ServicioAlquiler;
import servicio.ServicioClientes;
import servicio.ServicioFlota;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(ServicioFlota servicioFlota, ServicioClientes servicioClientes,
                            ServicioAlquiler servicioAlquiler) {
        super("Agencia de alquiler de vehículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        PanelFlota panelFlota = new PanelFlota(this, servicioFlota);
        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Flota", panelFlota);
        pestanias.addTab("Clientes", new PanelClientes(this, servicioClientes));
        pestanias.addTab("Contratos", new PanelContratos(this, servicioAlquiler,
                servicioClientes, servicioFlota, panelFlota::refrescar));
        add(pestanias);

        setSize(900, 500);
        setLocationRelativeTo(null);
    }
}
