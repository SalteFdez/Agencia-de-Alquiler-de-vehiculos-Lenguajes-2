package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Vehiculo;
import servicio.ServicioFlota;

public class PanelFlota extends JPanel {

    private static final String TODOS = "Todos";

    private final Frame padre;
    private final ServicioFlota servicio;
    private final ModeloTablaFlota modelo;
    private final JComboBox<String> comboTipo =
            new JComboBox<>(new String[] {TODOS, "Auto", "Moto", "Camioneta"});
    private final JComboBox<String> comboEstado =
            new JComboBox<>(new String[] {TODOS, "DISPONIBLE", "ALQUILADO"});
    private final JTable tablaFlota;
    private final JButton darDeBaja = new JButton("Dar de baja");

    public PanelFlota(Frame padre, ServicioFlota servicio) {
        super(new BorderLayout());
        this.padre = padre;
        this.servicio = servicio;
        this.modelo = new ModeloTablaFlota(servicio);
        tablaFlota = new JTable(modelo);

        JButton agregarAuto = new JButton("Agregar auto");
        agregarAuto.addActionListener(e ->
                new FormularioAuto(padre, servicio, modelo::fireTableDataChanged).setVisible(true));
        JButton agregarMoto = new JButton("Agregar moto");
        agregarMoto.addActionListener(e ->
                new FormularioMoto(padre, servicio, modelo::fireTableDataChanged).setVisible(true));
        JButton agregarCamioneta = new JButton("Agregar camioneta");
        agregarCamioneta.addActionListener(e ->
                new FormularioCamioneta(padre, servicio, modelo::fireTableDataChanged).setVisible(true));

        Runnable aplicarFiltro = () -> modelo.setFiltro(
                TODOS.equals(comboTipo.getSelectedItem()) ? null : (String) comboTipo.getSelectedItem(),
                TODOS.equals(comboEstado.getSelectedItem()) ? null
                        : EstadoVehiculo.valueOf((String) comboEstado.getSelectedItem()));
        comboTipo.addActionListener(e -> aplicarFiltro.run());
        comboEstado.addActionListener(e -> aplicarFiltro.run());
        darDeBaja.setEnabled(false);
        tablaFlota.getSelectionModel().addListSelectionListener(e ->
                darDeBaja.setEnabled(tablaFlota.getSelectedRow() >= 0));
        darDeBaja.addActionListener(e -> darDeBajaElegido());

        JPanel filtros = new JPanel();
        filtros.add(new JLabel("Tipo"));
        filtros.add(comboTipo);
        filtros.add(new JLabel("Estado"));
        filtros.add(comboEstado);
        JPanel botones = new JPanel();
        botones.add(agregarAuto);
        botones.add(agregarMoto);
        botones.add(agregarCamioneta);
        botones.add(darDeBaja);

        add(filtros, BorderLayout.NORTH);
        add(new JScrollPane(tablaFlota), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    public void refrescar() {
        modelo.fireTableDataChanged();
    }

    private void darDeBajaElegido() {
        Vehiculo elegido = modelo.getVehiculoEn(tablaFlota.getSelectedRow());
        int respuesta = JOptionPane.showConfirmDialog(padre,
                "¿Dar de baja el vehículo " + elegido.getPatente() + "?",
                "Dar de baja", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            servicio.eliminarVehiculo(elegido);
            modelo.fireTableDataChanged();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(padre, ex.getMessage(),
                    "No se pudo dar de baja el vehículo", JOptionPane.ERROR_MESSAGE);
        }
    }
}
