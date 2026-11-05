package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;

import excepciones.AlquilerException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import modelo.Agencia;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import javax.swing.event.TableModelEvent;
import org.junit.jupiter.api.Test;
import servicio.ServicioClientes;

class ModeloTablaClientesTest {

    private static Cliente cliente(String nombre, String apellido, String dni) {
        return new Cliente(nombre, apellido, dni, LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B),
                        LocalDate.now().plusYears(3)));
    }

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

    @Test
    void conUnaBusquedaLaTablaMuestraSoloLasFilasQueCoinciden() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(cliente("Ana", "Gomez", "30123456"));
        servicio.registrarCliente(cliente("Luis", "Perez", "27987654"));
        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);

        modelo.setFiltro("lu");

        assertEquals(1, modelo.getRowCount());
        assertEquals("Luis Perez", modelo.getValueAt(0, 1));
    }

    @Test
    void borrarLaBusquedaVuelveAMostrarTodosLosClientes() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(cliente("Ana", "Gomez", "30123456"));
        servicio.registrarCliente(cliente("Luis", "Perez", "27987654"));
        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);
        modelo.setFiltro("lu");

        modelo.setFiltro("");

        assertEquals(2, modelo.getRowCount());
    }

    @Test
    void cambiarLaBusquedaAvisaALaTablaQueLosDatosCambiaron() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(cliente("Ana", "Gomez", "30123456"));
        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);
        List<TableModelEvent> avisos = new ArrayList<>();
        modelo.addTableModelListener(avisos::add);

        modelo.setFiltro("ana");

        assertEquals(1, avisos.size());
        assertEquals(TableModelEvent.UPDATE, avisos.get(0).getType());
    }

    @Test
    void registrarUnClienteQueNoCoincideConLaBusquedaNoCambiaLaTabla() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(cliente("Luis", "Perez", "27987654"));
        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);
        modelo.setFiltro("lu");

        servicio.registrarCliente(cliente("Ana", "Gomez", "30123456"));

        assertEquals(1, modelo.getRowCount());
        assertEquals("Luis Perez", modelo.getValueAt(0, 1));
    }

    @Test
    void registrarUnClienteQueCoincideConLaBusquedaApareceEnLaTabla() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(cliente("Luis", "Perez", "27987654"));
        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);
        modelo.setFiltro("lu");

        servicio.registrarCliente(cliente("Lucía", "Díaz", "30123456"));

        assertEquals(2, modelo.getRowCount());
        assertEquals("Lucía Díaz", modelo.getValueAt(1, 1));
    }

    @Test
    void getClienteEnDevuelveElClienteDeEsaFila() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        Cliente ana = cliente("Ana", "Gomez", "30123456");
        Cliente luis = cliente("Luis", "Perez", "27987654");
        servicio.registrarCliente(ana);
        servicio.registrarCliente(luis);
        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);

        assertEquals(luis, modelo.getClienteEn(1));
    }

    @Test
    void conUnaBusquedaGetClienteEnDevuelveElClienteQueSeVeEnEsaFila() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(cliente("Ana", "Gomez", "30123456"));
        Cliente luis = cliente("Luis", "Perez", "27987654");
        servicio.registrarCliente(luis);
        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);
        modelo.setFiltro("lu");

        assertEquals(luis, modelo.getClienteEn(0));
    }

    @Test
    void eliminarUnClienteConUnaBusquedaDejaLaTablaFiltradaSinElEliminado() throws AlquilerException {
        ServicioClientes servicio = new ServicioClientes(new Agencia());
        servicio.registrarCliente(cliente("Ana", "Gomez", "30123456"));
        servicio.registrarCliente(cliente("Luis", "Perez", "27987654"));
        servicio.registrarCliente(cliente("Lucía", "Díaz", "25111222"));
        ModeloTablaClientes modelo = new ModeloTablaClientes(servicio);
        modelo.setFiltro("lu");

        servicio.eliminarCliente(modelo.getClienteEn(0));

        assertEquals(1, modelo.getRowCount());
        assertEquals("Lucía Díaz", modelo.getValueAt(0, 1));
    }
}
