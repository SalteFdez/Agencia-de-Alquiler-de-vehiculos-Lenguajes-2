# Plan — Historia #4 (borrar al terminar la historia)

## Story
Como operador, quiero filtrar la flota por tipo y estado, para encontrar rápido qué vehículo puedo alquilar.

## Acceptance criteria
- [ ] Dado que la flota tiene autos, motos y camionetas, cuando elijo un tipo en el filtro de tipo, entonces la lista muestra solo los vehículos de ese tipo; si elijo "Todos", vuelve a mostrar la flota completa.
- [ ] Dado que la flota tiene vehículos, cuando elijo un estado en el filtro de estado (solo o junto con un tipo), entonces la lista muestra solo los vehículos que cumplen todos los filtros elegidos; si ninguno los cumple, la lista queda vacía.
- [ ] Dado que hay un filtro elegido, cuando registro un vehículo nuevo, entonces la lista sigue filtrada y el vehículo nuevo aparece solo si cumple el filtro.

## Plan
- Servicio: una operación que devuelve los vehículos que cumplen el tipo y el estado pedidos; un filtro sin valor significa "todos".
- Interfaz: dos listas desplegables arriba de la tabla de la pestaña Flota, una de tipo (Todos, Auto, Moto, Camioneta) y otra de estado (Todos, DISPONIBLE, ALQUILADO).
- La tabla muestra el resultado del filtro elegido y se actualiza al cambiar cualquiera de las dos listas.
- Después de registrar un vehículo, la tabla se actualiza conservando el filtro elegido.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- Servicio: `ServicioFlota.filtrar(tipo: String, estado: EstadoVehiculo): List<Vehiculo>`. `tipo` es el valor que devuelve `getTipo()` ("Auto", "Moto", "Camioneta"); `null` en `tipo` o en `estado` significa sin filtro.
- Tabla: `ModeloTablaFlota` guarda el filtro en `filtroTipo: String` y `filtroEstado: EstadoVehiculo`, y lo cambia con `setFiltro(tipo: String, estado: EstadoVehiculo): void`, que refresca la tabla.
- Vista: las listas desplegables de `VentanaPrincipal` se llaman `comboTipo` y `comboEstado`. La opción "Todos" se traduce a `null` antes de llamar a `setFiltro()`.
- Estos nombres son nuevos: agregarlos a `uml.md` al empezar la historia. Si hace falta otro nombre que no está en esta lista ni en `uml.md`, agregarlo primero a `uml.md`.

## Out of scope
- Buscar por patente, marca o modelo (no está en el backlog).
- Ordenar la tabla por columnas.
- Dar de baja vehículos (#5).
- Hoy todos los vehículos quedan DISPONIBLE: el filtro ALQUILADO devuelve una lista vacía hasta que exista el contrato (#9).

## Suggestions (your call)
- Ninguna.
