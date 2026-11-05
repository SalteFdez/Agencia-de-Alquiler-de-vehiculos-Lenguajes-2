package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;

import excepciones.AlquilerException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import modelo.Agencia;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import org.junit.jupiter.api.Test;
import servicio.ServicioClientes;

class ModeloTablaClientesTest {

    @Test
    void laFilaDeUnClienteMuestraNumeroNombreDniTelefonoCategoriasYVencimiento() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(new Cliente("Ana", "Gomez", "30123456",
                LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.of(2030, 3, 15))));

        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);

        assertEquals(1, modelo.getRowCount());
        assertEquals(6, modelo.getColumnCount());
        assertEquals("Nº", modelo.getColumnName(0));
        assertEquals("Nombre", modelo.getColumnName(1));
        assertEquals("DNI", modelo.getColumnName(2));
        assertEquals("Teléfono", modelo.getColumnName(3));
        assertEquals("Categorías", modelo.getColumnName(4));
        assertEquals("Vencimiento licencia", modelo.getColumnName(5));
        assertEquals(1, modelo.getValueAt(0, 0));
        assertEquals("Ana Gomez", modelo.getValueAt(0, 1));
        assertEquals("30123456", modelo.getValueAt(0, 2));
        assertEquals("3874123456", modelo.getValueAt(0, 3));
        assertEquals("B", modelo.getValueAt(0, 4));
        assertEquals("15/03/2030", modelo.getValueAt(0, 5));
    }

    @Test
    void conVariasCategoriasLaFilaLasMuestraTodasSeparadasPorComaYEnOrden() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Set<CategoriaLicencia> desordenadas = new LinkedHashSet<>(List.of(
                CategoriaLicencia.C, CategoriaLicencia.A, CategoriaLicencia.B));
        servicio.registrarCliente(new Cliente("Ana", "Gomez", "30123456",
                LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", desordenadas, LocalDate.of(2030, 3, 15))));

        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);

        assertEquals("A, B, C", modelo.getValueAt(0, 4));
    }
}
