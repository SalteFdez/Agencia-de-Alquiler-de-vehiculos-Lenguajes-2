package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import javax.swing.JTable;
import modelo.Agencia;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;
import persistencia.GestorPersistencia;
import servicio.ServicioFlota;
import servicio.ServicioOperario;

class PanelFlotaTest {

    private static JTable tabla(Container contenedor) {
        for (Component hijo : contenedor.getComponents()) {
            if (hijo instanceof JTable) {
                return (JTable) hijo;
            }
            if (hijo instanceof Container) {
                JTable encontrada = tabla((Container) hijo);
                if (encontrada != null) {
                    return encontrada;
                }
            }
        }
        return null;
    }

    private static Auto auto(String patente) {
        return new Auto(patente, "Fiat", "Cronos", "Blanco", 2022, 15000, 10000.0, 4, 5, TipoCaja.MANUAL);
    }

    @Test
    void refrescarConservaLaFilaElegidaPorPatenteAunqueLosDatosCambienDeLugar() {
        Agencia agencia = new Agencia();
        agencia.getVehiculos().add(auto("AA111AA"));
        agencia.getVehiculos().add(auto("BB222BB"));
        agencia.getVehiculos().add(auto("CC333CC"));
        PanelFlota panel = new PanelFlota(null, new ServicioFlota(agencia),
                new ServicioOperario(new GestorPersistencia(new File("agencia-de-prueba.dat")), agencia));
        JTable tabla = tabla(panel);
        assertNotNull(tabla);
        tabla.setRowSelectionInterval(1, 1);

        agencia.getVehiculos().add(0, auto("ZZ999ZZ"));
        panel.refrescar();

        assertEquals(2, tabla.getSelectedRow());
        assertEquals("BB222BB", tabla.getValueAt(tabla.getSelectedRow(), 0));
    }
}
