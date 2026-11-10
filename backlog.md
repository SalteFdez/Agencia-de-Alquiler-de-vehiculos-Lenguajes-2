# Backlog — Agencia de alquiler de vehículos

Historias de usuario del proyecto final de Lenguajes 2 (Grupo 25). Cada historia hace una sola tarea. `Requiere: #N` indica que la historia no tiene sentido sin la otra. Las historias #12, #13 y #14 (recargo por atraso, tarifa semanal y tarifa por kilometraje) se sacaron del alcance el 05/11/2026; los números no se reasignan.

## Contexto

Sistema de escritorio para una agencia de alquiler de vehículos, usado por el personal de mostrador y el encargado de flota, y un kiosco de autoservicio (tipo cine o McDonald's) donde el cliente ya registrado elige un vehículo disponible y lo alquila solo.

- **Stack:** Java + Swing.
- **Decisiones caras de cambiar:** el kiosco y la parte del operario funcionan al mismo tiempo sobre el mismo `agencia.dat`. Sin login ni permisos por el momento, sin multi-organización, sin modo offline/sincronización, sin multi-idioma ni multi-moneda.

## Historias

- **#1** Como encargado de flota, quiero registrar un auto y verlo en la lista de la flota, para empezar a armar la flota.
- **#2** Como encargado de flota, quiero registrar una moto, para ofrecer motos en alquiler. Requiere: #1
- **#3** Como encargado de flota, quiero registrar una camioneta, para ofrecer camionetas en alquiler. Requiere: #1
- **#4** Como operador, quiero filtrar la flota por tipo y estado, para encontrar rápido qué vehículo puedo alquilar. Requiere: #1
- **#5** Como encargado de flota, quiero dar de baja un vehículo, para que no se ofrezca más. Requiere: #1
- **#6** Como operador, quiero registrar un cliente con su licencia y verlo en la lista de clientes, para poder alquilarle.
- **#7** Como operador, quiero buscar clientes por nombre o DNI, para encontrarlos sin recorrer toda la lista. Requiere: #6
- **#8** Como operador, quiero eliminar un cliente, para mantener la base al día. Requiere: #6
- **#9** Como operador, quiero crear un contrato con tarifa diaria para un cliente y un vehículo disponible, para entregarle el vehículo. Requiere: #1, #6
- **#10** Como operador, quiero que el sistema rechace el contrato si el cliente no está habilitado para manejar ese vehículo, para no alquilarle a quien no puede conducir. Requiere: #9
- **#11** Como operador, quiero registrar la devolución con fecha y km final, para cerrar el contrato y saber cuánto cobrar. Requiere: #9
- **#15** Como operador, quiero ver los contratos filtrados por estado, para saber qué vehículos están afuera. Requiere: #9
- **#16** Como operador, quiero que los datos se conserven al cerrar la aplicación, para no perder lo cargado. Requiere: #1
- **#17** Como cliente, quiero ver en el kiosco los vehículos disponibles como tarjetas grandes con imagen, marca, modelo y precio por día, para elegir uno de un vistazo.
- **#18** Como cliente, quiero tocar un vehículo, escribir mi DNI y la fecha de devolución, ver el total y confirmar, para alquilarlo sin pasar por el mostrador. Requiere: #17
- **#19** Como operario, quiero ver los contratos hechos en el kiosco sin cerrar mi aplicación, y que lo que yo guarde no los borre, para poder atender esas devoluciones. Requiere: #18
- **#20** Como cliente, quiero que el kiosco deje de mostrar los vehículos que el operario acaba de alquilar o dar de baja, para no elegir uno que ya no está. Requiere: #17
- **#21** Como cliente, quiero recibir un ticket en PDF al confirmar mi alquiler en el kiosco, para quedarme con el comprobante del contrato. Requiere: #18
