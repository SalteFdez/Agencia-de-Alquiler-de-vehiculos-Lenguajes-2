package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;
import modelo.Agencia;
import modelo.alquiler.Contrato;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;
import servicio.ServicioAlquiler;

class ModeloTablaContratosTest {

    @Test
    void laFilaDeUnContratoMuestraNumeroClienteVehiculoFechasTarifaEstadoYTotal() {
        Agencia agencia = new Agencia();
        Cliente cliente = new Cliente("Ana", "Gomez", "30123456", LocalDate.of(1990, 5, 20),
                "3874123456", new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.of(2035, 1, 1)));
        Auto auto = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000,
                10000.0, 4, 5, TipoCaja.MANUAL);
        Contrato contrato = new Contrato(cliente, auto, new TarifaDiaria(),
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 8));
        contrato.setNumero(1);
        agencia.getContratos().add(contrato);

        ModeloTablaContratos modelo = new ModeloTablaContratos(new ServicioAlquiler(agencia));

        assertEquals(1, modelo.getRowCount());
        assertEquals(8, modelo.getColumnCount());
        assertEquals("Nº", modelo.getColumnName(0));
        assertEquals("Cliente", modelo.getColumnName(1));
        assertEquals("Vehículo", modelo.getColumnName(2));
        assertEquals("Inicio", modelo.getColumnName(3));
        assertEquals("Fin prevista", modelo.getColumnName(4));
        assertEquals("Tarifa", modelo.getColumnName(5));
        assertEquals("Estado", modelo.getColumnName(6));
        assertEquals("Total", modelo.getColumnName(7));
        assertEquals(1, modelo.getValueAt(0, 0));
        assertEquals("Ana Gomez", modelo.getValueAt(0, 1));
        assertEquals("AB123CD", modelo.getValueAt(0, 2));
        assertEquals("05/11/2026", modelo.getValueAt(0, 3));
        assertEquals("08/11/2026", modelo.getValueAt(0, 4));
        assertEquals("Diaria", modelo.getValueAt(0, 5));
        assertEquals("ACTIVO", modelo.getValueAt(0, 6));
        assertEquals(30000.0, (Double) modelo.getValueAt(0, 7), 0.001);
    }
}
