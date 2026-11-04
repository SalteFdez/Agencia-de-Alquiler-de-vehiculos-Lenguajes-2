# Backlog — Agencia de alquiler de vehículos

Historias de usuario del proyecto final de Lenguajes 2 (Grupo 25). Cada historia hace una sola tarea. `Requiere: #N` indica que la historia no tiene sentido sin la otra.

## Contexto

Sistema de escritorio para una agencia de alquiler de vehículos, usado por el personal de mostrador y el encargado de flota.

- **Stack:** Java + Swing.
- **Decisiones caras de cambiar:** ninguna. Sin login ni permisos por el momento, sin multi-organización, sin modo offline/sincronización, sin multi-idioma ni multi-moneda.

## Historias

1. Como encargado de flota, quiero registrar un auto y verlo en la lista de la flota, para empezar a armar la flota.
2. Como encargado de flota, quiero registrar una moto, para ofrecer motos en alquiler. Requiere: #1
3. Como encargado de flota, quiero registrar una camioneta, para ofrecer camionetas en alquiler. Requiere: #1
4. Como operador, quiero filtrar la flota por tipo y estado, para encontrar rápido qué vehículo puedo alquilar. Requiere: #1
5. Como encargado de flota, quiero dar de baja un vehículo, para que no se ofrezca más. Requiere: #1
6. Como operador, quiero registrar un cliente con su licencia y verlo en la lista de clientes, para poder alquilarle.
7. Como operador, quiero buscar clientes por nombre o DNI, para encontrarlos sin recorrer toda la lista. Requiere: #6
8. Como operador, quiero eliminar un cliente, para mantener la base al día. Requiere: #6
9. Como operador, quiero crear un contrato con tarifa diaria para un cliente y un vehículo disponible, para entregarle el vehículo. Requiere: #1, #6
10. Como operador, quiero que el sistema rechace el contrato si el cliente no está habilitado para manejar ese vehículo, para no alquilarle a quien no puede conducir. Requiere: #9
11. Como operador, quiero registrar la devolución con fecha y km final, para cerrar el contrato y saber cuánto cobrar. Requiere: #9
12. Como operador, quiero que se sume un recargo cuando el vehículo se devuelve tarde, para cobrar la demora. Requiere: #11
13. Como operador, quiero ofrecer la tarifa semanal, para dar descuento en alquileres largos. Requiere: #9
14. Como operador, quiero ofrecer la tarifa por kilometraje, para que los clientes que manejan poco paguen menos. Requiere: #11
15. Como operador, quiero ver los contratos filtrados por estado, para saber qué vehículos están afuera. Requiere: #9
16. Como operador, quiero que los datos se conserven al cerrar la aplicación, para no perder lo cargado. Requiere: #1
