# Plan — Historia #11 (borrar al terminar la historia)

## Story
Como operador, quiero registrar la devolución con fecha y km final, para cerrar el contrato y saber cuánto cobrar.

## Acceptance criteria
- [ ] Dado que elijo un contrato ACTIVO en la pestaña Contratos, cuando presiono "Registrar devolución", cargo la fecha de devolución y el km final y guardo, entonces el contrato pasa a FINALIZADO, el vehículo vuelve a DISPONIBLE con el km final en la pestaña Flota, y veo un resumen con los días cobrados, los km recorridos y el total a cobrar.
- [ ] Dado un contrato con fin previsto a 5 días, cuando se devuelve antes (por ejemplo, a los 2 días) se cobran los 5 días previstos, y cuando se devuelve después (por ejemplo, a los 7 días) se cobran los 7 días reales, sin recargo.
- [ ] Dado que la fecha de devolución está vacía, no tiene el formato dd/mm/aaaa, es anterior al inicio del contrato o posterior a hoy, o el km final es menor al km inicial, o el contrato ya está FINALIZADO, cuando guardo, entonces veo un mensaje que indica el problema y no cambian ni el contrato ni el vehículo.

## Plan
- Modelo: el contrato registra la fecha de devolución y el km final, pasa a FINALIZADO y calcula los días cobrados y los km recorridos; el vehículo actualiza su kilometraje.
- Servicio de alquiler: registra la devolución validando el estado del contrato, la fecha y el km; finaliza el contrato, actualiza el km del vehículo y lo deja DISPONIBLE.
- Interfaz: un botón "Registrar devolución" en la pestaña Contratos, habilitado con un contrato elegido, que abre un formulario con la fecha (precargada con hoy) y el km final. Al guardar muestra el resumen y refresca las tablas de contratos y de flota.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- `Contrato`:
    - Atributos nuevos `fechaDevolucion: LocalDate` y `kmFinal: int` (vacíos mientras está ACTIVO), con sus getters.
    - `finalizar(fecha: LocalDate, kmFinal: int): void`: guarda la fecha y el km y pasa a `FINALIZADO`. No valida; valida el servicio.
    - `getDias(): long`: con el contrato ACTIVO, como hasta ahora (inicio a fin previsto); FINALIZADO, el mayor entre los días previstos y los días entre `fechaInicio` y `fechaDevolucion`.
    - `getKmRecorridos(): int`: `kmFinal - kmInicial` si está FINALIZADO; 0 si está ACTIVO.
    - `calcularTotal()` pasa a `tarifa.calcular(vehiculo, getDias(), getKmRecorridos())`.
- `Vehiculo.actualizarKilometraje(km: int): void`. `kilometraje` deja de ser `final`.
- `ServicioAlquiler.registrarDevolucion(c: Contrato, fecha: LocalDate, kmFinal: int): void` (lanza `AlquilerException`), con validaciones en este orden y estos mensajes:
    1. "El contrato <numero> ya está finalizado"
    2. "La fecha de devolución no puede ser anterior al inicio del contrato"
    3. "La fecha de devolución no puede ser posterior a hoy"
    4. "El km final no puede ser menor al km inicial (<kmInicial>)"
    Si todo está bien: `c.finalizar(fecha, kmFinal)`, `vehiculo.actualizarKilometraje(kmFinal)` y `vehiculo.cambiarEstado(DISPONIBLE)`.
- Vista:
    - `ModeloTablaContratos.getContratoEn(fila: int): Contrato`.
    - `FormularioDevolucion(padre, servicioAlquiler, contrato, alRegistrar: Runnable)` con `campoFechaDevolucion: JTextField` (precargado con hoy en dd/mm/aaaa) y `campoKmFinal: JTextField`. Lee la fecha con `FormularioCliente.fecha()` y el km con `PanelDatosVehiculo.entero()`.
    - Resumen después de guardar, en un cuadro de diálogo con título "Devolución registrada": "Días cobrados: <n>", "Km recorridos: <n>" y "Total a cobrar: <total con 2 decimales>".
    - En `PanelContratos`, el botón `devolucionBoton` ("Registrar devolución"), habilitado solo con una fila elegida en `tablaContratos`. Después de registrar, refresca la tabla de contratos y la de la flota con el mismo `Runnable` que ya refresca la flota al crear un contrato.
- `getContratoEn()`, `FormularioDevolucion` y sus campos, y `devolucionBoton` son nombres nuevos: agregarlos a `uml.md` al empezar la historia. Si hace falta otro nombre, agregarlo primero a `uml.md`.

## Out of scope
- Recargo por atraso y tarifa por kilometraje: fuera de alcance.
- Registrar pagos, multas o daños.
- Editar o deshacer una devolución.
- Columnas nuevas en la tabla de contratos: el total ya se actualiza al finalizar y el resumen se ve en el cuadro de diálogo.
- Filtrar contratos por estado (#15).

## Suggestions (your call)
- En `PanelContratos`, el `Runnable` que refresca la flota se llama `alCrear`; con esta historia también se usa al registrar la devolución. Si quieren, renómbrenlo a algo como `alCambiarFlota` en un refactor aparte, actualizando `uml.md`.
