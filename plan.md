# Plan — Historia #16 (borrar al terminar la historia)

## Story
Como operador, quiero que los datos se conserven al cerrar la aplicación, para no perder lo cargado.

## Acceptance criteria
- [ ] Dado que registré vehículos, clientes y contratos (activos y finalizados), cuando cierro la aplicación y la vuelvo a abrir, entonces veo los mismos datos con sus estados, kilometrajes y totales, y lo que registre después sigue la numeración (si había 2 clientes y 3 contratos, el siguiente cliente es el 3 y el siguiente contrato el 4).
- [ ] Dado que es la primera vez que abro la aplicación y no hay datos guardados, cuando la abro, entonces arranca vacía y sin mensajes de error.
- [ ] Dado que el archivo de datos está dañado, cuando abro la aplicación, entonces veo un mensaje que indica que no se pudieron cargar los datos y arranca vacía; y si al cerrar no se pueden guardar, veo un mensaje con la opción de cerrar igual sin guardar o seguir en la aplicación.

## Plan
- Modelo: todas las clases que forman la agencia (vehículos, clientes, licencias, contratos y tarifas) se pueden serializar, junto con los contadores de números.
- Persistencia: una clase que guarda la agencia completa en un archivo y la vuelve a cargar, informando los errores con una excepción propia del sistema.
- Arranque: si existe el archivo, se carga la agencia desde él; si no existe, se crea vacía; si falla la carga, se avisa y se crea vacía.
- Cierre: al cerrar la ventana se guarda la agencia; si falla, se pregunta si cerrar igual sin guardar.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- Implementan `java.io.Serializable` y declaran `private static final long serialVersionUID = 1L;`: `Agencia`, `Vehiculo` (las subclases lo heredan), `Cliente`, `LicenciaConducir`, `Contrato` y `TarifaDiaria`. Los enums ya son serializables.
- `persistencia.GestorPersistencia`:
    - Atributo `archivo: File` y constructor `GestorPersistencia(archivo: File)`.
    - `existeArchivo(): boolean`.
    - `guardar(a: Agencia): void` y `cargar(): Agencia`, con `ObjectOutputStream` / `ObjectInputStream`. Ambos lanzan `AlquilerException` envolviendo el error original, con los mensajes "No se pudieron guardar los datos" y "No se pudieron cargar los datos".
- Archivo: `agencia.dat`, en la carpeta desde la que se ejecuta la aplicación (la del proyecto, con `ejecutar.sh`). Ya está en `.gitignore`.
- `Main`:
    - Crea `GestorPersistencia` con `new File("agencia.dat")` y obtiene la agencia con un método privado `cargarAgencia(gestor: GestorPersistencia): Agencia` (archivo existente: `cargar()`; sin archivo o con error: agencia nueva; con error, además, un cuadro de diálogo con título "No se pudieron cargar los datos").
    - Agrega a la ventana el cierre con guardado en un método privado `guardarAlCerrar(ventana: JFrame, gestor: GestorPersistencia, agencia: Agencia): void`. La ventana pasa a `DO_NOTHING_ON_CLOSE` y un `WindowListener` llama a `guardar()`; si falla, un diálogo Sí/No con título "No se pudieron guardar los datos" pregunta "¿Cerrar igual sin guardar?".
- `VentanaPrincipal`, los paneles y los servicios no cambian.
- `cargarAgencia()` y `guardarAlCerrar()` son nombres nuevos: agregarlos a `uml.md` al empezar la historia. Si hace falta otro nombre, agregarlo primero a `uml.md`.

## Out of scope
- Guardar automáticamente después de cada cambio o con un botón "Guardar".
- Elegir dónde se guarda el archivo o tener varios archivos de datos.
- Hacer copias de seguridad o recuperar el archivo dañado.
- Migrar archivos guardados con versiones anteriores de las clases: si el modelo cambia, se borra `agencia.dat`.
- Datos de demostración precargados.

## Suggestions (your call)
- Ninguna.
