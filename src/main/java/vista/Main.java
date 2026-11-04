package vista;

import javax.swing.SwingUtilities;
import modelo.Agencia;
import servicio.ServicioFlota;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new VentanaPrincipal(new ServicioFlota(new Agencia())).setVisible(true));
    }
}
