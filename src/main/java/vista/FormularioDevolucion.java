package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import modelo.alquiler.Contrato;
import servicio.ServicioAlquiler;

public class FormularioDevolucion extends JDialog {

    private final ServicioAlquiler servicioAlquiler;
    private final Contrato contrato;
    private final Runnable alRegistrar;

    private final JTextField campoFechaDevolucion = new JTextField(15);
    private final JTextField campoKmFinal = new JTextField(15);

    public FormularioDevolucion(Frame padre, ServicioAlquiler servicioAlquiler, Contrato contrato,
                                Runnable alRegistrar) {
        super(padre, "Registrar devolución", true);
        this.servicioAlquiler = servicioAlquiler;
        this.contrato = contrato;
        this.alRegistrar = alRegistrar;
        campoFechaDevolucion.setText(
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        JPanel datos = new JPanel(new GridLayout(0, 2, 8, 8));
        datos.setBorder(new EmptyBorder(12, 12, 12, 12));
        datos.add(new JLabel("Fecha de devolución (dd/mm/aaaa)"));
        datos.add(campoFechaDevolucion);
        datos.add(new JLabel("Km final"));
        datos.add(campoKmFinal);

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
            LocalDate fecha = FormularioCliente.fecha("de devolución", campoFechaDevolucion);
            int kmFinal = PanelDatosVehiculo.entero("Km final", campoKmFinal);
            servicioAlquiler.registrarDevolucion(contrato, fecha, kmFinal);
            alRegistrar.run();
            JOptionPane.showMessageDialog(this,
                    "Días cobrados: " + contrato.getDias()
                            + "\nKm recorridos: " + contrato.getKmRecorridos()
                            + "\nTotal a cobrar: " + String.format("%.2f", contrato.calcularTotal()),
                    "Devolución registrada", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo registrar la devolución", JOptionPane.ERROR_MESSAGE);
        }
    }
}
