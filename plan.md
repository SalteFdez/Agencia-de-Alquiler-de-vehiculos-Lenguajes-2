# Plan — Historia #15 (borrar al terminar la historia)

## Story
Como operador, quiero ver los contratos filtrados por estado, para saber qué vehículos están afuera.

## Acceptance criteria
- [ ] Dado que hay contratos ACTIVO y FINALIZADO, cuando elijo un estado en el filtro de la pestaña Contratos, entonces la lista muestra solo los contratos con ese estado; si elijo "Todos", vuelve a mostrar todos. Con ACTIVO veo qué vehículos están alquilados y a qué cliente.
- [ ] Dado que hay un estado elegido en el filtro, cuando creo un contrato o registro una devolución, entonces la lista sigue filtrada: un contrato nuevo aparece solo si el filtro es "Todos" o ACTIVO, y un contrato recién devuelto desaparece del filtro ACTIVO.
- [ ] Dado que la lista está filtrada, cuando elijo un contrato y presiono "Registrar devolución", entonces se abre la devolución de ese contrato (no la de otro de la lista completa).

## Plan
- Servicio: sin cambios; la operación que lista contratos por estado ya existe.
- Tabla: guarda el estado elegido y muestra solo los contratos con ese estado; la fila elegida se resuelve según ese filtro.
- Interfaz: una lista desplegable "Estado" (Todos, ACTIVO, FINALIZADO) arriba de la tabla de la pestaña Contratos, que refresca la tabla al cambiar.
- Después de crear un contrato o registrar una devolución, la tabla se refresca conservando el filtro.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- Servicio: `ServicioAlquiler.listarContratos(estado: EstadoContrato): List<Contrato>`, ya existente; `null` devuelve todos.
- Tabla: `ModeloTablaContratos` guarda el filtro en `filtroEstado: EstadoContrato` y lo cambia con `setFiltro(estado: EstadoContrato): void`, que refresca la tabla. `getRowCount()`, `getValueAt()` y `getContratoEn()` consultan siempre `listarContratos(filtroEstado)`.
- Vista: en `PanelContratos`, la lista desplegable se llama `comboEstado` (`JComboBox<String>` con "Todos", "ACTIVO", "FINALIZADO"). "Todos" se traduce a `null` antes de llamar a `setFiltro()`, igual que en `PanelFlota`.
- `filtroEstado`, `setFiltro()` de `ModeloTablaContratos` y `comboEstado` de `PanelContratos` son nombres nuevos: agregarlos a `uml.md` al empezar la historia. Si hace falta otro nombre, agregarlo primero a `uml.md`.

## Out of scope
- Buscar contratos por cliente o patente.
- Destacar contratos vencidos (activos con fin previsto anterior a hoy).
- Ordenar la tabla por columnas.
- Guardar en archivo (#16).

## Suggestions (your call)
- `PanelFlota` y `PanelContratos` van a traducir "Todos" a `null` de la misma forma. Si molesta la repetición, se puede extraer en un refactor aparte; con dos usos todavía no es necesario.
