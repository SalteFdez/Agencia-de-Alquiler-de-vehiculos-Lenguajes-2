package persistencia;

import excepciones.AlquilerException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
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
        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(archivo.toPath()))) {
            salida.writeObject(a);
        } catch (IOException e) {
            throw new AlquilerException("No se pudieron guardar los datos");
        }
    }

    public Agencia cargar() throws AlquilerException {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(archivo.toPath()))) {
            return (Agencia) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AlquilerException("No se pudieron cargar los datos");
        }
    }
}
