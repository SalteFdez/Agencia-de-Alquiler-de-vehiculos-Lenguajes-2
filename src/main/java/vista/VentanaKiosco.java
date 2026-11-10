package vista;

import java.awt.Dimension;
import javax.swing.JFrame;
import modelo.Agencia;
import persistencia.GestorPersistencia;
import servicio.ServicioAlquiler;
import servicio.ServicioFlota;
import servicio.ServicioKiosco;

public class VentanaKiosco extends JFrame {

    private final GestorPersistencia gestor;
    private final CarpetaFotos fotos;

    public VentanaKiosco(GestorPersistencia gestor, CarpetaFotos fotos) {
        super("Kiosco - Alquiler de vehículos");
        this.gestor = gestor;
        this.fotos = fotos;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mostrarVehiculos();
        setMinimumSize(new Dimension(900, 600));
        setSize(1280, 800);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void mostrarVehiculos() {
        Agencia agencia = Main.cargarAgencia(gestor);
        ServicioAlquiler servicioAlquiler = new ServicioAlquiler(agencia);
        getContentPane().removeAll();
        add(new PanelKiosco(new ServicioFlota(agencia), fotos,
                vehiculo -> new FormularioAlquiler(this, vehiculo, servicioAlquiler,
                        new ServicioKiosco(gestor), this::mostrarVehiculos).setVisible(true)));
        revalidate();
        repaint();
    }
}
