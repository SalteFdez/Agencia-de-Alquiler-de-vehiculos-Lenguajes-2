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
import servicio.ServicioOperario;

public class PanelContratos extends JPanel {

    private static final String TODOS = "Todos";

    private final Frame padre;
    private final ServicioAlquiler servicioAlquiler;
    private final ServicioClientes servicioClientes;
    private final ServicioFlota servicioFlota;
    private final ServicioOperario servicioOperario;
    private final Runnable alCambiarFlota;
    private final ModeloTablaContratos modelo;
    private final JComboBox<String> comboEstado =
            new JComboBox<>(new String[] {TODOS, "ACTIVO", "FINALIZADO"});
    private final JTable tablaContratos;
    private final JButton nuevoContrato = new JButton("Nuevo contrato");
    private final JButton devolucionBoton = new JButton("Registrar devolución");
    private Integer numeroElegido;
    private boolean refrescando;

    public PanelContratos(Frame padre, ServicioAlquiler servicioAlquiler,
                          ServicioClientes servicioClientes, ServicioFlota servicioFlota,
                          ServicioOperario servicioOperario, Runnable alCambiarFlota) {
        super(new BorderLayout());
        this.padre = padre;
        this.servicioAlquiler = servicioAlquiler;
        this.servicioClientes = servicioClientes;
        this.servicioFlota = servicioFlota;
        this.servicioOperario = servicioOperario;
        this.alCambiarFlota = alCambiarFlota;
        this.modelo = new ModeloTablaContratos(servicioAlquiler);
        tablaContratos = new JTable(modelo);

        comboEstado.addActionListener(e -> modelo.setFiltro(
                TODOS.equals(comboEstado.getSelectedItem()) ? null
                        : EstadoContrato.valueOf((String) comboEstado.getSelectedItem())));

        Runnable alCambiarContratos = () -> {
            refrescar();
            alCambiarFlota.run();
        };

        nuevoContrato.addActionListener(e ->
                new FormularioContrato(padre, servicioAlquiler, servicioClientes, servicioFlota,
                        servicioOperario, alCambiarContratos).setVisible(true));

        devolucionBoton.setEnabled(false);
        tablaContratos.getSelectionModel().addListSelectionListener(e -> {
            devolucionBoton.setEnabled(tablaContratos.getSelectedRow() >= 0);
            if (!refrescando) {
                numeroElegido = tablaContratos.getSelectedRow() >= 0
                        ? modelo.getContratoEn(tablaContratos.getSelectedRow()).getNumero() : null;
            }
        });
        devolucionBoton.addActionListener(e ->
                new FormularioDevolucion(padre, servicioOperario,
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

    public void refrescar() {
        refrescando = true;
        modelo.fireTableDataChanged();
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {
            if (Integer.valueOf(modelo.getContratoEn(fila).getNumero()).equals(numeroElegido)) {
                tablaContratos.setRowSelectionInterval(fila, fila);
                break;
            }
        }
        refrescando = false;
    }
}
