package vista;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.flota.Vehiculo;
import servicio.ServicioFlota;

public class ModeloTablaFlota extends AbstractTableModel {

    private static final String[] COLUMNAS = {
        "Patente", "Tipo", "Marca", "Modelo", "Año", "Km", "Estado", "Precio diario"
    };

    private final ServicioFlota servicio;

    public ModeloTablaFlota(ServicioFlota servicio) {
        this.servicio = servicio;
    }

    @Override
    public int getRowCount() {
        return servicio.listarVehiculos().size();
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
        List<Vehiculo> vehiculos = servicio.listarVehiculos();
        Vehiculo v = vehiculos.get(fila);
        switch (columna) {
            case 0: return v.getPatente();
            case 1: return v.getTipo();
            case 2: return v.getMarca();
            case 3: return v.getModelo();
            case 4: return v.getAnio();
            case 5: return v.getKilometraje();
            case 6: return v.getEstado().name();
            default: return v.getPrecioDiario();
        }
    }
}
