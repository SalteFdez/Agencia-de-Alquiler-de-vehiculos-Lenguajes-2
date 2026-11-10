package vista;

import java.io.File;
import javax.swing.SwingUtilities;
import persistencia.GestorPersistencia;

public class MainKiosco {

    public static void main(String[] args) {
        GestorPersistencia gestor = new GestorPersistencia(new File("agencia.dat"));
        SwingUtilities.invokeLater(() ->
                new VentanaKiosco(gestor, new CarpetaFotos(new File("imagenes"))).setVisible(true));
    }
}
