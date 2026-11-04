# Plan — Historia #1 (borrar al terminar la historia)

## Story
Como encargado de flota, quiero registrar un auto y verlo en la lista de la flota, para empezar a armar la flota.

## Acceptance criteria
- [ ] Dado que estoy en la pestaña Flota, cuando cargo patente, marca, modelo, color, año, kilometraje, precio base diario, cantidad de puertas, capacidad de pasajeros y tipo de caja y guardo, entonces el auto aparece en la lista con su patente, tipo, marca, modelo, año, km, estado DISPONIBLE y precio diario (precio base × 1,0, o × 1,15 si es automático).
- [ ] Dado que ya existe un vehículo con una patente, cuando intento registrar otro auto con esa misma patente, entonces veo un mensaje que me avisa que la patente ya está registrada y la lista no cambia.
- [ ] Dado que dejo la patente vacía o cargo un kilometraje o un precio negativo, cuando guardo, entonces veo un mensaje que indica qué dato está mal y el auto no se registra.

## Plan
- Modelo: un vehículo genérico (abstracto) con los datos comunes y su estado, y el auto como subclase con sus datos propios y su factor de tarifa.
- Una agencia en memoria que guarda la lista de vehículos, y un servicio de flota que valida los datos (obligatorios, no negativos, patente única) y registra el auto.
- Ventana principal con la pestaña Flota: una tabla con los vehículos y un botón "Agregar auto".
- Formulario en un cuadro de diálogo con los campos del auto: al guardar llama al servicio y actualiza la tabla; si hay un error, lo muestra en un mensaje sin cerrar la aplicación.
- Punto de entrada que abre la ventana con la agencia vacía.

## Out of scope
- Registrar motos y camionetas (#2, #3).
- Filtrar la flota (#4) y dar de baja vehículos (#5).
- Guardar los datos en un archivo (#16): por ahora se pierden al cerrar la aplicación.
- Editar un vehículo ya registrado.
- Validar el formato de la patente y cargar datos de demostración.

## Suggestions (your call)
- Ninguna por ahora: todavía no hay código donde se repita algo.
