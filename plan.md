# Plan — Historia #8 (borrar al terminar la historia)

## Story
Como operador, quiero eliminar un cliente, para mantener la base al día.

## Acceptance criteria
- [ ] Dado que elijo un cliente en la lista de clientes, cuando presiono "Eliminar cliente" y confirmo, entonces el cliente desaparece de la lista y ya no forma parte de la base.
- [ ] Dado que elijo un cliente y presiono "Eliminar cliente", cuando cancelo la confirmación, entonces el cliente sigue en la lista sin cambios.
- [ ] Dado que hay un texto de búsqueda escrito, cuando elimino uno de los clientes que se ven, entonces se elimina ese cliente (no otro de la lista completa) y la lista sigue filtrada con la misma búsqueda.

## Plan
- Servicio: una operación que quita un cliente de la base.
- Tabla: una forma de obtener el cliente de una fila según la búsqueda escrita.
- Interfaz: un botón "Eliminar cliente" en la pestaña Clientes, habilitado solo cuando hay un cliente elegido en la tabla. Pide confirmación mostrando el nombre completo y el DNI; si se confirma, llama al servicio y refresca la tabla conservando la búsqueda.
- Antes de empezar: el refactor de `VentanaPrincipal` tiene que estar terminado y reflejado en `uml.md`. El botón va en la clase que arme la pestaña Clientes después del refactor.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- Servicio: `ServicioClientes.eliminarCliente(c: Cliente): void`, declarado con `throws AlquilerException` para que en #9 pueda rechazar a un cliente con un contrato activo sin cambiar su firma.
- Tabla: `ModeloTablaClientes.getClienteEn(fila: int): Cliente` devuelve el cliente de esa fila según `filtroTexto`.
- Vista: el botón se llama `eliminarClienteBoton` y vive en la clase que arma la pestaña Clientes (hoy `VentanaPrincipal`; después del refactor, la que corresponda según `uml.md`). La tabla conserva el nombre `tablaClientes`.
- Mensajes: confirmación "¿Eliminar al cliente <nombre completo> (DNI <dni>)?" con título "Eliminar cliente"; si el servicio lanza una excepción, se muestra en un cuadro de diálogo con título "No se pudo eliminar el cliente".
- `getClienteEn()` y `eliminarClienteBoton` son nombres nuevos: agregarlos a `uml.md` al empezar la historia. Si hace falta otro nombre que no está en esta lista ni en `uml.md`, agregarlo primero a `uml.md`.

## Out of scope
- Impedir eliminar un cliente con un contrato activo: todavía no hay contratos; esa regla se agrega en #9, donde `eliminarCliente()` empieza a lanzar `AlquilerException`.
- Eliminar varios clientes a la vez, deshacer una eliminación o ver clientes eliminados.
- Renumerar los clientes: los números ya asignados no se reutilizan.

## Suggestions (your call)
- Ninguna.
