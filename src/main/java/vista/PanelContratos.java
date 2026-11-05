package vista;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import servicio.ServicioAlquiler;
import servicio.ServicioClientes;
import servicio.ServicioFlota;

public class PanelContratos extends JPanel {

    private final Frame padre;
    private final ServicioAlquiler servicioAlquiler;
    private final ServicioClientes servicioClientes;
    private final ServicioFlota servicioFlota;
    private final Runnable alCrear;
    private final ModeloTablaContratos modelo;
    private final JTable tablaContratos;
    private final JButton nuevoContrato = new JButton("Nuevo contrato");

    public PanelContratos(Frame padre, ServicioAlquiler servicioAlquiler,
                          ServicioClientes servicioClientes, ServicioFlota servicioFlota,
                          Runnable alCrear) {
        super(new BorderLayout());
        this.padre = padre;
        this.servicioAlquiler = servicioAlquiler;
        this.servicioClientes = servicioClientes;
        this.servicioFlota = servicioFlota;
        this.alCrear = alCrear;
        this.modelo = new ModeloTablaContratos(servicioAlquiler);
        tablaContratos = new JTable(modelo);

        nuevoContrato.addActionListener(e ->
                new FormularioContrato(padre, servicioAlquiler, servicioClientes, servicioFlota,
                        () -> {
                            modelo.fireTableDataChanged();
                            alCrear.run();
                        }).setVisible(true));

        JPanel botones = new JPanel();
        botones.add(nuevoContrato);

        add(new JScrollPane(tablaContratos), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }
}
