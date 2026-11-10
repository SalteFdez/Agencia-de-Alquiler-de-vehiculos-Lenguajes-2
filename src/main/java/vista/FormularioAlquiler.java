package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import modelo.Formatos;
import modelo.alquiler.Contrato;
import modelo.flota.Vehiculo;
import servicio.ServicioAlquiler;
import servicio.ServicioKiosco;

public class FormularioAlquiler extends JDialog {

    private static final Font FUENTE = new Font(Font.SANS_SERIF, Font.PLAIN, 28);
    private static final String SIN_TOTAL = "-";

    private final Vehiculo vehiculo;
    private final ServicioAlquiler servicioAlquiler;
    private final ServicioKiosco servicioKiosco;
    private final Runnable alAlquilar;

    private final JTextField campoDni = new JTextField(12);
    private final JTextField campoFechaDevolucion = new JTextField(12);
    private final JLabel etiquetaTotal = new JLabel(SIN_TOTAL);

    public FormularioAlquiler(Frame padre, Vehiculo vehiculo, ServicioAlquiler servicioAlquiler,
                              ServicioKiosco servicioKiosco, Runnable alAlquilar) {
        super(padre, "Alquilar vehículo", true);
        this.vehiculo = vehiculo;
        this.servicioAlquiler = servicioAlquiler;
        this.servicioKiosco = servicioKiosco;
        this.alAlquilar = alAlquilar;

        PanelClientes.alCambiarElTexto(campoFechaDevolucion, this::actualizarTotal);

        JLabel elegido = new JLabel(vehiculo.getMarca() + " " + vehiculo.getModelo()
                + " (" + vehiculo.getPatente() + ")");
        elegido.setFont(FUENTE.deriveFont(Font.BOLD, 32f));
        elegido.setBorder(new EmptyBorder(16, 16, 0, 16));

        JPanel datos = new JPanel(new GridLayout(0, 2, 16, 16));
        datos.setBorder(new EmptyBorder(16, 16, 16, 16));
        datos.add(etiqueta("Tu DNI"));
        datos.add(campoDni);
        datos.add(etiqueta("Devolución (dd/mm/aaaa)"));
        datos.add(campoFechaDevolucion);
        datos.add(etiqueta("Total"));
        datos.add(etiquetaTotal);
        campoDni.setFont(FUENTE);
        campoFechaDevolucion.setFont(FUENTE);
        etiquetaTotal.setFont(FUENTE.deriveFont(Font.BOLD));

        JButton confirmar = new JButton("Confirmar");
        confirmar.setFont(FUENTE);
        confirmar.addActionListener(e -> confirmar());
        JButton cancelar = new JButton("Cancelar");
        cancelar.setFont(FUENTE);
        cancelar.addActionListener(e -> dispose());
        JPanel botones = new JPanel();
        botones.setBorder(new EmptyBorder(0, 16, 16, 16));
        botones.add(confirmar);
        botones.add(cancelar);

        add(elegido, BorderLayout.NORTH);
        add(datos, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(confirmar);
        pack();
        setLocationRelativeTo(padre);
    }

    private static JLabel etiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE);
        return etiqueta;
    }

    private void confirmar() {
        try {
            LocalDate fin = FormularioCliente.fecha("de devolución", campoFechaDevolucion);
            Contrato contrato = servicioKiosco.alquilar(campoDni.getText(), vehiculo.getPatente(), fin);
            dispose();
            alAlquilar.run();
            MensajeKiosco.exito(getOwner(), "¡Listo! Tu contrato es el N° " + contrato.getNumero()
                    + ".\n" + vehiculo.getMarca() + " " + vehiculo.getModelo()
                    + "\nDevolución: " + fin.format(Formatos.FECHA));
        } catch (AlquilerException ex) {
            MensajeKiosco.error(this, ex.getMessage());
        }
    }

    private void actualizarTotal() {
        try {
            LocalDate fin = FormularioCliente.fecha("de devolución", campoFechaDevolucion);
            double total = servicioAlquiler.calcularTotalEstimado(vehiculo, fin);
            etiquetaTotal.setText(String.format(Locale.forLanguageTag("es-AR"), "$%,.2f", total));
        } catch (AlquilerException ex) {
            etiquetaTotal.setText(SIN_TOTAL);
        }
    }
}
