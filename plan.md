# Plan — Historia #3 (borrar al terminar la historia)

## Story
Como encargado de flota, quiero registrar una camioneta, para ofrecer camionetas en alquiler.

## Acceptance criteria
- [ ] Dado que estoy en la pestaña Flota, cuando cargo patente, marca, modelo, color, año, kilometraje, precio base diario, capacidad de carga en kg y si tiene tracción 4x4 y guardo, entonces la camioneta aparece en la lista con tipo "Camioneta", estado DISPONIBLE y precio diario igual al precio base × 1,4 (o × 1,6 si tiene tracción 4x4).
- [ ] Dado que ya existe un vehículo (auto, moto o camioneta) con una patente, o que dejo la patente vacía o cargo un kilometraje o precio negativo, cuando guardo la camioneta, entonces veo un mensaje que indica el problema y la camioneta no se registra.
- [ ] Dado que cargo una capacidad de carga igual a 0 o negativa, cuando guardo, entonces veo un mensaje que indica que la capacidad de carga debe ser mayor a 0 y la camioneta no se registra.

## Plan
- Modelo: la camioneta como subclase de vehículo, con capacidad de carga y tracción 4x4, su factor de tarifa (1,4, o 1,6 con 4x4) y la validación de su capacidad de carga.
- Servicio: sin cambios, porque ya registra cualquier vehículo con las validaciones comunes y la de patente repetida.
- Interfaz: un botón "Agregar camioneta" en la pestaña Flota que abre un formulario con el panel de datos comunes, la capacidad de carga y una casilla para la tracción 4x4.
- Al guardar, el formulario llama al servicio y actualiza la tabla; si hay un error, lo muestra en un mensaje.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- Clase del modelo: `Camioneta` en `modelo.flota`, subclase de `Vehiculo`.
- Atributos: `capacidadCargaKg: double` y `traccion4x4: boolean`.
- Métodos: `getTipo()` devuelve `"Camioneta"`; `getFactorTarifa()` (protegido); `validarDatosPropios()`.
- Constructor: los 7 datos comunes en el mismo orden que `Auto` y `Moto`, y después `capacidadCargaKg` y `traccion4x4`.
- Vista: `FormularioCamioneta` en `vista`, que contiene un `PanelDatosVehiculo`; sus campos se llaman `capacidadCargaKg` y `traccion4x4`.
- Mensaje de error: "La capacidad de carga debe ser mayor a 0".
- Si hace falta un nombre que no está en esta lista ni en `uml.md`, agregarlo primero a `uml.md`.

## Out of scope
- Filtrar la flota (#4) y dar de baja vehículos (#5).
- La licencia requerida para la camioneta (categoría B), que llega con la validación del conductor (#10).
- Editar vehículos y guardar en archivo.

## Suggestions (your call)
- Ninguna: el panel compartido ya evita repetir los campos comunes.
