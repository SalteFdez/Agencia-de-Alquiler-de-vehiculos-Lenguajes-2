package vista;

import javax.swing.SwingUtilities;
import modelo.Agencia;
import servicio.ServicioClientes;
import servicio.ServicioFlota;

public class Main {

    public static void main(String[] args) {
        Agencia agencia = new Agencia();
        SwingUtilities.invokeLater(() ->
                new VentanaPrincipal(new ServicioFlota(agencia), new ServicioClientes(agencia))
                        .setVisible(true));
    }
}
