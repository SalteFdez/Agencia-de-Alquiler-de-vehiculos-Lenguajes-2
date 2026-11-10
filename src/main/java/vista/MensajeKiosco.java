package vista;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

public final class MensajeKiosco {

    private static final int SEGUNDOS_PARA_CERRAR = 6;
    private static final Font FUENTE = new Font(Font.SANS_SERIF, Font.BOLD, 28);

    private MensajeKiosco() {
    }

    public static void exito(Component padre, String texto) {
        mostrar(padre, "Alquiler confirmado", texto, JOptionPane.INFORMATION_MESSAGE,
                SEGUNDOS_PARA_CERRAR);
    }

    public static void error(Component padre, String texto) {
        mostrar(padre, "No se pudo alquilar", texto, JOptionPane.ERROR_MESSAGE, 0);
    }

    private static void mostrar(Component padre, String titulo, String texto, int tipo, int segundos) {
        JTextArea area = new JTextArea(texto);
        area.setFont(FUENTE);
        area.setColumns(28);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setOpaque(false);

        JButton aceptar = new JButton("Aceptar");
        aceptar.setFont(FUENTE);
        JOptionPane panel = new JOptionPane(area, tipo, JOptionPane.DEFAULT_OPTION, null,
                new Object[] {aceptar});
        aceptar.addActionListener(e -> panel.setValue(JOptionPane.OK_OPTION));
        JDialog dialogo = panel.createDialog(padre, titulo);

        Timer temporizador = new Timer(segundos * 1000, e -> dialogo.dispose());
        temporizador.setRepeats(false);
        if (segundos > 0) {
            temporizador.start();
        }
        dialogo.setVisible(true);
        temporizador.stop();
    }
}
