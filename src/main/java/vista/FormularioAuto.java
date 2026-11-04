package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import servicio.ServicioFlota;

public class FormularioAuto extends JDialog {

    private final PanelDatosVehiculo datos = new PanelDatosVehiculo();
    private final ServicioFlota servicio;
    private final Runnable alRegistrar;

    private final JTextField puertas = new JTextField(15);
    private final JTextField pasajeros = new JTextField(15);
    private final JComboBox<TipoCaja> caja = new JComboBox<>(TipoCaja.values());

    public FormularioAuto(Frame padre, ServicioFlota servicio, Runnable alRegistrar) {
        super(padre, "Agregar auto", true);
        this.servicio = servicio;
        this.alRegistrar = alRegistrar;

        datos.agregar("Cantidad de puertas", puertas);
        datos.agregar("Capacidad de pasajeros", pasajeros);
        datos.agregar("Tipo de caja", caja);

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
            Auto auto = new Auto(
                    datos.getPatente(), datos.getMarca(), datos.getModelo(), datos.getColor(),
                    datos.getAnio(), datos.getKilometraje(), datos.getPrecioBaseDiario(),
                    PanelDatosVehiculo.entero("Cantidad de puertas", puertas),
                    PanelDatosVehiculo.entero("Capacidad de pasajeros", pasajeros),
                    (TipoCaja) caja.getSelectedItem());
            servicio.registrarVehiculo(auto);
            alRegistrar.run();
            dispose();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar el auto",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
