# Plan — Historia #7 (borrar al terminar la historia)

## Story
Como operador, quiero buscar clientes por nombre o DNI, para encontrarlos sin recorrer toda la lista.

## Acceptance criteria
- [ ] Dado que hay clientes registrados, cuando escribo parte de un nombre, un apellido o un DNI en el campo de búsqueda de la pestaña Clientes, entonces la lista muestra solo los clientes que coinciden, sin distinguir mayúsculas de minúsculas.
- [ ] Dado que escribí algo en el campo de búsqueda, cuando lo borro, entonces la lista vuelve a mostrar todos los clientes; si ningún cliente coincide, la lista queda vacía.
- [ ] Dado que hay un texto de búsqueda escrito, cuando registro un cliente nuevo, entonces la lista sigue filtrada y el cliente nuevo aparece solo si coincide con la búsqueda.

## Plan
- Servicio: una operación que devuelve los clientes cuyo nombre, apellido, nombre completo o DNI contienen el texto buscado; un texto vacío devuelve todos.
- Interfaz: un campo de texto "Buscar" arriba de la tabla de la pestaña Clientes.
- La tabla se actualiza mientras se escribe, sin botón, y conserva la búsqueda al registrar un cliente.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- Servicio: `ServicioClientes.buscarPorTexto(texto: String): List<Cliente>`. Compara sin distinguir mayúsculas e ignora espacios al principio y al final del texto; `null` o vacío devuelve todos, en el orden en que se registraron.
- Tabla: `ModeloTablaClientes` guarda la búsqueda en `filtroTexto: String` y la cambia con `setFiltro(texto: String): void`, que refresca la tabla. Siempre consulta a `buscarPorTexto()`.
- Vista: en `VentanaPrincipal`, el campo se llama `campoBusqueda` y aplica la búsqueda con cada cambio del texto.
- `filtroTexto`, `setFiltro()` de `ModeloTablaClientes` y `campoBusqueda` son nombres nuevos: agregarlos a `uml.md` al empezar la historia. Si hace falta otro nombre que no está en esta lista ni en `uml.md`, agregarlo primero a `uml.md`.

## Out of scope
- Ignorar tildes en la búsqueda (por ejemplo, que "Gomez" encuentre "Gómez").
- Buscar por teléfono o número de licencia.
- Eliminar clientes (#8).
- Ordenar la tabla por columnas.

## Suggestions (your call)
- Ninguna.
