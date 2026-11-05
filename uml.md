# Diagrama de clases UML — Agencia de alquiler de vehículos

Fuente de verdad del diseño, en texto. Los nombres de clases, atributos y métodos del código deben coincidir con este archivo. Si el diseño cambia durante una historia, actualizar este archivo en el mismo paso.

## Regla de nombres

- Todo nombre de clase, atributo, método o parámetro del modelo y los servicios se escribe **exactamente** como figura en este archivo.
- Lo mismo vale para la vista cuando el dato es el mismo: un campo de formulario o un getter que representa un atributo del modelo usa el nombre de ese atributo (por ejemplo `precioBaseDiario` y `getPrecioBaseDiario()`, no `precioBase`).
- Si hace falta un nombre que no está acá, primero se agrega a este archivo y después se usa en el código.
- Antes de dar por terminada una historia, comparar los nombres del código con este archivo y actualizar el diagrama, la tabla de estado y las notas.

Notación (Mermaid): `-` privado, `+` público, `#` protegido, `*` abstracto, `$` static. `<|--` herencia, `<|..` implementa, `*--` composición, `o--` agregación, `-->` asociación, `..>` dependencia.

Última actualización: historia #10 terminada (rechazar el contrato si el cliente no está habilitado).

## Diagrama

```mermaid
classDiagram
    direction TB

    namespace vista {
        class Main {
            +main(args: String[])$ void
        }
        class VentanaPrincipal {
            +VentanaPrincipal(servicioFlota: ServicioFlota, servicioClientes: ServicioClientes, servicioAlquiler: ServicioAlquiler)
        }
        class PanelFlota {
            -TODOS: String$
            -padre: Frame
            -servicio: ServicioFlota
            -modelo: ModeloTablaFlota
            -comboTipo: JComboBox~String~
            -comboEstado: JComboBox~String~
            -tablaFlota: JTable
            -darDeBaja: JButton
            +PanelFlota(padre: Frame, servicio: ServicioFlota)
            +refrescar() void
            -darDeBajaElegido() void
        }
        class PanelClientes {
            -padre: Frame
            -servicio: ServicioClientes
            -modelo: ModeloTablaClientes
            -campoBusqueda: JTextField
            -tablaClientes: JTable
            -agregarCliente: JButton
            -eliminarClienteBoton: JButton
            +PanelClientes(padre: Frame, servicio: ServicioClientes)
            -alCambiarElTexto(campo: JTextField, accion: Runnable) void
            -eliminarClienteElegido() void
        }
        class PanelContratos {
            -padre: Frame
            -servicioAlquiler: ServicioAlquiler
            -servicioClientes: ServicioClientes
            -servicioFlota: ServicioFlota
            -alCrear: Runnable
            -modelo: ModeloTablaContratos
            -tablaContratos: JTable
            -nuevoContrato: JButton
            +PanelContratos(padre: Frame, servicioAlquiler: ServicioAlquiler, servicioClientes: ServicioClientes, servicioFlota: ServicioFlota, alCrear: Runnable)
        }
        class FormularioContrato {
            -SIN_TOTAL: String$
            -servicioAlquiler: ServicioAlquiler
            -alCrear: Runnable
            -comboCliente: JComboBox~Cliente~
            -comboVehiculo: JComboBox~Vehiculo~
            -campoFechaFin: JTextField
            -etiquetaTotal: JLabel
            +FormularioContrato(padre: Frame, servicioAlquiler: ServicioAlquiler, servicioClientes: ServicioClientes, servicioFlota: ServicioFlota, alCrear: Runnable)
            -guardar() void
            -actualizarTotal() void
        }
        class FormularioAuto {
            -servicio: ServicioFlota
            -alRegistrar: Runnable
            +FormularioAuto(padre: Frame, servicio: ServicioFlota, alRegistrar: Runnable)
            -guardar() void
        }
        class FormularioMoto {
            -servicio: ServicioFlota
            -alRegistrar: Runnable
            +FormularioMoto(padre: Frame, servicio: ServicioFlota, alRegistrar: Runnable)
            -guardar() void
        }
        class FormularioCamioneta {
            -servicio: ServicioFlota
            -alRegistrar: Runnable
            -capacidadCargaKg: JTextField
            -traccion4x4: JCheckBox
            +FormularioCamioneta(padre: Frame, servicio: ServicioFlota, alRegistrar: Runnable)
            -guardar() void
        }
        class FormularioCliente {
            -FORMATO_FECHA: DateTimeFormatter$
            -servicio: ServicioClientes
            -alRegistrar: Runnable
            -nombre: JTextField
            -apellido: JTextField
            -dni: JTextField
            -fechaNacimiento: JTextField
            -telefono: JTextField
            -numero: JTextField
            -fechaVencimiento: JTextField
            -categorias: Map~CategoriaLicencia, JCheckBox~
            +FormularioCliente(padre: Frame, servicio: ServicioClientes, alRegistrar: Runnable)
            -agregar(panel: JPanel, etiqueta: String, campo: Component)$ void
            -guardar() void
            -avisarSiNoPuedeAlquilar(cliente: Cliente) void
            ~fecha(etiqueta: String, campo: JTextField)$ LocalDate
        }
        class PanelDatosVehiculo {
            -patente: JTextField
            -marca: JTextField
            -modelo: JTextField
            -color: JTextField
            -anio: JTextField
            -kilometraje: JTextField
            -precioBaseDiario: JTextField
            +agregar(etiqueta: String, campo: Component) void
            +getPatente() String
            +getMarca() String
            +getModelo() String
            +getColor() String
            +getAnio() int
            +getKilometraje() int
            +getPrecioBaseDiario() double
            ~entero(etiqueta: String, campo: JTextField)$ int
            ~decimal(etiqueta: String, campo: JTextField)$ double
        }
        class ModeloTablaFlota {
            -COLUMNAS: String[]$
            -servicio: ServicioFlota
            -filtroTipo: String
            -filtroEstado: EstadoVehiculo
            +setFiltro(tipo: String, estado: EstadoVehiculo) void
            +getVehiculoEn(fila: int) Vehiculo
            +getRowCount() int
            +getColumnCount() int
            +getColumnName(columna: int) String
            +getValueAt(fila: int, columna: int) Object
        }
        class ModeloTablaClientes {
            -COLUMNAS: String[]$
            -FORMATO_FECHA: DateTimeFormatter$
            -servicio: ServicioClientes
            -filtroTexto: String
            +setFiltro(texto: String) void
            +getClienteEn(fila: int) Cliente
            +getRowCount() int
            +getColumnCount() int
            +getColumnName(columna: int) String
            +getValueAt(fila: int, columna: int) Object
        }
        class ModeloTablaContratos {
            -COLUMNAS: String[]$
            -FORMATO_FECHA: DateTimeFormatter$
            -servicio: ServicioAlquiler
            +ModeloTablaContratos(servicio: ServicioAlquiler)
            +getRowCount() int
            +getColumnCount() int
            +getColumnName(columna: int) String
            +getValueAt(fila: int, columna: int) Object
        }
    }

    namespace servicio {
        class ServicioFlota {
            -agencia: Agencia
            +registrarVehiculo(v: Vehiculo) void
            +listarVehiculos() List~Vehiculo~
            +eliminarVehiculo(v: Vehiculo) void
            +filtrar(tipo: String, estado: EstadoVehiculo) List~Vehiculo~
        }
        class ServicioClientes {
            -agencia: Agencia
            +registrarCliente(c: Cliente) void
            -exigir(valor: String, campo: String) void
            +eliminarCliente(c: Cliente) void
            +buscarPorTexto(texto: String) List~Cliente~
            +listarClientes() List~Cliente~
        }
        class ServicioAlquiler {
            -FORMATO_FECHA: DateTimeFormatter$
            -agencia: Agencia
            +ServicioAlquiler(agencia: Agencia)
            +crearContrato(c: Cliente, v: Vehiculo, t: Tarifa, fin: LocalDate) Contrato
            +registrarDevolucion(c: Contrato, fecha: LocalDate, kmFinal: int) void
            -motivoDeRechazo(c: Cliente, v: Vehiculo, fin: LocalDate) String
            +listarContratos(estado: EstadoContrato) List~Contrato~
        }
    }

    namespace persistencia {
        class GestorPersistencia {
            -archivo: File
            +guardar(a: Agencia) void
            +cargar() Agencia
            +existeArchivo() boolean
        }
    }

    namespace modelo {
        class Agencia {
            -vehiculos: List~Vehiculo~
            -clientes: List~Cliente~
            -contratos: List~Contrato~
            -ultimoIdCliente: int
            -ultimoNroContrato: int
            +getVehiculos() List~Vehiculo~
            +getClientes() List~Cliente~
            +getContratos() List~Contrato~
            +siguienteIdCliente() int
            +siguienteNroContrato() int
        }
    }

    namespace flota {
        class Vehiculo {
            <<abstract>>
            -patente: String
            -marca: String
            -modelo: String
            -color: String
            -anio: int
            -kilometraje: int
            -precioBaseDiario: double
            -estado: EstadoVehiculo
            +getTipo()* String
            #getFactorTarifa()* double
            +getPrecioDiario() double
            +validarDatosPropios() void
            +getLicenciaRequerida()* CategoriaLicencia
            +estaDisponible() boolean
            +toString() String
            +cambiarEstado(e: EstadoVehiculo) void
            +actualizarKilometraje(km: int) void
        }
        class Auto {
            -cantidadPuertas: int
            -capacidadPasajeros: int
            -tipoCaja: TipoCaja
            +getTipo() String
            #getFactorTarifa() double
            +validarDatosPropios() void
            +getLicenciaRequerida() CategoriaLicencia
        }
        class Moto {
            -cilindrada: int
            -incluyeCasco: boolean
            +getTipo() String
            #getFactorTarifa() double
            +validarDatosPropios() void
            +getLicenciaRequerida() CategoriaLicencia
        }
        class Camioneta {
            -capacidadCargaKg: double
            -traccion4x4: boolean
            +getTipo() String
            #getFactorTarifa() double
            +validarDatosPropios() void
            +getLicenciaRequerida() CategoriaLicencia
        }
        class EstadoVehiculo {
            <<enumeration>>
            DISPONIBLE
            ALQUILADO
        }
        class TipoCaja {
            <<enumeration>>
            MANUAL
            AUTOMATICA
        }
    }

    namespace clientes {
        class Cliente {
            -id: int
            -nombre: String
            -apellido: String
            -dni: String
            -fechaNacimiento: LocalDate
            -telefono: String
            -licencia: LicenciaConducir
            +EDAD_MINIMA: int = 18$
            +setId(id: int) void
            +getNombreCompleto() String
            +getEdad() int
            +toString() String
            +puedeConducir(v: Vehiculo, hasta: LocalDate) boolean
        }
        class LicenciaConducir {
            -numero: String
            -categorias: Set~CategoriaLicencia~
            -fechaVencimiento: LocalDate
            +estaVigente(fecha: LocalDate) boolean
            +habilitaPara(c: CategoriaLicencia, fecha: LocalDate) boolean
        }
        class CategoriaLicencia {
            <<enumeration>>
            A
            B
            C
            D
            E
        }
    }

    namespace alquiler {
        class Contrato {
            -numero: int
            -cliente: Cliente
            -vehiculo: Vehiculo
            -tarifa: Tarifa
            -fechaInicio: LocalDate
            -fechaFinPrevista: LocalDate
            -fechaDevolucion: LocalDate
            -kmInicial: int
            -kmFinal: int
            -estado: EstadoContrato
            +Contrato(cliente: Cliente, vehiculo: Vehiculo, tarifa: Tarifa, fechaInicio: LocalDate, fechaFinPrevista: LocalDate)
            +setNumero(numero: int) void
            +getDias() long
            +getKmRecorridos() int
            +calcularTotal() double
            +finalizar(fecha: LocalDate, kmFinal: int) void
            +getFechaDevolucion() LocalDate
            +getKmFinal() int
        }
        class EstadoContrato {
            <<enumeration>>
            ACTIVO
            FINALIZADO
        }
    }

    namespace tarifas {
        class Tarifa {
            <<interface>>
            +calcular(v: Vehiculo, dias: long, km: int) double
            +getNombre() String
        }
        class TarifaDiaria {
            +calcular(v: Vehiculo, dias: long, km: int) double
            +getNombre() String
        }
    }

    namespace excepciones {
        class AlquilerException {
            +AlquilerException(mensaje: String)
        }
        class VehiculoNoDisponibleException {
            +VehiculoNoDisponibleException(patente: String)
        }
        class ConductorNoHabilitadoException {
            +ConductorNoHabilitadoException(motivo: String)
        }
    }

    Vehiculo <|-- Auto
    Vehiculo <|-- Moto
    Vehiculo <|-- Camioneta
    Vehiculo --> "1" EstadoVehiculo
    Auto --> "1" TipoCaja

    Cliente "1" *-- "1" LicenciaConducir
    LicenciaConducir --> "1..*" CategoriaLicencia

    Contrato "0..*" --> "1" Cliente : cliente
    Contrato "0..*" --> "1" Vehiculo : vehiculo
    Contrato "0..*" --> "1" Tarifa : tarifa
    Contrato --> "1" EstadoContrato

    Tarifa <|.. TarifaDiaria

    Agencia "1" o-- "0..*" Vehiculo
    Agencia "1" o-- "0..*" Cliente
    Agencia "1" o-- "0..*" Contrato

    ServicioFlota --> "1" Agencia
    ServicioClientes --> "1" Agencia
    ServicioAlquiler --> "1" Agencia
    GestorPersistencia ..> Agencia : serializa

    Exception <|-- AlquilerException
    AlquilerException <|-- VehiculoNoDisponibleException
    AlquilerException <|-- ConductorNoHabilitadoException

    Main ..> VentanaPrincipal : crea
    VentanaPrincipal --> PanelFlota
    VentanaPrincipal --> PanelClientes
    VentanaPrincipal --> PanelContratos
    PanelContratos ..> FormularioContrato : abre
    PanelContratos --> ModeloTablaContratos
    FormularioContrato --> ServicioAlquiler
    FormularioContrato --> ServicioClientes
    FormularioContrato --> ServicioFlota
    PanelFlota ..> FormularioAuto : abre
    PanelFlota ..> FormularioMoto : abre
    PanelFlota ..> FormularioCamioneta : abre
    PanelClientes ..> FormularioCliente : abre
    PanelFlota --> ModeloTablaFlota
    PanelClientes --> ModeloTablaClientes
    PanelFlota --> ServicioFlota
    PanelClientes --> ServicioClientes
    FormularioAuto --> ServicioFlota
    FormularioMoto --> ServicioFlota
    FormularioCamioneta --> ServicioFlota
    FormularioCliente --> ServicioClientes
    FormularioAuto *-- PanelDatosVehiculo
    FormularioMoto *-- PanelDatosVehiculo
    FormularioCamioneta *-- PanelDatosVehiculo
    ModeloTablaFlota --> ServicioFlota
    ModeloTablaClientes --> ServicioClientes
    ModeloTablaContratos --> ServicioAlquiler
```

## Estado de implementación

| Clase | Paquete | Estado | Pendiente | Historia |
| --- | --- | --- | --- | --- |
| `Vehiculo` | `modelo.flota` | Parcial | `actualizarKilometraje()` | #11 |
| `Auto` | `modelo.flota` | Hecho | — | #2, #10 |
| `EstadoVehiculo`, `TipoCaja` | `modelo.flota` | Hecho | — | #1 |
| `Moto` | `modelo.flota` | Hecho | — | #2, #10 |
| `Camioneta` | `modelo.flota` | Hecho | — | #3, #10 |
| `Agencia` | `modelo` | Hecho | — | #6, #9 |
| `ServicioFlota` | `servicio` | Hecho | — | #1, #4, #5 |
| `AlquilerException` | `excepciones` | Hecho | — | #1 |
| `Main`, `VentanaPrincipal`, `PanelFlota`, `PanelClientes`, `PanelContratos`, `FormularioContrato`, `FormularioAuto`, `FormularioMoto`, `FormularioCamioneta`, `FormularioCliente`, `PanelDatosVehiculo`, `ModeloTablaFlota`, `ModeloTablaClientes`, `ModeloTablaContratos` | `vista` | Hecho para #10 | Crecen con cada historia | #1, #2, #3, #4, #5, #6, #7, #8, #9, #10 |
| `CategoriaLicencia` | `modelo.clientes` | Hecho | — | #6 |
| `LicenciaConducir` | `modelo.clientes` | Hecho | — | #6, #10 |
| `Cliente` | `modelo.clientes` | Hecho | — | #6, #10 |
| `ServicioClientes` | `servicio` | Hecho | — | #6, #7, #8 |
| `Contrato`, `EstadoContrato`, `ServicioAlquiler` | `modelo.alquiler`, `servicio` | Parcial | `fechaDevolucion`, `kmFinal`, `getKmRecorridos()`, `finalizar()`, `registrarDevolucion()` | #11, #15 |
| `Tarifa`, `TarifaDiaria` | `modelo.tarifas` | Hecho | — | #9 |
| `VehiculoNoDisponibleException` | `excepciones` | Hecho | — | #9 |
| `ConductorNoHabilitadoException` | `excepciones` | Hecho | — | #10 |
| `GestorPersistencia` | `persistencia` | Pendiente | Todo | #16 |

## Notas de diseño

- Las clases del modelo tienen getters públicos para los atributos que muestra la interfaz (`getPatente()`, `getMarca()`, `getModelo()`, `getAnio()`, `getKilometraje()`, `getPrecioBaseDiario()`, `getEstado()`). No se dibujan en el diagrama para no saturarlo.
- `validarDatosPropios()` valida los datos propios de cada subclase y la llama `ServicioFlota.registrarVehiculo()`. En `Vehiculo` no valida nada; `Auto` exige puertas y pasajeros mayores a 0; `Moto` exige cilindrada mayor a 0; `Camioneta` exige capacidad de carga mayor a 0.
- `PanelDatosVehiculo` tiene los 7 campos comunes del formulario. Cada formulario de vehículo lo contiene y le agrega sus campos propios con `agregar()`.
- `ServicioFlota.registrarVehiculo()` rechaza con `AlquilerException`: patente vacía o en blanco, kilometraje o precio negativos y patente repetida (sin distinguir mayúsculas).
- `ServicioFlota.filtrar(tipo, estado)` devuelve los vehículos que cumplen todos los filtros pedidos, en el orden en que se registraron. `tipo` es el texto de `getTipo()` ("Auto", "Moto", "Camioneta") y `null` en `tipo` o en `estado` significa sin filtro.
- `ModeloTablaFlota` guarda el filtro (`filtroTipo`, `filtroEstado`) y lo cambia con `setFiltro()`, que refresca la tabla. Siempre consulta a `ServicioFlota.filtrar()`, por eso un vehículo nuevo aparece solo si cumple el filtro elegido.
- `PanelFlota` tiene dos listas desplegables, `comboTipo` y `comboEstado`, con "Todos" como primera opción; "Todos" se traduce a `null` antes de llamar a `setFiltro()`.
- `ServicioFlota.eliminarVehiculo(v)` quita el vehículo de la flota y lanza `AlquilerException` ("No se puede dar de baja un vehículo alquilado") si `v.getEstado()` es `ALQUILADO`. No distingue si `v` estaba en la flota: la interfaz solo ofrece vehículos de la tabla.
- `ModeloTablaFlota.getVehiculoEn(fila)` devuelve el vehículo de esa fila según el filtro elegido.
- En `PanelFlota`, el botón `darDeBaja` está habilitado solo con una fila elegida en `tablaFlota`. Pide confirmación mostrando la patente; si se confirma, llama a `eliminarVehiculo()` y refresca la tabla conservando el filtro, y si hay error lo muestra en un cuadro de diálogo.
- `Vehiculo.cambiarEstado()` asigna el estado sin validar nada. Las reglas de cuándo se puede alquilar o devolver llegan con #9 y #11.
- Fuera de alcance (historias #12, #13 y #14 sacadas del backlog): `TarifaSemanal`, `TarifaKilometraje` y el recargo por atraso (`RECARGO_ATRASO`, `getDiasAtraso()`, `calcularRecargoAtraso()`). `Tarifa` queda como interfaz con una sola implementación, `TarifaDiaria`; el parámetro `km` de `calcular()` se conserva para poder sumar otras tarifas sin cambiar `Contrato`.
- `getFactorTarifa()` es protegido: solo lo usa `getPrecioDiario()`. `Camioneta` devuelve 1,4, o 1,6 si tiene tracción 4x4.
- `ServicioClientes.registrarCliente()` rechaza con `AlquilerException`, en este orden: nombre, apellido, DNI, teléfono o número de licencia vacíos o en blanco ("El campo <campo> es obligatorio"), licencia sin categorías y DNI repetido (se compara sin espacios sobrantes). Valida todo antes de asignar el número con `Agencia.siguienteIdCliente()` (1, 2, 3…), así un cliente rechazado no consume número. Un cliente menor de edad o con la licencia vencida se registra igual.
- `exigir(valor, campo)` es un método privado de `ServicioClientes` que evita repetir la validación de obligatorios.
- `Cliente.getEdad()` cuenta los años completos hasta hoy. `LicenciaConducir.estaVigente(fecha)` es verdadera hasta el día del vencimiento inclusive. `Cliente.EDAD_MINIMA` es 18 y la usa `FormularioCliente` para decidir el aviso; `Cliente.setId()` lo usa solo el servicio.
- `FormularioCliente` lee las fechas con `fecha(etiqueta, campo)` (formato dd/mm/aaaa estricto: rechaza 31/02) y tiene una casilla por cada `CategoriaLicencia`. Después de registrar, si el cliente es menor de `EDAD_MINIMA` o su licencia no está vigente hoy, muestra un aviso que empieza con "Cliente registrado, pero todavía no va a poder alquilar". `agregar()` y `avisarSiNoPuedeAlquilar()` son métodos privados suyos.
- `ModeloTablaClientes` muestra número, nombre completo, DNI, teléfono, categorías (ordenadas y separadas por coma) y vencimiento en dd/mm/aaaa. `VentanaPrincipal` arma la pestaña "Clientes" con `PanelClientes` (que tiene `tablaClientes` y `agregarCliente`) y recibe los dos servicios; `Main` los crea con la misma `Agencia`.
- `ServicioClientes.buscarPorTexto(texto)` devuelve los clientes cuyo nombre, apellido, nombre completo o DNI contienen el texto, sin distinguir mayúsculas y sin contar los espacios del principio y del final; con texto `null`, vacío o en blanco devuelve todos, en orden de registro. No ignora tildes ("Gomez" no encuentra a "Gómez").
- `VentanaPrincipal` solo arma las pestañas con `PanelFlota`, `PanelClientes` y `PanelContratos`; cada panel tiene su tabla, sus botones y su servicio, y recibe la ventana (`padre`) para abrir sus diálogos. `PanelClientes.alCambiarElTexto()` registra un único listener que avisa ante cualquier cambio del texto de un campo, y `PanelFlota.darDeBajaElegido()` tiene la baja de un vehículo.
- `ModeloTablaClientes` guarda la búsqueda (`filtroTexto`) y la cambia con `setFiltro()`, que refresca la tabla. Siempre consulta a `buscarPorTexto()`, por eso un cliente nuevo aparece solo si coincide con la búsqueda. `PanelClientes` tiene `campoBusqueda` arriba de la tabla de clientes y llama a `setFiltro()` con cada cambio de texto, sin botón.
- `ServicioClientes.eliminarCliente(c)` quita al cliente de la base y declara `throws AlquilerException` y rechaza con "No se puede eliminar un cliente con un contrato activo" si hay un contrato `ACTIVO` de ese cliente en `agencia.getContratos()`. Si el cliente no está registrado no hace nada. Los números ya asignados no se reutilizan: el siguiente cliente sigue la numeración (`Agencia.siguienteIdCliente()`).
- `ModeloTablaClientes.getClienteEn(fila)` devuelve el cliente de esa fila según la búsqueda (`filtroTexto`). `PanelClientes` tiene `eliminarClienteBoton`, habilitado solo con una fila elegida; `eliminarClienteElegido()` (privado) pide confirmación con nombre completo y DNI, llama al servicio y refresca la tabla conservando la búsqueda; si el servicio lanza una excepción la muestra en un diálogo "No se pudo eliminar el cliente".
- Los getters de `Cliente`, `LicenciaConducir` y `Agencia.getClientes()` no se dibujan completos en el diagrama (ver la primera nota).
- El formato de fecha dd/mm/aaaa está definido cuatro veces (`FORMATO_FECHA` en `FormularioCliente`, `ModeloTablaClientes`, `ModeloTablaContratos` y `ServicioAlquiler`): candidato a refactor. El listener de cambios de texto también está repetido (`PanelClientes.alCambiarElTexto()` y el de `campoFechaFin` en `FormularioContrato`).
- `Tarifa` es una interfaz (`calcular(v, dias, km)` y `getNombre()`); por ahora solo existe `TarifaDiaria`, que devuelve `v.getPrecioDiario() * dias` e ignora `km`.
- `Contrato` toma `kmInicial` de `vehiculo.getKilometraje()` y queda `ACTIVO`. `getDias()` son los días entre `fechaInicio` y `fechaFinPrevista`; `calcularTotal()` es `tarifa.calcular(vehiculo, getDias(), 0)`. `setNumero()` lo usa solo `ServicioAlquiler`. `Vehiculo.estaDisponible()` es verdadero con estado `DISPONIBLE`.
- `ServicioAlquiler.crearContrato()` valida primero que `fin` sea posterior a hoy ("La fecha de fin prevista debe ser posterior a hoy") y después que el vehículo esté disponible (`VehiculoNoDisponibleException`: "El vehículo <patente> no está disponible"). Si todo está bien asigna el número con `Agencia.siguienteNroContrato()`, deja el vehículo `ALQUILADO`, guarda el contrato con fecha de inicio hoy y lo devuelve. Un contrato rechazado no consume número. `listarContratos(estado)` devuelve los contratos de ese estado, o todos con `null`, en orden de creación.
- `Cliente.toString()` ("<nombre completo> (DNI <dni>)") y `Vehiculo.toString()` ("<patente> - <marca> <modelo>") existen para mostrar los elementos en las listas desplegables de `FormularioContrato`.
- `ModeloTablaContratos` muestra número, nombre completo del cliente, patente, fechas en dd/mm/aaaa, nombre de la tarifa, estado y total; consulta a `ServicioAlquiler.listarContratos(null)` cada vez.
- `FormularioContrato` ofrece todos los clientes y solo los vehículos `DISPONIBLE`, sin nada elegido al abrir. `etiquetaTotal` muestra el total estimado (precio diario × días hasta `campoFechaFin`) y "-" si falta el vehículo o la fecha no es válida o no es posterior a hoy; se recalcula al cambiar el vehículo o la fecha. Al guardar valida en este orden: cliente ("Hay que elegir un cliente"), vehículo ("Hay que elegir un vehículo disponible"), formato de la fecha y las reglas de `crearContrato()`; los errores se muestran en un diálogo "No se pudo crear el contrato".
- `PanelContratos` recibe un `Runnable alCrear` que `VentanaPrincipal` conecta con `PanelFlota.refrescar()`: al crear un contrato se refrescan la tabla de contratos y la de la flota, porque el vehículo pasó a `ALQUILADO`.
- `Vehiculo.getLicenciaRequerida()` es abstracto: `Auto` y `Camioneta` piden la categoría B y `Moto` la A. `LicenciaConducir.habilitaPara(c, fecha)` es verdadero si la licencia está vigente en esa fecha (inclusive el día del vencimiento) y tiene la categoría `c`. `Cliente.puedeConducir(v, hasta)` es verdadero con `EDAD_MINIMA` años o más (la edad se cuenta a hoy) y una licencia que habilite la categoría del vehículo hasta `hasta`.
- `ServicioAlquiler.crearContrato()` valida en este orden: fecha de fin, vehículo disponible y conductor. Si `!c.puedeConducir(v, fin)` lanza `ConductorNoHabilitadoException` ("El cliente no está habilitado para conducir este vehículo: <motivo>"); `motivoDeRechazo()` (privado) elige el primer motivo: "es menor de 18 años", "su licencia no tiene la categoría <A o B>" o "su licencia no está vigente hasta el <fin en dd/mm/aaaa>". `FormularioContrato` lo muestra como cualquier otro error, sin cambios. No hay equivalencias entre categorías: solo cuenta la exacta.
- El punto de entrada (`Main`) está en el paquete `vista`; crea los tres servicios con la misma `Agencia`.
