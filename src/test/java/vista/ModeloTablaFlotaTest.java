package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;

import excepciones.AlquilerException;
import modelo.Agencia;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import org.junit.jupiter.api.Test;
import servicio.ServicioFlota;

class ModeloTablaFlotaTest {

    @Test
    void laFilaDeUnAutoMuestraSusDatosYElPrecioDiarioYaMultiplicado() throws AlquilerException {
        ServicioFlota servicio = new ServicioFlota(new Agencia());
        servicio.registrarVehiculo(new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022,
                15000, 10000.0, 4, 5, TipoCaja.AUTOMATICA));

        ModeloTablaFlota modelo = new ModeloTablaFlota(servicio);

        assertEquals(1, modelo.getRowCount());
        assertEquals(8, modelo.getColumnCount());
        assertEquals("Patente", modelo.getColumnName(0));
        assertEquals("Tipo", modelo.getColumnName(1));
        assertEquals("Marca", modelo.getColumnName(2));
        assertEquals("Modelo", modelo.getColumnName(3));
        assertEquals("Año", modelo.getColumnName(4));
        assertEquals("Km", modelo.getColumnName(5));
        assertEquals("Estado", modelo.getColumnName(6));
        assertEquals("Precio diario", modelo.getColumnName(7));
        assertEquals("AB123CD", modelo.getValueAt(0, 0));
        assertEquals("Auto", modelo.getValueAt(0, 1));
        assertEquals("Fiat", modelo.getValueAt(0, 2));
        assertEquals("Cronos", modelo.getValueAt(0, 3));
        assertEquals(2022, modelo.getValueAt(0, 4));
        assertEquals(15000, modelo.getValueAt(0, 5));
        assertEquals("DISPONIBLE", modelo.getValueAt(0, 6));
        assertEquals(11500.0, (double) modelo.getValueAt(0, 7), 0.001);
    }
}
