package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.flota.Camioneta;
import servicio.ServicioFlota;

public class FormularioCamioneta extends JDialog {

    private final PanelDatosVehiculo datos = new PanelDatosVehiculo();
    private final ServicioFlota servicio;
    private final Runnable alRegistrar;

    private final JTextField capacidadCargaKg = new JTextField(15);
    private final JCheckBox traccion4x4 = new JCheckBox();

    public FormularioCamioneta(Frame padre, ServicioFlota servicio, Runnable alRegistrar) {
        super(padre, "Agregar camioneta", true);
        this.servicio = servicio;
        this.alRegistrar = alRegistrar;

        datos.agregar("Capacidad de carga (kg)", capacidadCargaKg);
        datos.agregar("Tracción 4x4", traccion4x4);

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
            Camioneta camioneta = new Camioneta(
                    datos.getPatente(), datos.getMarca(), datos.getModelo(), datos.getColor(),
                    datos.getAnio(), datos.getKilometraje(), datos.getPrecioBaseDiario(),
                    PanelDatosVehiculo.decimal("Capacidad de carga", capacidadCargaKg),
                    traccion4x4.isSelected());
            servicio.registrarVehiculo(camioneta);
            alRegistrar.run();
            dispose();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar la camioneta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
