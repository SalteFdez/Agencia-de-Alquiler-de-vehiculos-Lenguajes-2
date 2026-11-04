# Plan — Historia #5 (borrar al terminar la historia)

## Story
Como encargado de flota, quiero dar de baja un vehículo, para que no se ofrezca más.

## Acceptance criteria
- [ ] Dado que elijo un vehículo en la lista de la flota, cuando presiono "Dar de baja" y confirmo, entonces el vehículo desaparece de la lista y ya no forma parte de la flota.
- [ ] Dado que elijo un vehículo y presiono "Dar de baja", cuando cancelo la confirmación, entonces el vehículo sigue en la lista sin cambios.
- [ ] Dado que el vehículo elegido está ALQUILADO, cuando intento darlo de baja, entonces veo un mensaje que indica que no se puede dar de baja un vehículo alquilado y el vehículo sigue en la lista.

## Plan
- Servicio: una operación que quita un vehículo de la flota y lo rechaza si está alquilado.
- Interfaz: un botón "Dar de baja" en la pestaña Flota, habilitado solo cuando hay un vehículo elegido en la tabla.
- Al presionarlo, se pide confirmación mostrando la patente; si se confirma, se llama al servicio y se actualiza la tabla conservando el filtro elegido; si hay un error, se muestra en un mensaje.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- Servicio: `ServicioFlota.eliminarVehiculo(v: Vehiculo): void`, que lanza `AlquilerException` si `v.getEstado()` es `ALQUILADO`.
- Mensaje de error: "No se puede dar de baja un vehículo alquilado".
- Tabla: `ModeloTablaFlota.getVehiculoEn(fila: int): Vehiculo` devuelve el vehículo de esa fila según el filtro elegido.
- Vista: en `VentanaPrincipal`, la tabla se llama `tablaFlota` y el botón `darDeBaja`.
- `getVehiculoEn()`, `tablaFlota` y `darDeBaja` son nombres nuevos: agregarlos a `uml.md` al empezar la historia. Si hace falta otro nombre que no está en esta lista ni en `uml.md`, agregarlo primero a `uml.md`.

## Out of scope
- Dar de baja varios vehículos a la vez.
- Deshacer una baja o ver los vehículos dados de baja.
- Conservar el historial de contratos del vehículo (todavía no hay contratos; llega con #9).
- Hoy ningún vehículo queda ALQUILADO: el tercer criterio solo se prueba con tests del servicio hasta que exista el contrato (#9).

## Suggestions (your call)
- Ninguna.
