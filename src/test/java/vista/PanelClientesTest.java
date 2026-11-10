package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.time.LocalDate;
import java.util.Set;
import javax.swing.JTable;
import modelo.Agencia;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import org.junit.jupiter.api.Test;
import persistencia.GestorPersistencia;
import servicio.ServicioClientes;
import servicio.ServicioOperario;

class PanelClientesTest {

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

    private static Cliente cliente(String dni) {
        return new Cliente("Ana", "Gomez", dni, LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3)));
    }

    @Test
    void refrescarConservaLaFilaElegidaPorDniAunqueLosDatosCambienDeLugar() {
        Agencia agencia = new Agencia();
        agencia.getClientes().add(cliente("11111111"));
        agencia.getClientes().add(cliente("22222222"));
        agencia.getClientes().add(cliente("33333333"));
        PanelClientes panel = new PanelClientes(null, new ServicioClientes(agencia),
                new ServicioOperario(new GestorPersistencia(new File("agencia-de-prueba.dat")), agencia));
        JTable tabla = tabla(panel);
        assertNotNull(tabla);
        tabla.setRowSelectionInterval(1, 1);

        agencia.getClientes().add(0, cliente("99999999"));
        panel.refrescar();

        assertEquals(2, tabla.getSelectedRow());
        assertEquals("22222222", tabla.getValueAt(tabla.getSelectedRow(), 2));
    }
}
