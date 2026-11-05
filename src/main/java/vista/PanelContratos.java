package vista;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import modelo.alquiler.EstadoContrato;
import servicio.ServicioAlquiler;
import servicio.ServicioClientes;
import servicio.ServicioFlota;

public class PanelContratos extends JPanel {

    private static final String TODOS = "Todos";

    private final Frame padre;
    private final ServicioAlquiler servicioAlquiler;
    private final ServicioClientes servicioClientes;
    private final ServicioFlota servicioFlota;
    private final Runnable alCrear;
    private final ModeloTablaContratos modelo;
    private final JComboBox<String> comboEstado =
            new JComboBox<>(new String[] {TODOS, "ACTIVO", "FINALIZADO"});
    private final JTable tablaContratos;
    private final JButton nuevoContrato = new JButton("Nuevo contrato");
    private final JButton devolucionBoton = new JButton("Registrar devolución");

    public PanelContratos(Frame padre, ServicioAlquiler servicioAlquiler,
                          ServicioClientes servicioClientes, ServicioFlota servicioFlota,
                          Runnable alCrear) {
        super(new BorderLayout());
        this.padre = padre;
        this.servicioAlquiler = servicioAlquiler;
        this.servicioClientes = servicioClientes;
        this.servicioFlota = servicioFlota;
        this.alCrear = alCrear;
        this.modelo = new ModeloTablaContratos(servicioAlquiler);
        tablaContratos = new JTable(modelo);

        comboEstado.addActionListener(e -> modelo.setFiltro(
                TODOS.equals(comboEstado.getSelectedItem()) ? null
                        : EstadoContrato.valueOf((String) comboEstado.getSelectedItem())));

        Runnable alCambiarContratos = () -> {
            modelo.fireTableDataChanged();
            alCrear.run();
        };

        nuevoContrato.addActionListener(e ->
                new FormularioContrato(padre, servicioAlquiler, servicioClientes, servicioFlota,
                        alCambiarContratos).setVisible(true));

        devolucionBoton.setEnabled(false);
        tablaContratos.getSelectionModel().addListSelectionListener(e ->
                devolucionBoton.setEnabled(tablaContratos.getSelectedRow() >= 0));
        devolucionBoton.addActionListener(e ->
                new FormularioDevolucion(padre, servicioAlquiler,
                        modelo.getContratoEn(tablaContratos.getSelectedRow()),
                        alCambiarContratos).setVisible(true));

        JPanel botones = new JPanel();
        botones.add(nuevoContrato);
        botones.add(devolucionBoton);

        JPanel filtros = new JPanel();
        filtros.add(new JLabel("Estado"));
        filtros.add(comboEstado);

        add(filtros, BorderLayout.NORTH);
        add(new JScrollPane(tablaContratos), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }
}
