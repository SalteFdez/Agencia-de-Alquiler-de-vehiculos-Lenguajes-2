# Agencia de alquiler de vehículos — Lenguajes 2, Grupo 25

Proyecto final integrador de Lenguajes 2 (Ingeniería en Informática): sistema de escritorio para una agencia de alquiler de vehículos, usado por el personal de mostrador y el encargado de flota.

## Stack

- Java + Swing (la cátedra exige Java y una interfaz gráfica; no se aceptan aplicaciones de consola).
- Sin base de datos: los datos viven en memoria y se guardan en un archivo con serialización (historia #16).

## Decisiones caras de cambiar

- Sin login ni permisos por el momento: cualquier persona que use la aplicación puede hacer todo.
- Sin multi-organización, sin modo offline/sincronización, sin multi-idioma ni multi-moneda.

## Reglas de negocio

- Vehículos: Auto, Moto y Camioneta heredan de `Vehiculo` (abstracta). Estados: DISPONIBLE o ALQUILADO.
- Factor de tarifa por tipo: Auto 1,0 (+0,15 si es automático); Moto 0,6; Camioneta 1,4 (+0,2 si es 4x4). Precio diario = precio base × factor.
- Tarifas (interfaz `Tarifa`, patrón Strategy): por ahora solo la diaria (precio diario × días). La tarifa semanal y la de kilometraje quedan fuera de alcance.
- Conductor habilitado: el propio cliente, con 18 años o más y licencia vigente hasta la fecha de fin prevista, con categoría A para motos o B para autos y camionetas.
- Contratos: ACTIVO o FINALIZADO. Total = costo de la tarifa. No hay recargo por atraso (fuera de alcance).

## Referencias

- Historias de usuario: `backlog.md` (los números #N son fijos; las historias #12, #13 y #14 se sacaron del alcance).
- Diagrama de clases UML: `uml.md` (fuente de verdad en texto, con el estado de cada clase). `Alquiler de autos.drawio.png` es la versión visual y puede estar desactualizada.

## Forma de trabajo

- Refinar una historia con `/story` antes de programarla y luego implementarla con `/pair`.
- Los nombres del código siguen la "Regla de nombres" de `uml.md`. Al terminar cada historia, `uml.md` se actualiza en el mismo paso.
- Los errores de validación se muestran al usuario en un cuadro de diálogo, sin cerrar la aplicación.
