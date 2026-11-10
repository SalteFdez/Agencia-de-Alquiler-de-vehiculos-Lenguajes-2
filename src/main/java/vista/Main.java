package vista;

import excepciones.AlquilerException;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import modelo.Agencia;
import persistencia.GestorPersistencia;
import servicio.ServicioAlquiler;
import servicio.ServicioClientes;
import servicio.ServicioFlota;
import servicio.ServicioOperario;

public class Main {

    public static void main(String[] args) {
        GestorPersistencia gestor = new GestorPersistencia(new File("agencia.dat"));
        SwingUtilities.invokeLater(() -> {
            Agencia agencia = cargarAgencia(gestor);
            new VentanaPrincipal(new ServicioFlota(agencia), new ServicioClientes(agencia),
                    new ServicioAlquiler(agencia), new ServicioOperario(gestor, agencia))
                    .setVisible(true);
        });
    }

    static Agencia cargarAgencia(GestorPersistencia gestor) {
        if (!gestor.existeArchivo()) {
            return new Agencia();
        }
        try {
            return gestor.cargar();
        } catch (AlquilerException e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage() + ". La aplicación arranca sin datos.",
                    "No se pudieron cargar los datos", JOptionPane.ERROR_MESSAGE);
            return new Agencia();
        }
    }
}
