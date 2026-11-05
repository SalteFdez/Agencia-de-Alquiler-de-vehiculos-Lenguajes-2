package vista;

import excepciones.AlquilerException;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import modelo.Formatos;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import servicio.ServicioClientes;

public class FormularioCliente extends JDialog {

    private final ServicioClientes servicio;
    private final Runnable alRegistrar;

    private final JTextField nombre = new JTextField(15);
    private final JTextField apellido = new JTextField(15);
    private final JTextField dni = new JTextField(15);
    private final JTextField fechaNacimiento = new JTextField(15);
    private final JTextField telefono = new JTextField(15);
    private final JTextField numero = new JTextField(15);
    private final JTextField fechaVencimiento = new JTextField(15);
    private final Map<CategoriaLicencia, JCheckBox> categorias = new EnumMap<>(CategoriaLicencia.class);

    public FormularioCliente(Frame padre, ServicioClientes servicio, Runnable alRegistrar) {
        super(padre, "Agregar cliente", true);
        this.servicio = servicio;
        this.alRegistrar = alRegistrar;

        JPanel casillas = new JPanel();
        for (CategoriaLicencia categoria : CategoriaLicencia.values()) {
            JCheckBox casilla = new JCheckBox(categoria.name());
            categorias.put(categoria, casilla);
            casillas.add(casilla);
        }

        JPanel datos = new JPanel(new GridLayout(0, 2, 8, 8));
        datos.setBorder(new EmptyBorder(12, 12, 12, 12));
        agregar(datos, "Nombre", nombre);
        agregar(datos, "Apellido", apellido);
        agregar(datos, "DNI", dni);
        agregar(datos, "Fecha de nacimiento (dd/mm/aaaa)", fechaNacimiento);
        agregar(datos, "Teléfono", telefono);
        agregar(datos, "Número de licencia", numero);
        agregar(datos, "Categorías", casillas);
        agregar(datos, "Vencimiento de la licencia (dd/mm/aaaa)", fechaVencimiento);

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

    private static void agregar(JPanel panel, String etiqueta, java.awt.Component campo) {
        panel.add(new JLabel(etiqueta));
        panel.add(campo);
    }

    private void guardar() {
        try {
            LocalDate nacimiento = fecha("de nacimiento", fechaNacimiento);
            LocalDate vencimiento = fecha("de vencimiento", fechaVencimiento);
            Set<CategoriaLicencia> elegidas = EnumSet.noneOf(CategoriaLicencia.class);
            categorias.forEach((categoria, casilla) -> {
                if (casilla.isSelected()) {
                    elegidas.add(categoria);
                }
            });
            Cliente cliente = new Cliente(nombre.getText(), apellido.getText(), dni.getText(),
                    nacimiento, telefono.getText(),
                    new LicenciaConducir(numero.getText(), elegidas, vencimiento));
            servicio.registrarCliente(cliente);
            alRegistrar.run();
            avisarSiNoPuedeAlquilar(cliente);
            dispose();
        } catch (AlquilerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar el cliente",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void avisarSiNoPuedeAlquilar(Cliente cliente) {
        List<String> motivos = new ArrayList<>();
        if (cliente.getEdad() < Cliente.EDAD_MINIMA) {
            motivos.add("es menor de " + Cliente.EDAD_MINIMA + " años");
        }
        if (!cliente.getLicencia().estaVigente(LocalDate.now())) {
            motivos.add("su licencia está vencida");
        }
        if (!motivos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Cliente registrado, pero todavía no va a poder alquilar: "
                            + String.join(" y ", motivos) + ".",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    static LocalDate fecha(String etiqueta, JTextField campo) throws AlquilerException {
        try {
            return LocalDate.parse(campo.getText().trim(), Formatos.FECHA);
        } catch (DateTimeParseException ex) {
            throw new AlquilerException("La fecha " + etiqueta + " debe tener el formato dd/mm/aaaa");
        }
    }
}
