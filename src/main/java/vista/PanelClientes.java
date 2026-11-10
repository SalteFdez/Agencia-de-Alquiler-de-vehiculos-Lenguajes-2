package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modelo.clientes.Cliente;
import servicio.ServicioClientes;

public class PanelClientes extends JPanel {

    private final Frame padre;
    private final ServicioClientes servicio;
    private final ModeloTablaClientes modelo;
    private final JTextField campoBusqueda = new JTextField(20);
    private final JTable tablaClientes;
    private final JButton agregarCliente = new JButton("Agregar cliente");
    private final JButton eliminarClienteBoton = new JButton("Eliminar cliente");
    private String dniElegido;
    private boolean refrescando;

    public PanelClientes(Frame padre, ServicioClientes servicio) {
        super(new BorderLayout());
        this.padre = padre;
        this.servicio = servicio;
        this.modelo = new ModeloTablaClientes(servicio);
        tablaClientes = new JTable(modelo);

        agregarCliente.addActionListener(e ->
                new FormularioCliente(padre, servicio, modelo::fireTableDataChanged).setVisible(true));
        alCambiarElTexto(campoBusqueda, () -> modelo.setFiltro(campoBusqueda.getText()));
        eliminarClienteBoton.setEnabled(false);
        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            eliminarClienteBoton.setEnabled(tablaClientes.getSelectedRow() >= 0);
            if (!refrescando) {
                dniElegido = tablaClientes.getSelectedRow() >= 0
                        ? modelo.getClienteEn(tablaClientes.getSelectedRow()).getDni() : null;
            }
        });
        eliminarClienteBoton.addActionListener(e -> eliminarClienteElegido());

        JPanel busqueda = new JPanel();
        busqueda.add(new JLabel("Buscar"));
        busqueda.add(campoBusqueda);
        JPanel botones = new JPanel();
        botones.add(agregarCliente);
        botones.add(eliminarClienteBoton);

        add(busqueda, BorderLayout.NORTH);
        add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    public void refrescar() {
        refrescando = true;
        modelo.fireTableDataChanged();
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {
            if (modelo.getClienteEn(fila).getDni().equals(dniElegido)) {
                tablaClientes.setRowSelectionInterval(fila, fila);
                break;
            }
        }
        refrescando = false;
    }

    private void eliminarClienteElegido() {
        Cliente elegido = modelo.getClienteEn(tablaClientes.getSelectedRow());
        int respuesta = JOptionPane.showConfirmDialog(padre,
                "¿Eliminar al cliente " + elegido.getNombreCompleto() + " (DNI " + elegido.getDni() + ")?",
                "Eliminar cliente", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            servicio.eliminarCliente(elegido);
            modelo.fireTableDataChanged();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(padre, ex.getMessage(),
                    "No se pudo eliminar el cliente", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void alCambiarElTexto(JTextField campo, Runnable accion) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                accion.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                accion.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                accion.run();
            }
        });
    }
}
