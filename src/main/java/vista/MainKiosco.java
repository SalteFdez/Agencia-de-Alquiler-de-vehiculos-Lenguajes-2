package vista;

import java.io.File;
import javax.swing.SwingUtilities;
import modelo.Agencia;
import persistencia.GestorPersistencia;
import servicio.ServicioFlota;

public class MainKiosco {

    public static void main(String[] args) {
        GestorPersistencia gestor = new GestorPersistencia(new File("agencia.dat"));
        SwingUtilities.invokeLater(() -> {
            Agencia agencia = Main.cargarAgencia(gestor);
            new VentanaKiosco(new ServicioFlota(agencia), new CarpetaFotos(new File("imagenes")))
                    .setVisible(true);
        });
    }
}
