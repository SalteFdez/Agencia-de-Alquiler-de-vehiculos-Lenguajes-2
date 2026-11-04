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
import modelo.flota.Moto;
import servicio.ServicioFlota;

public class FormularioMoto extends JDialog {

    private final PanelDatosVehiculo datos = new PanelDatosVehiculo();
    private final ServicioFlota servicio;
    private final Runnable alRegistrar;

    private final JTextField cilindrada = new JTextField(15);
    private final JCheckBox incluyeCasco = new JCheckBox();

    public FormularioMoto(Frame padre, ServicioFlota servicio, Runnable alRegistrar) {
        super(padre, "Agregar moto", true);
        this.servicio = servicio;
        this.alRegistrar = alRegistrar;

        datos.agregar("Cilindrada (cc)", cilindrada);
        datos.agregar("Incluye casco", incluyeCasco);

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
            Moto moto = new Moto(
                    datos.getPatente(), datos.getMarca(), datos.getModelo(), datos.getColor(),
                    datos.getAnio(), datos.getKilometraje(), datos.getPrecioBase(),
                    PanelDatosVehiculo.entero("Cilindrada", cilindrada),
                    incluyeCasco.isSelected());
            servicio.registrarVehiculo(moto);
            alRegistrar.run();
            dispose();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar la moto",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
