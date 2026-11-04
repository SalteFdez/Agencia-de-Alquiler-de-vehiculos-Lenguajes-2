package vista;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import servicio.ServicioFlota;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(ServicioFlota servicio) {
        super("Agencia de alquiler de vehículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);
        JButton agregarAuto = new JButton("Agregar auto");
        agregarAuto.addActionListener(e ->
                new FormularioAuto(this, servicio, modelo::fireTableDataChanged).setVisible(true));
        JButton agregarMoto = new JButton("Agregar moto");
        agregarMoto.addActionListener(e ->
                new FormularioMoto(this, servicio, modelo::fireTableDataChanged).setVisible(true));
        JButton agregarCamioneta = new JButton("Agregar camioneta");
        agregarCamioneta.addActionListener(e ->
                new FormularioCamioneta(this, servicio, modelo::fireTableDataChanged).setVisible(true));

        JPanel flota = new JPanel(new BorderLayout());
        flota.add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
        JPanel botones = new JPanel();
        botones.add(agregarAuto);
        botones.add(agregarMoto);
        botones.add(agregarCamioneta);
        flota.add(botones, BorderLayout.SOUTH);

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Flota", flota);
        add(pestanias);

        setSize(900, 500);
        setLocationRelativeTo(null);
    }
}
