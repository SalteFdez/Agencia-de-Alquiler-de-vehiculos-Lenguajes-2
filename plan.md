# Plan — Historia #6 (borrar al terminar la historia)

## Story
Como operador, quiero registrar un cliente con su licencia y verlo en la lista de clientes, para poder alquilarle.

## Acceptance criteria
- [ ] Dado que estoy en la pestaña Clientes, cuando cargo nombre, apellido, DNI, fecha de nacimiento, teléfono, número de licencia, una o más categorías de licencia (A, B, C, D, E) y la fecha de vencimiento de la licencia y guardo, entonces el cliente aparece en la lista con un número de cliente asignado automáticamente, su nombre completo, DNI, teléfono, categorías y vencimiento de la licencia.
- [ ] Dado que dejo un dato obligatorio vacío, no elijo ninguna categoría, escribo una fecha que no tiene el formato dd/mm/aaaa o cargo un DNI que ya tiene otro cliente, cuando guardo, entonces veo un mensaje que indica el problema y el cliente no se registra.
- [ ] Dado que el cliente tiene menos de 18 años o su licencia ya está vencida, cuando guardo, entonces el cliente se registra igual y veo una advertencia que indica que todavía no va a poder alquilar.

## Plan
- Modelo: la categoría de licencia, la licencia de conducir (número, categorías y vencimiento, y si está vigente en una fecha) y el cliente con sus datos, su edad y su licencia.
- Agencia: guarda también la lista de clientes y el contador para numerarlos.
- Servicio de clientes: valida los datos obligatorios, que haya al menos una categoría y que el DNI no se repita; asigna el número de cliente y lo registra.
- Interfaz: una pestaña Clientes con su tabla y un botón "Agregar cliente" que abre un formulario con los datos, una casilla por categoría y las fechas en formato dd/mm/aaaa. Los errores se muestran en un mensaje y la advertencia del tercer criterio en un aviso después de registrar.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- Paquete `modelo.clientes`:
    - `CategoriaLicencia` (enum): `A`, `B`, `C`, `D`, `E`.
    - `LicenciaConducir`: atributos `numero: String`, `categorias: Set<CategoriaLicencia>`, `fechaVencimiento: LocalDate`; constructor `LicenciaConducir(numero, categorias, fechaVencimiento)`; métodos `estaVigente(fecha: LocalDate): boolean` y los getters `getNumero()`, `getCategorias()`, `getFechaVencimiento()`.
    - `Cliente`: atributos `id: int`, `nombre`, `apellido`, `dni`, `fechaNacimiento: LocalDate`, `telefono`, `licencia: LicenciaConducir` y la constante `EDAD_MINIMA: int = 18`; constructor `Cliente(nombre, apellido, dni, fechaNacimiento, telefono, licencia)` (sin `id`); métodos `getNombreCompleto(): String` (nombre y apellido separados por un espacio), `getEdad(): int` y los getters de cada atributo.
- `Cliente.setId(id: int): void` es un nombre nuevo: lo usa el servicio para asignar el número.
- `Agencia`: `clientes: List<Cliente>`, `ultimoIdCliente: int`, `getClientes(): List<Cliente>` y `siguienteIdCliente(): int` (devuelve 1, 2, 3…).
- Servicio: `ServicioClientes(agencia: Agencia)`, `registrarCliente(c: Cliente): void` (lanza `AlquilerException`) y `listarClientes(): List<Cliente>`.
- Mensajes de error: "El campo <nombre del campo> es obligatorio", "La licencia debe tener al menos una categoría", "El DNI <dni> ya está registrado" y, desde el formulario, "La fecha <campo> debe tener el formato dd/mm/aaaa".
- Advertencia: la decide el formulario después de registrar, con `getEdad() < Cliente.EDAD_MINIMA` o `!getLicencia().estaVigente(LocalDate.now())`; el texto empieza con "Cliente registrado, pero todavía no va a poder alquilar".
- Vista: `FormularioCliente` y `ModeloTablaClientes` en `vista`. En `VentanaPrincipal`: pestaña "Clientes", tabla `tablaClientes` y botón `agregarCliente`. Columnas: "Nº", "Nombre", "DNI", "Teléfono", "Categorías", "Vencimiento licencia".
- `VentanaPrincipal` pasa a recibir los dos servicios: `VentanaPrincipal(servicioFlota: ServicioFlota, servicioClientes: ServicioClientes)`. `Main` crea ambos con la misma `Agencia`.
- Los nombres nuevos (`setId()`, `FormularioCliente`, `ModeloTablaClientes`, `tablaClientes`, `agregarCliente` y el constructor nuevo de `VentanaPrincipal`) se agregan a `uml.md` al empezar la historia. Si hace falta otro nombre que no está en esta lista ni en `uml.md`, agregarlo primero a `uml.md`.

## Out of scope
- Buscar clientes (#7) y eliminarlos (#8).
- `habilitaPara()` y `puedeConducir()`: la validación del conductor llega con #10.
- Editar un cliente ya registrado.
- Validar el formato del DNI o del teléfono.
- Guardar en archivo (#16).

## Suggestions (your call)
- Ninguna.
