package vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CarpetaFotosTest {

    @TempDir
    Path carpeta;

    @Test
    void buscarFotoDevuelveElArchivoConLaPatenteComoNombreSiExisteEnLaCarpeta() throws IOException {
        File foto = Files.createFile(carpeta.resolve("AB123CD.png")).toFile();
        CarpetaFotos fotos = new CarpetaFotos(carpeta.toFile());

        Optional<File> encontrada = fotos.buscarFoto("AB123CD");

        assertEquals(Optional.of(foto), encontrada);
    }

    @Test
    void buscarFotoDevuelveVacioSiNoHayFotoParaEsaPatente() {
        CarpetaFotos fotos = new CarpetaFotos(carpeta.toFile());

        assertTrue(fotos.buscarFoto("AB123CD").isEmpty());
    }

    @Test
    void buscarFotoDevuelveVacioSiLaCarpetaNoExiste() {
        CarpetaFotos fotos = new CarpetaFotos(carpeta.resolve("no-existe").toFile());

        assertTrue(fotos.buscarFoto("AB123CD").isEmpty());
    }

    @Test
    void cadaPatenteRecibeSuPropiaFoto() throws IOException {
        File primera = Files.createFile(carpeta.resolve("AB123CD.png")).toFile();
        File segunda = Files.createFile(carpeta.resolve("AC456EF.png")).toFile();
        CarpetaFotos fotos = new CarpetaFotos(carpeta.toFile());

        assertEquals(Optional.of(primera), fotos.buscarFoto("AB123CD"));
        assertEquals(Optional.of(segunda), fotos.buscarFoto("AC456EF"));
        assertTrue(fotos.buscarFoto("AD789GH").isEmpty());
    }
}
