# Plan — Historia #2 (borrar al terminar la historia)

## Story
Como encargado de flota, quiero registrar una moto, para ofrecer motos en alquiler.

## Acceptance criteria
- [ ] Dado que estoy en la pestaña Flota, cuando cargo patente, marca, modelo, color, año, kilometraje, precio base diario, cilindrada y si incluye casco y guardo, entonces la moto aparece en la lista con tipo "Moto", estado DISPONIBLE y precio diario igual al precio base × 0,6.
- [ ] Dado que ya existe un vehículo (auto o moto) con una patente, o que dejo la patente vacía o cargo un kilometraje o precio negativo, cuando guardo la moto, entonces veo un mensaje que indica el problema y la moto no se registra.
- [ ] Dado que cargo una cilindrada igual a 0 o negativa, cuando guardo, entonces veo un mensaje que indica que la cilindrada debe ser mayor a 0 y la moto no se registra.

## Plan
- Modelo: la moto como subclase de vehículo, con cilindrada, si incluye casco, su factor de tarifa de 0,6 y la validación de su cilindrada.
- Servicio: sin cambios, porque ya registra cualquier vehículo con las validaciones comunes y la de patente repetida.
- Interfaz: un botón "Agregar moto" en la pestaña Flota que abre un formulario con los datos comunes, la cilindrada y una casilla para el casco.
- Al guardar, el formulario llama al servicio y actualiza la tabla; si hay un error, lo muestra en un mensaje.
- Actualizar `uml.md`: la moto pasa a implementada.

## Out of scope
- Registrar camionetas (#3).
- Que el factor de tarifa dependa de la cilindrada (la regla es 0,6 fijo).
- La licencia requerida para la moto (categoría A), que llega con la validación del conductor (#10).
- Editar vehículos y guardar en archivo.

## Suggestions (your call)
- El formulario del auto ya tiene los 7 campos comunes, y el de la moto los repetiría (y el de la camioneta en #3 otra vez). Dos opciones: extraer esos campos a un panel compartido que reutilicen los formularios, o tener un único botón "Agregar vehículo" con un selector de tipo que muestra los campos propios. La primera cambia menos lo que ya está hecho; la segunda deja la pestaña con un solo botón.
