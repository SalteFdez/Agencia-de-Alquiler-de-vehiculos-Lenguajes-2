package vista;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import modelo.clientes.Cliente;
import servicio.ServicioClientes;

public class ModeloTablaClientes extends AbstractTableModel {

    private static final String[] COLUMNAS = {
        "Nº", "Nombre", "DNI", "Teléfono", "Categorías", "Vencimiento licencia"
    };

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ServicioClientes servicio;
    private String filtroTexto;

    public ModeloTablaClientes(ServicioClientes servicio) {
        this.servicio = servicio;
    }

    public void setFiltro(String texto) {
        this.filtroTexto = texto;
        fireTableDataChanged();
    }

    public Cliente getClienteEn(int fila) {
        return servicio.buscarPorTexto(filtroTexto).get(fila);
    }

    @Override
    public int getRowCount() {
        return servicio.buscarPorTexto(filtroTexto).size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        List<Cliente> clientes = servicio.buscarPorTexto(filtroTexto);
        Cliente c = clientes.get(fila);
        switch (columna) {
            case 0: return c.getId();
            case 1: return c.getNombreCompleto();
            case 2: return c.getDni();
            case 3: return c.getTelefono();
            case 4: return c.getLicencia().getCategorias().stream()
                    .sorted().map(Enum::name).collect(Collectors.joining(", "));
            default: return c.getLicencia().getFechaVencimiento().format(FORMATO_FECHA);
        }
    }
}
