package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import servicio.ServicioFlota;

public class FormularioAuto extends JDialog {

    private final ServicioFlota servicio;
    private final Runnable alRegistrar;

    private final JTextField patente = new JTextField(15);
    private final JTextField marca = new JTextField(15);
    private final JTextField modelo = new JTextField(15);
    private final JTextField color = new JTextField(15);
    private final JTextField anio = new JTextField(15);
    private final JTextField kilometraje = new JTextField(15);
    private final JTextField precioBase = new JTextField(15);
    private final JTextField puertas = new JTextField(15);
    private final JTextField pasajeros = new JTextField(15);
    private final JComboBox<TipoCaja> caja = new JComboBox<>(TipoCaja.values());

    public FormularioAuto(Frame padre, ServicioFlota servicio, Runnable alRegistrar) {
        super(padre, "Agregar auto", true);
        this.servicio = servicio;
        this.alRegistrar = alRegistrar;

        JPanel campos = new JPanel(new GridLayout(0, 2, 8, 8));
        campos.setBorder(new EmptyBorder(12, 12, 12, 12));
        agregar(campos, "Patente", patente);
        agregar(campos, "Marca", marca);
        agregar(campos, "Modelo", modelo);
        agregar(campos, "Color", color);
        agregar(campos, "Año", anio);
        agregar(campos, "Kilometraje", kilometraje);
        agregar(campos, "Precio base diario", precioBase);
        agregar(campos, "Cantidad de puertas", puertas);
        agregar(campos, "Capacidad de pasajeros", pasajeros);
        agregar(campos, "Tipo de caja", caja);

        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(e -> guardar());
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dispose());
        JPanel botones = new JPanel();
        botones.add(guardar);
        botones.add(cancelar);

        add(campos, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(guardar);
        pack();
        setLocationRelativeTo(padre);
    }

    private void agregar(JPanel panel, String etiqueta, java.awt.Component campo) {
        panel.add(new JLabel(etiqueta));
        panel.add(campo);
    }

    private void guardar() {
        try {
            Auto auto = new Auto(
                    patente.getText(), marca.getText(), modelo.getText(), color.getText(),
                    entero("Año", anio), entero("Kilometraje", kilometraje),
                    decimal("Precio base diario", precioBase),
                    entero("Cantidad de puertas", puertas),
                    entero("Capacidad de pasajeros", pasajeros),
                    (TipoCaja) caja.getSelectedItem());
            servicio.registrarVehiculo(auto);
            alRegistrar.run();
            dispose();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar el auto",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int entero(String etiqueta, JTextField campo) throws AlquilerException {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException ex) {
            throw new AlquilerException("El campo " + etiqueta + " debe ser un número entero");
        }
    }

    private double decimal(String etiqueta, JTextField campo) throws AlquilerException {
        try {
            return Double.parseDouble(campo.getText().trim());
        } catch (NumberFormatException ex) {
            throw new AlquilerException("El campo " + etiqueta + " debe ser un número");
        }
    }
}
