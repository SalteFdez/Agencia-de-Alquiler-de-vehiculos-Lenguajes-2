package vista;

import excepciones.AlquilerException;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** Campos comunes a todo vehículo. Cada formulario agrega a continuación los suyos. */
public class PanelDatosVehiculo extends JPanel {

    private final JTextField patente = new JTextField(15);
    private final JTextField marca = new JTextField(15);
    private final JTextField modelo = new JTextField(15);
    private final JTextField color = new JTextField(15);
    private final JTextField anio = new JTextField(15);
    private final JTextField kilometraje = new JTextField(15);
    private final JTextField precioBaseDiario = new JTextField(15);

    public PanelDatosVehiculo() {
        super(new GridLayout(0, 2, 8, 8));
        setBorder(new EmptyBorder(12, 12, 12, 12));
        agregar("Patente", patente);
        agregar("Marca", marca);
        agregar("Modelo", modelo);
        agregar("Color", color);
        agregar("Año", anio);
        agregar("Kilometraje", kilometraje);
        agregar("Precio base diario", precioBaseDiario);
    }

    public void agregar(String etiqueta, Component campo) {
        add(new JLabel(etiqueta));
        add(campo);
    }

    public String getPatente() {
        return patente.getText();
    }

    public String getMarca() {
        return marca.getText();
    }

    public String getModelo() {
        return modelo.getText();
    }

    public String getColor() {
        return color.getText();
    }

    public int getAnio() throws AlquilerException {
        return entero("Año", anio);
    }

    public int getKilometraje() throws AlquilerException {
        return entero("Kilometraje", kilometraje);
    }

    public double getPrecioBaseDiario() throws AlquilerException {
        return decimal("Precio base diario", precioBaseDiario);
    }

    static int entero(String etiqueta, JTextField campo) throws AlquilerException {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException ex) {
            throw new AlquilerException("El campo " + etiqueta + " debe ser un número entero");
        }
    }

    static double decimal(String etiqueta, JTextField campo) throws AlquilerException {
        try {
            return Double.parseDouble(campo.getText().trim());
        } catch (NumberFormatException ex) {
            throw new AlquilerException("El campo " + etiqueta + " debe ser un número");
        }
    }
}
