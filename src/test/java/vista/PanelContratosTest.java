package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import excepciones.AlquilerException;
import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.time.LocalDate;
import java.util.Set;
import javax.swing.JTable;
import modelo.Agencia;
import modelo.alquiler.Contrato;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;
import persistencia.GestorPersistencia;
import servicio.ServicioAlquiler;
import servicio.ServicioClientes;
import servicio.ServicioFlota;
import servicio.ServicioOperario;

class PanelContratosTest {

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
    void refrescarConservaLaFilaElegidaPorNumeroAunqueLosDatosCambienDeLugar() throws AlquilerException {
        Agencia agencia = new Agencia();
        Cliente ana = new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3)));
        ServicioAlquiler alquiler = new ServicioAlquiler(agencia);
        for (String patente : new String[] {"AA111AA", "BB222BB", "CC333CC"}) {
            agencia.getVehiculos().add(auto(patente));
        }
        for (int i = 0; i < 3; i++) {
            alquiler.crearContrato(ana, agencia.getVehiculos().get(i), new TarifaDiaria(),
                    LocalDate.now().plusDays(2));
        }
        PanelContratos panel = new PanelContratos(null, alquiler, new ServicioClientes(agencia),
                new ServicioFlota(agencia),
                new ServicioOperario(new GestorPersistencia(new File("agencia-de-prueba.dat")), agencia),
                () -> { });
        JTable tabla = tabla(panel);
        assertNotNull(tabla);
        tabla.setRowSelectionInterval(1, 1);

        Contrato nuevo = new Contrato(ana, auto("ZZ999ZZ"), new TarifaDiaria(), LocalDate.now(),
                LocalDate.now().plusDays(2));
        nuevo.setNumero(99);
        agencia.getContratos().add(0, nuevo);
        panel.refrescar();

        assertEquals(2, tabla.getSelectedRow());
        assertEquals(2, tabla.getValueAt(tabla.getSelectedRow(), 0));
    }
}
