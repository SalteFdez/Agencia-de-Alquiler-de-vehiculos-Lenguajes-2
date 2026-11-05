package vista;

import excepciones.AlquilerException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import modelo.Agencia;
import persistencia.GestorPersistencia;
import servicio.ServicioAlquiler;
import servicio.ServicioClientes;
import servicio.ServicioFlota;

public class Main {

    public static void main(String[] args) {
        GestorPersistencia gestor = new GestorPersistencia(new File("agencia.dat"));
        SwingUtilities.invokeLater(() -> {
            Agencia agencia = cargarAgencia(gestor);
            VentanaPrincipal ventana = new VentanaPrincipal(new ServicioFlota(agencia),
                    new ServicioClientes(agencia), new ServicioAlquiler(agencia));
            guardarAlCerrar(ventana, gestor, agencia);
            ventana.setVisible(true);
        });
    }

    private static Agencia cargarAgencia(GestorPersistencia gestor) {
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

    private static void guardarAlCerrar(JFrame ventana, GestorPersistencia gestor, Agencia agencia) {
        ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    gestor.guardar(agencia);
                    ventana.dispose();
                } catch (AlquilerException ex) {
                    int respuesta = JOptionPane.showConfirmDialog(ventana,
                            ex.getMessage() + ". ¿Cerrar igual sin guardar?",
                            "No se pudieron guardar los datos", JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        ventana.dispose();
                    }
                }
            }
        });
    }
}
