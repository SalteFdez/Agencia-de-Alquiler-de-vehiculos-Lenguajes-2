package vista;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import servicio.ServicioClientes;
import servicio.ServicioFlota;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(ServicioFlota servicioFlota, ServicioClientes servicioClientes) {
        super("Agencia de alquiler de vehículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Flota", new PanelFlota(this, servicioFlota));
        pestanias.addTab("Clientes", new PanelClientes(this, servicioClientes));
        add(pestanias);

        setSize(900, 500);
        setLocationRelativeTo(null);
    }
}
