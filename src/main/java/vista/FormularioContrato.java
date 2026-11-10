package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import modelo.clientes.Cliente;
import modelo.flota.EstadoVehiculo;
import modelo.flota.Vehiculo;
import modelo.tarifas.TarifaDiaria;
import servicio.ServicioAlquiler;
import servicio.ServicioClientes;
import servicio.ServicioFlota;

public class FormularioContrato extends JDialog {

    private static final String SIN_TOTAL = "-";

    private final ServicioAlquiler servicioAlquiler;
    private final Runnable alCrear;

    private final JComboBox<Cliente> comboCliente = new JComboBox<>();
    private final JComboBox<Vehiculo> comboVehiculo = new JComboBox<>();
    private final JTextField campoFechaFin = new JTextField(15);
    private final JLabel etiquetaTotal = new JLabel(SIN_TOTAL);

    public FormularioContrato(Frame padre, ServicioAlquiler servicioAlquiler,
                              ServicioClientes servicioClientes, ServicioFlota servicioFlota,
                              Runnable alCrear) {
        super(padre, "Nuevo contrato", true);
        this.servicioAlquiler = servicioAlquiler;
        this.alCrear = alCrear;

        for (Cliente cliente : servicioClientes.listarClientes()) {
            comboCliente.addItem(cliente);
        }
        for (Vehiculo vehiculo : servicioFlota.filtrar(null, EstadoVehiculo.DISPONIBLE)) {
            comboVehiculo.addItem(vehiculo);
        }
        comboCliente.setSelectedIndex(-1);
        comboVehiculo.setSelectedIndex(-1);
        comboVehiculo.addActionListener(e -> actualizarTotal());
        PanelClientes.alCambiarElTexto(campoFechaFin, this::actualizarTotal);

        JPanel datos = new JPanel(new GridLayout(0, 2, 8, 8));
        datos.setBorder(new EmptyBorder(12, 12, 12, 12));
        datos.add(new JLabel("Cliente"));
        datos.add(comboCliente);
        datos.add(new JLabel("Vehículo disponible"));
        datos.add(comboVehiculo);
        datos.add(new JLabel("Fin prevista (dd/mm/aaaa)"));
        datos.add(campoFechaFin);
        datos.add(new JLabel("Total estimado"));
        datos.add(etiquetaTotal);

        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(e -> guardar());
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dispose());
        JPanel botones = new JPanel();
        botones.add(guardar);
        botones.add(cancelar);

        add(datos, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(guardar);
        pack();
        setLocationRelativeTo(padre);
    }

    private void guardar() {
        try {
            Cliente cliente = (Cliente) comboCliente.getSelectedItem();
            if (cliente == null) {
                throw new AlquilerException("Hay que elegir un cliente");
            }
            Vehiculo vehiculo = (Vehiculo) comboVehiculo.getSelectedItem();
            if (vehiculo == null) {
                throw new AlquilerException("Hay que elegir un vehículo disponible");
            }
            LocalDate fin = FormularioCliente.fecha("de fin prevista", campoFechaFin);
            servicioAlquiler.crearContrato(cliente, vehiculo, new TarifaDiaria(), fin);
            alCrear.run();
            dispose();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo crear el contrato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTotal() {
        Vehiculo vehiculo = (Vehiculo) comboVehiculo.getSelectedItem();
        try {
            LocalDate fin = FormularioCliente.fecha("de fin prevista", campoFechaFin);
            if (vehiculo == null) {
                etiquetaTotal.setText(SIN_TOTAL);
                return;
            }
            double total = servicioAlquiler.calcularTotalEstimado(vehiculo, fin);
            etiquetaTotal.setText(String.format("%.2f", total));
        } catch (AlquilerException ex) {
            etiquetaTotal.setText(SIN_TOTAL);
        }
    }
}
