package vista;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.alquiler.Contrato;
import servicio.ServicioAlquiler;

public class ModeloTablaContratos extends AbstractTableModel {

    private static final String[] COLUMNAS = {
        "Nº", "Cliente", "Vehículo", "Inicio", "Fin prevista", "Tarifa", "Estado", "Total"
    };

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ServicioAlquiler servicio;

    public ModeloTablaContratos(ServicioAlquiler servicio) {
        this.servicio = servicio;
    }

    @Override
    public int getRowCount() {
        return servicio.listarContratos(null).size();
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
        List<Contrato> contratos = servicio.listarContratos(null);
        Contrato c = contratos.get(fila);
        switch (columna) {
            case 0: return c.getNumero();
            case 1: return c.getCliente().getNombreCompleto();
            case 2: return c.getVehiculo().getPatente();
            case 3: return c.getFechaInicio().format(FORMATO_FECHA);
            case 4: return c.getFechaFinPrevista().format(FORMATO_FECHA);
            case 5: return c.getTarifa().getNombre();
            case 6: return c.getEstado().name();
            default: return c.calcularTotal();
        }
    }
}
