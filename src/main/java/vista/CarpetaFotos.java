package vista;

import java.io.File;
import java.util.Optional;

public class CarpetaFotos {

    private final File carpeta;

    public CarpetaFotos(File carpeta) {
        this.carpeta = carpeta;
    }

    public Optional<File> buscarFoto(String patente) {
        File foto = new File(carpeta, patente + ".png");
        return foto.isFile() ? Optional.of(foto) : Optional.empty();
    }
}
