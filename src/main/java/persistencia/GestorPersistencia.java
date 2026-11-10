package persistencia;

import excepciones.AlquilerException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import modelo.Agencia;

public class GestorPersistencia {

    private final File archivo;

    public GestorPersistencia(File archivo) {
        this.archivo = archivo;
    }

    public boolean existeArchivo() {
        return archivo.exists();
    }

    public void guardar(Agencia a) throws AlquilerException {
        Path temporal = archivo.toPath().resolveSibling(archivo.getName() + ".tmp");
        try {
            try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(temporal))) {
                salida.writeObject(a);
            }
            Files.move(temporal, archivo.toPath(), StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            try {
                Files.deleteIfExists(temporal);
            } catch (IOException ignorada) {
                // si no se puede borrar el temporal, igual se informa el error original
            }
            throw new AlquilerException("No se pudieron guardar los datos", e);
        }
    }

    public Agencia cargar() throws AlquilerException {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(archivo.toPath()))) {
            return (Agencia) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AlquilerException("No se pudieron cargar los datos", e);
        }
    }
}
