# Plan — Historia #9 (borrar al terminar la historia)

## Story
Como operador, quiero crear un contrato con tarifa diaria para un cliente y un vehículo disponible, para entregarle el vehículo.

## Acceptance criteria
- [ ] Dado que hay clientes y vehículos DISPONIBLE, cuando en la pestaña Contratos presiono "Nuevo contrato", elijo un cliente, un vehículo (solo se ofrecen los DISPONIBLE) y una fecha de fin prevista, entonces el formulario muestra el total estimado (precio diario del vehículo × días); al guardar, el contrato aparece en la lista con su número, cliente, patente, fecha de inicio (hoy), fecha de fin prevista, tarifa "Diaria", estado ACTIVO y total, y el vehículo pasa a ALQUILADO en la pestaña Flota.
- [ ] Dado que no elijo cliente o vehículo, o la fecha de fin prevista está vacía, no tiene el formato dd/mm/aaaa o no es posterior a hoy, cuando guardo, entonces veo un mensaje que indica el problema, el contrato no se crea y el vehículo sigue DISPONIBLE.
- [ ] Dado que un cliente tiene un contrato ACTIVO, cuando intento eliminarlo desde la pestaña Clientes, entonces veo un mensaje que indica que no se puede eliminar un cliente con un contrato activo y el cliente sigue en la lista.

## Plan
- Modelo: la tarifa como interfaz con una única implementación, la diaria; el contrato con su número, cliente, vehículo, tarifa, fechas, km inicial y estado; y la agencia guarda también los contratos y su contador.
- Servicio de alquiler: crea el contrato validando la fecha de fin y que el vehículo esté disponible, le asigna número, deja el vehículo ALQUILADO y lo lista. El servicio de clientes pasa a rechazar la eliminación de un cliente con un contrato activo.
- Interfaz: una pestaña Contratos con su tabla y un botón "Nuevo contrato" que abre un formulario con una lista de clientes, una lista de vehículos disponibles, la fecha de fin prevista y el total estimado, que se recalcula al cambiar el vehículo o la fecha.
- Al crear un contrato se refrescan la tabla de contratos y la de la flota (el vehículo cambió de estado). Los errores se muestran en un mensaje.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- `modelo.tarifas`:
    - `Tarifa` (interfaz): `calcular(v: Vehiculo, dias: long, km: int): double` y `getNombre(): String`.
    - `TarifaDiaria`: `calcular()` devuelve `v.getPrecioDiario() * dias` (ignora `km`); `getNombre()` devuelve `"Diaria"`.
- `modelo.alquiler`:
    - `EstadoContrato` (enum): `ACTIVO`, `FINALIZADO`.
    - `Contrato`: atributos `numero: int`, `cliente: Cliente`, `vehiculo: Vehiculo`, `tarifa: Tarifa`, `fechaInicio: LocalDate`, `fechaFinPrevista: LocalDate`, `kmInicial: int`, `estado: EstadoContrato`. Constructor `Contrato(cliente, vehiculo, tarifa, fechaInicio, fechaFinPrevista)`: toma `kmInicial` de `vehiculo.getKilometraje()` y queda `ACTIVO`. `setNumero(numero: int): void` (lo usa solo el servicio). `getDias(): long` (días entre `fechaInicio` y `fechaFinPrevista`) y `calcularTotal(): double` (`tarifa.calcular(vehiculo, getDias(), 0)`), más los getters de cada atributo. `fechaDevolucion` y `kmFinal` llegan con #11.
- `Agencia`: `contratos: List<Contrato>`, `ultimoNroContrato: int`, `getContratos(): List<Contrato>` y `siguienteNroContrato(): int` (devuelve 1, 2, 3…).
- `Vehiculo.estaDisponible(): boolean` (estado `DISPONIBLE`).
- `excepciones.VehiculoNoDisponibleException extends AlquilerException`, mensaje "El vehículo <patente> no está disponible".
- `ServicioAlquiler(agencia: Agencia)`:
    - `crearContrato(c: Cliente, v: Vehiculo, t: Tarifa, fin: LocalDate): Contrato` (lanza `AlquilerException`): si `fin` no es posterior a hoy, "La fecha de fin prevista debe ser posterior a hoy"; si `!v.estaDisponible()`, `VehiculoNoDisponibleException`. Si todo está bien, asigna número, llama a `v.cambiarEstado(ALQUILADO)` y agrega el contrato.
    - `listarContratos(estado: EstadoContrato): List<Contrato>`; `null` devuelve todos, en el orden en que se crearon.
- `ServicioClientes` pasa a recibir la agencia como hasta ahora y `eliminarCliente()` lanza `AlquilerException` con "No se puede eliminar un cliente con un contrato activo" si hay un contrato `ACTIVO` de ese cliente en `agencia.getContratos()`.
- Vista:
    - `ModeloTablaContratos` (en `vista`), columnas "Nº", "Cliente", "Vehículo", "Inicio", "Fin prevista", "Tarifa", "Estado", "Total". "Cliente" muestra el nombre completo, "Vehículo" la patente y las fechas en dd/mm/aaaa.
    - `FormularioContrato(padre, servicioAlquiler, servicioClientes, servicioFlota, alCrear: Runnable)` con `comboCliente: JComboBox<Cliente>`, `comboVehiculo: JComboBox<Vehiculo>` (solo los `DISPONIBLE`, con `servicioFlota.filtrar(null, DISPONIBLE)`), `campoFechaFin: JTextField` y `etiquetaTotal: JLabel`. Lee la fecha con `FormularioCliente.fecha()`. Mensajes si falta elegir: "Hay que elegir un cliente" y "Hay que elegir un vehículo disponible".
    - Para mostrar los elementos de las listas, `Cliente.toString()` devuelve "<nombre completo> (DNI <dni>)" y `Vehiculo.toString()` devuelve "<patente> - <marca> <modelo>".
    - `VentanaPrincipal(servicioFlota, servicioClientes, servicioAlquiler)` agrega la pestaña "Contratos" con `tablaContratos`, el botón `nuevoContrato` y el método privado `crearPestaniaContratos()`. `Main` crea los tres servicios con la misma `Agencia`.
- Todos estos nombres que todavía no están en `uml.md` (`setNumero()`, `toString()`, `VehiculoNoDisponibleException` con su mensaje, `ModeloTablaContratos`, `FormularioContrato` y sus campos, `tablaContratos`, `nuevoContrato`, `crearPestaniaContratos()` y los constructores nuevos) se agregan a `uml.md` al empezar la historia. Si hace falta otro nombre, agregarlo primero a `uml.md`.

## Out of scope
- Validar que el cliente pueda manejar el vehículo (edad, licencia vigente y categoría): es la historia #10. Por ahora cualquier cliente puede alquilar.
- Registrar la devolución (#11) y filtrar contratos por estado (#15).
- Tarifa semanal, tarifa por kilometraje y recargo por atraso: fuera de alcance.
- Elegir una fecha de inicio distinta de hoy (reservas a futuro).
- Editar o cancelar un contrato.

## Suggestions (your call)
- `VentanaPrincipal` ya tiene 169 líneas y esta historia le suma una tercera pestaña. Antes de empezar se puede extraer cada pestaña a su propia clase (`PanelFlota`, `PanelClientes`, `PanelContratos`), como refactor aparte y con los tests en verde. Si lo hacen, el plan sigue igual cambiando solo dónde viven `tablaContratos`, `nuevoContrato` y `crearPestaniaContratos()`, y hay que reflejarlo primero en `uml.md`.
