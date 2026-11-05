# Plan — Historia #10 (borrar al terminar la historia)

## Story
Como operador, quiero que el sistema rechace el contrato si el cliente no está habilitado para manejar ese vehículo, para no alquilarle a quien no puede conducir.

## Acceptance criteria
- [ ] Dado que el cliente elegido tiene menos de 18 años, cuando guardo el contrato, entonces veo un mensaje que indica que no está habilitado porque es menor de 18 años, el contrato no se crea y el vehículo sigue DISPONIBLE.
- [ ] Dado que la licencia del cliente no tiene la categoría que pide el vehículo (A para una moto; B para un auto o una camioneta), cuando guardo el contrato, entonces veo un mensaje que indica la categoría que le falta, el contrato no se crea y el vehículo sigue DISPONIBLE.
- [ ] Dado que la licencia del cliente vence antes de la fecha de fin prevista, cuando guardo el contrato, entonces veo un mensaje que indica que su licencia no está vigente hasta esa fecha, el contrato no se crea y el vehículo sigue DISPONIBLE. Un cliente mayor de edad, con la categoría correcta y la licencia vigente hasta el fin del contrato lo sigue pudiendo alquilar como hasta ahora.

## Plan
- Modelo de flota: cada tipo de vehículo indica la categoría de licencia que requiere (moto A; auto y camioneta B).
- Modelo de clientes: la licencia responde si habilita una categoría en una fecha (vigente en esa fecha y con esa categoría), y el cliente responde si puede conducir un vehículo hasta una fecha (18 años o más y licencia que lo habilita hasta esa fecha).
- Servicio de alquiler: antes de crear el contrato, si el cliente no puede conducir el vehículo, lo rechaza con una excepción propia cuyo mensaje dice el motivo.
- Interfaz: sin cambios; el formulario de contrato ya muestra en un mensaje cualquier error del servicio.
- Al terminar: comparar los nombres del código con `uml.md` y actualizarlo (diagrama, tabla de estado y notas).

## Nombres obligatorios (según `uml.md`)
- `Vehiculo.getLicenciaRequerida(): CategoriaLicencia` (abstracto). `Auto` y `Camioneta` devuelven `CategoriaLicencia.B`; `Moto` devuelve `CategoriaLicencia.A`.
- `LicenciaConducir.habilitaPara(c: CategoriaLicencia, fecha: LocalDate): boolean`: `estaVigente(fecha)` y `categorias.contains(c)`.
- `Cliente.puedeConducir(v: Vehiculo, hasta: LocalDate): boolean`: `getEdad() >= EDAD_MINIMA` y `licencia.habilitaPara(v.getLicenciaRequerida(), hasta)`. La edad se cuenta a hoy, como en `getEdad()`.
- `excepciones.ConductorNoHabilitadoException extends AlquilerException`, con constructor `ConductorNoHabilitadoException(motivo: String)` y mensaje "El cliente no está habilitado para conducir este vehículo: <motivo>".
- `ServicioAlquiler.crearContrato()` valida en este orden: fecha de fin, vehículo disponible y conductor. Si `!c.puedeConducir(v, fin)`, lanza `ConductorNoHabilitadoException` con el primer motivo que corresponda, en este orden:
    1. "es menor de 18 años"
    2. "su licencia no tiene la categoría <A o B>"
    3. "su licencia no está vigente hasta el <fin en dd/mm/aaaa>"
- Si para armar el motivo hace falta un método nuevo, agregarlo primero a `uml.md`.

## Out of scope
- Filtrar en el formulario los clientes que no pueden conducir el vehículo elegido: el rechazo ocurre al guardar.
- Conductores distintos del cliente (no hay clientes empresa).
- Equivalencias entre categorías (por ejemplo, que la C también habilite a manejar un auto): solo cuenta la categoría exacta.
- Registrar la devolución (#11).

## Suggestions (your call)
- Ninguna.
