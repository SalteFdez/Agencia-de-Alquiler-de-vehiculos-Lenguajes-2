# Diagrama de clases UML — Agencia de alquiler de vehículos

Fuente de verdad del diseño, en texto. Los nombres de clases, atributos y métodos del código deben coincidir con este archivo. Si el diseño cambia durante una historia, actualizar este archivo en el mismo paso.

## Regla de nombres

- Todo nombre de clase, atributo, método o parámetro del modelo y los servicios se escribe **exactamente** como figura en este archivo.
- Lo mismo vale para la vista cuando el dato es el mismo: un campo de formulario o un getter que representa un atributo del modelo usa el nombre de ese atributo (por ejemplo `precioBaseDiario` y `getPrecioBaseDiario()`, no `precioBase`).
- Si hace falta un nombre que no está acá, primero se agrega a este archivo y después se usa en el código.
- Antes de dar por terminada una historia, comparar los nombres del código con este archivo y actualizar el diagrama, la tabla de estado y las notas.

Notación (Mermaid): `-` privado, `+` público, `#` protegido, `*` abstracto, `$` static. `<|--` herencia, `<|..` implementa, `*--` composición, `o--` agregación, `-->` asociación, `..>` dependencia.

Última actualización: historia #19 terminada (el operario ve los contratos del kiosco sin cerrar y sus cambios no los pisan: guardado inmediato sobre datos frescos con `ServicioOperario`, recarga cada 3 s, guardado atómico).

## Diagrama

```mermaid
classDiagram
    direction TB

    namespace vista {
        class Main {
            +main(args: String[])$ void
            ~cargarAgencia(gestor: GestorPersistencia)$ Agencia
        }
        class VentanaPrincipal {
            -INTERVALO_MS: int$
            -temporizador: Timer
            +VentanaPrincipal(servicioFlota: ServicioFlota, servicioClientes: ServicioClientes, servicioAlquiler: ServicioAlquiler, servicioOperario: ServicioOperario)
        }
        class PanelFlota {
            -TODOS: String$
            -padre: Frame
            -servicio: ServicioFlota
            -servicioOperario: ServicioOperario
            -modelo: ModeloTablaFlota
            -comboTipo: JComboBox~String~
            -comboEstado: JComboBox~String~
            -tablaFlota: JTable
            -darDeBaja: JButton
            -patenteElegida: String
            -refrescando: boolean
            +PanelFlota(padre: Frame, servicio: ServicioFlota, servicioOperario: ServicioOperario)
            +refrescar() void
            -darDeBajaElegido() void
        }
        class PanelClientes {
            -padre: Frame
            -servicio: ServicioClientes
            -servicioOperario: ServicioOperario
            -modelo: ModeloTablaClientes
            -campoBusqueda: JTextField
            -tablaClientes: JTable
            -agregarCliente: JButton
            -eliminarClienteBoton: JButton
            -dniElegido: String
            -refrescando: boolean
            +PanelClientes(padre: Frame, servicio: ServicioClientes, servicioOperario: ServicioOperario)
            +refrescar() void
            ~alCambiarElTexto(campo: JTextField, accion: Runnable)$ void
            -eliminarClienteElegido() void
        }
        class PanelContratos {
            -TODOS: String$
            -padre: Frame
            -servicioAlquiler: ServicioAlquiler
            -servicioClientes: ServicioClientes
            -servicioFlota: ServicioFlota
            -servicioOperario: ServicioOperario
            -alCambiarFlota: Runnable
            -modelo: ModeloTablaContratos
            -comboEstado: JComboBox~String~
            -tablaContratos: JTable
            -nuevoContrato: JButton
            -devolucionBoton: JButton
            -numeroElegido: Integer
            -refrescando: boolean
            +PanelContratos(padre: Frame, servicioAlquiler: ServicioAlquiler, servicioClientes: ServicioClientes, servicioFlota: ServicioFlota, servicioOperario: ServicioOperario, alCambiarFlota: Runnable)
            +refrescar() void
        }
        class FormularioContrato {
            -SIN_TOTAL: String$
            -servicioAlquiler: ServicioAlquiler
            -servicioOperario: ServicioOperario
            -alCrear: Runnable
            -comboCliente: JComboBox~Cliente~
            -comboVehiculo: JComboBox~Vehiculo~
            -campoFechaFin: JTextField
            -etiquetaTotal: JLabel
            +FormularioContrato(padre: Frame, servicioAlquiler: ServicioAlquiler, servicioClientes: ServicioClientes, servicioFlota: ServicioFlota, servicioOperario: ServicioOperario, alCrear: Runnable)
            -guardar() void
            -actualizarTotal() void
        }
        class FormularioDevolucion {
            -servicioOperario: ServicioOperario
            -contrato: Contrato
            -alRegistrar: Runnable
            -campoFechaDevolucion: JTextField
            -campoKmFinal: JTextField
            +FormularioDevolucion(padre: Frame, servicioOperario: ServicioOperario, contrato: Contrato, alRegistrar: Runnable)
            -guardar() void
        }
        class FormularioAuto {
            -servicio: ServicioOperario
            -alRegistrar: Runnable
            +FormularioAuto(padre: Frame, servicio: ServicioOperario, alRegistrar: Runnable)
            -guardar() void
        }
        class FormularioMoto {
            -servicio: ServicioOperario
            -alRegistrar: Runnable
            +FormularioMoto(padre: Frame, servicio: ServicioOperario, alRegistrar: Runnable)
            -guardar() void
        }
        class FormularioCamioneta {
            -servicio: ServicioOperario
            -alRegistrar: Runnable
            -capacidadCargaKg: JTextField
            -traccion4x4: JCheckBox
            +FormularioCamioneta(padre: Frame, servicio: ServicioOperario, alRegistrar: Runnable)
            -guardar() void
        }
        class FormularioCliente {
            -servicio: ServicioOperario
            -alRegistrar: Runnable
            -nombre: JTextField
            -apellido: JTextField
            -dni: JTextField
            -fechaNacimiento: JTextField
            -telefono: JTextField
            -numero: JTextField
            -fechaVencimiento: JTextField
            -categorias: Map~CategoriaLicencia, JCheckBox~
            +FormularioCliente(padre: Frame, servicio: ServicioOperario, alRegistrar: Runnable)
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
            -servicio: ServicioAlquiler
            -filtroEstado: EstadoContrato
            +ModeloTablaContratos(servicio: ServicioAlquiler)
            +setFiltro(estado: EstadoContrato) void
            +getRowCount() int
            +getColumnCount() int
            +getColumnName(columna: int) String
            +getValueAt(fila: int, columna: int) Object
            +getContratoEn(fila: int) Contrato
        }
        class MainKiosco {
            +main(args: String[])$ void
        }
        class VentanaKiosco {
            -gestor: GestorPersistencia
            -fotos: CarpetaFotos
            +VentanaKiosco(gestor: GestorPersistencia, fotos: CarpetaFotos)
            -mostrarVehiculos() void
        }
        class FormularioAlquiler {
            -FUENTE: Font$
            -SIN_TOTAL: String$
            -vehiculo: Vehiculo
            -servicioAlquiler: ServicioAlquiler
            -servicioKiosco: ServicioKiosco
            -alAlquilar: Runnable
            -campoDni: JTextField
            -campoFechaDevolucion: JTextField
            -etiquetaTotal: JLabel
            +FormularioAlquiler(padre: Frame, vehiculo: Vehiculo, servicioAlquiler: ServicioAlquiler, servicioKiosco: ServicioKiosco, alAlquilar: Runnable)
            -confirmar() void
            -actualizarTotal() void
        }
        class MensajeKiosco {
            -SEGUNDOS_PARA_CERRAR: int$
            -FUENTE: Font$
            +exito(padre: Component, texto: String)$ void
            +error(padre: Component, texto: String)$ void
            -mostrar(padre: Component, titulo: String, texto: String, tipo: int, segundos: int)$ void
        }
        class PanelKiosco {
            -servicio: ServicioFlota
            -fotos: CarpetaFotos
            +PanelKiosco(servicio: ServicioFlota, fotos: CarpetaFotos, alElegir: Consumer~Vehiculo~)
        }
        class TarjetaVehiculo {
            -ANCHO_FOTO: int$
            -ALTO_FOTO: int$
            -etiquetaImagen: JLabel
            -etiquetaMarcaModelo: JLabel
            -etiquetaPrecio: JLabel
            +TarjetaVehiculo(vehiculo: Vehiculo, foto: Optional~File~)
        }
        class CarpetaFotos {
            -carpeta: File
            +CarpetaFotos(carpeta: File)
            +buscarFoto(patente: String) Optional~File~
        }
    }

    namespace servicio {
        class ServicioFlota {
            -agencia: Agencia
            +registrarVehiculo(v: Vehiculo) void
            +listarVehiculos() List~Vehiculo~
            +eliminarVehiculo(v: Vehiculo) void
            +filtrar(tipo: String, estado: EstadoVehiculo) List~Vehiculo~
            +buscarPorPatente(patente: String) Optional~Vehiculo~
        }
        class ServicioClientes {
            -agencia: Agencia
            +registrarCliente(c: Cliente) void
            -exigir(valor: String, campo: String) void
            +eliminarCliente(c: Cliente) void
            +buscarPorTexto(texto: String) List~Cliente~
            +buscarPorDni(dni: String) Optional~Cliente~
            +listarClientes() List~Cliente~
        }
        class ServicioAlquiler {
            -agencia: Agencia
            +ServicioAlquiler(agencia: Agencia)
            +crearContrato(c: Cliente, v: Vehiculo, t: Tarifa, fin: LocalDate) Contrato
            +calcularTotalEstimado(v: Vehiculo, fin: LocalDate) double
            +registrarDevolucion(c: Contrato, fecha: LocalDate, kmFinal: int) void
            -exigirFinPosteriorAHoy(fin: LocalDate) void
            -motivoDeRechazo(c: Cliente, v: Vehiculo, fin: LocalDate) String
            +listarContratos(estado: EstadoContrato) List~Contrato~
            +buscarPorNumero(numero: int) Optional~Contrato~
        }
        class ServicioKiosco {
            -gestor: GestorPersistencia
            +ServicioKiosco(gestor: GestorPersistencia)
            +alquilar(dni: String, patente: String, fin: LocalDate) Contrato
        }
        class ServicioOperario {
            -gestor: GestorPersistencia
            -agencia: Agencia
            +ServicioOperario(gestor: GestorPersistencia, agencia: Agencia)
            +recargar() void
            +registrarVehiculo(v: Vehiculo) void
            +eliminarVehiculo(patente: String) void
            +registrarCliente(c: Cliente) void
            +eliminarCliente(dni: String) void
            +crearContrato(dni: String, patente: String, fin: LocalDate) Contrato
            +registrarDevolucion(numero: int, fecha: LocalDate, kmFinal: int) Contrato
            -aplicar(cambio: Cambio~T~) T
        }
        class Cambio~T~ {
            <<interface>>
            +aplicarSobre(fresca: Agencia) T
        }
    }

    namespace persistencia {
        class GestorPersistencia {
            -archivo: File
            +GestorPersistencia(archivo: File)
            +guardar(a: Agencia) void
            +cargar() Agencia
            +existeArchivo() boolean
        }
    }

    namespace modelo {
        class Formatos {
            +FECHA: DateTimeFormatter$
            -Formatos()
        }
        class Agencia {
            -serialVersionUID: long$
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
            +reemplazarCon(otra: Agencia) void
        }
    }

    namespace flota {
        class Vehiculo {
            <<abstract>>
            -serialVersionUID: long$
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
            -serialVersionUID: long$
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
            -serialVersionUID: long$
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
            -serialVersionUID: long$
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
            -serialVersionUID: long$
            +calcular(v: Vehiculo, dias: long, km: int) double
            +getNombre() String
        }
    }

    namespace excepciones {
        class AlquilerException {
            +AlquilerException(mensaje: String)
            +AlquilerException(mensaje: String, causa: Throwable)
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
    ServicioKiosco --> GestorPersistencia
    ServicioOperario --> GestorPersistencia
    ServicioOperario --> "1" Agencia
    ServicioOperario ..> Cambio
    GestorPersistencia ..> Agencia : serializa

    Exception <|-- AlquilerException
    AlquilerException <|-- VehiculoNoDisponibleException
    AlquilerException <|-- ConductorNoHabilitadoException

    Main ..> VentanaPrincipal : crea
    Main ..> ServicioOperario : crea
    VentanaPrincipal --> ServicioOperario
    VentanaPrincipal --> PanelFlota
    VentanaPrincipal --> PanelClientes
    VentanaPrincipal --> PanelContratos
    PanelContratos ..> FormularioContrato : abre
    PanelContratos ..> FormularioDevolucion : abre
    PanelContratos --> ModeloTablaContratos
    FormularioContrato --> ServicioAlquiler
    FormularioContrato --> ServicioClientes
    FormularioContrato --> ServicioFlota
    FormularioContrato --> ServicioOperario
    FormularioDevolucion --> ServicioOperario
    FormularioDevolucion --> Contrato
    PanelFlota ..> FormularioAuto : abre
    PanelFlota ..> FormularioMoto : abre
    PanelFlota ..> FormularioCamioneta : abre
    PanelClientes ..> FormularioCliente : abre
    PanelFlota --> ModeloTablaFlota
    PanelClientes --> ModeloTablaClientes
    PanelFlota --> ServicioFlota
    PanelClientes --> ServicioClientes
    PanelFlota --> ServicioOperario
    PanelClientes --> ServicioOperario
    PanelContratos --> ServicioOperario
    FormularioAuto --> ServicioOperario
    FormularioMoto --> ServicioOperario
    FormularioCamioneta --> ServicioOperario
    FormularioCliente --> ServicioOperario
    FormularioAuto *-- PanelDatosVehiculo
    FormularioMoto *-- PanelDatosVehiculo
    FormularioCamioneta *-- PanelDatosVehiculo
    ModeloTablaFlota --> ServicioFlota
    ModeloTablaClientes --> ServicioClientes
    ModeloTablaContratos --> ServicioAlquiler
    MainKiosco ..> VentanaKiosco : crea
    MainKiosco ..> GestorPersistencia : crea
    VentanaKiosco ..> Main : carga con cargarAgencia
    VentanaKiosco --> PanelKiosco
    VentanaKiosco --> GestorPersistencia
    VentanaKiosco --> FormularioAlquiler
    FormularioAlquiler --> ServicioAlquiler
    FormularioAlquiler --> ServicioKiosco
    FormularioAlquiler --> MensajeKiosco
    PanelKiosco --> ServicioFlota
    PanelKiosco --> CarpetaFotos
    PanelKiosco *-- TarjetaVehiculo
    TarjetaVehiculo --> Vehiculo
```

## Estado de implementación

| Clase | Paquete | Estado | Pendiente | Historia |
| --- | --- | --- | --- | --- |
| `Vehiculo` | `modelo.flota` | Hecho | — | #1, #11, #16 |
| `Auto` | `modelo.flota` | Hecho | — | #2, #10 |
| `EstadoVehiculo`, `TipoCaja` | `modelo.flota` | Hecho | — | #1 |
| `Moto` | `modelo.flota` | Hecho | — | #2, #10 |
| `Camioneta` | `modelo.flota` | Hecho | — | #3, #10 |
| `Agencia` | `modelo` | Hecho | — | #6, #9, #16, #19 |
| `Formatos` | `modelo` | Hecho | — | refactor |
| `MainKiosco`, `VentanaKiosco`, `PanelKiosco`, `TarjetaVehiculo`, `CarpetaFotos`, `FormularioAlquiler`, `MensajeKiosco` | `vista` | Parcial | Actualizarse solo (#20) | #17, #18, #20 |
| `ServicioKiosco` | `servicio` | Hecho | Que el kiosco se actualice solo (#20) | #18, #19 |
| `ServicioOperario` | `servicio` | Hecho | — | #19 |
| `ServicioFlota` | `servicio` | Hecho | — | #1, #4, #5 |
| `AlquilerException` | `excepciones` | Hecho | — | #1, #16 |
| `Main`, `VentanaPrincipal`, `PanelFlota`, `PanelClientes`, `PanelContratos`, `FormularioContrato`, `FormularioDevolucion`, `FormularioAuto`, `FormularioMoto`, `FormularioCamioneta`, `FormularioCliente`, `PanelDatosVehiculo`, `ModeloTablaFlota`, `ModeloTablaClientes`, `ModeloTablaContratos` | `vista` | Hecho para #19 | Crecen con cada historia | #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #15, #16, #17, #19 |
| `CategoriaLicencia` | `modelo.clientes` | Hecho | — | #6 |
| `LicenciaConducir` | `modelo.clientes` | Hecho | — | #6, #10, #16 |
| `Cliente` | `modelo.clientes` | Hecho | — | #6, #10, #16 |
| `ServicioClientes` | `servicio` | Hecho | — | #6, #7, #8 |
| `Contrato`, `EstadoContrato`, `ServicioAlquiler` | `modelo.alquiler`, `servicio` | Hecho | — | #9, #10, #11, #15, #16 |
| `Tarifa`, `TarifaDiaria` | `modelo.tarifas` | Hecho | — | #9, #16 |
| `VehiculoNoDisponibleException` | `excepciones` | Hecho | — | #9 |
| `ConductorNoHabilitadoException` | `excepciones` | Hecho | — | #10 |
| `GestorPersistencia` | `persistencia` | Hecho | — | #16, #19 |

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
- `VentanaPrincipal` solo arma las pestañas con `PanelFlota`, `PanelClientes` y `PanelContratos`; cada panel tiene su tabla, sus botones y su servicio, y recibe la ventana (`padre`) para abrir sus diálogos. `PanelClientes.alCambiarElTexto()` (estático, del paquete) registra un único listener que avisa ante cualquier cambio del texto de un campo y también lo usa `FormularioContrato` para `campoFechaFin`, y `PanelFlota.darDeBajaElegido()` tiene la baja de un vehículo.
- `ModeloTablaClientes` guarda la búsqueda (`filtroTexto`) y la cambia con `setFiltro()`, que refresca la tabla. Siempre consulta a `buscarPorTexto()`, por eso un cliente nuevo aparece solo si coincide con la búsqueda. `PanelClientes` tiene `campoBusqueda` arriba de la tabla de clientes y llama a `setFiltro()` con cada cambio de texto, sin botón.
- `ServicioClientes.eliminarCliente(c)` quita al cliente de la base y declara `throws AlquilerException` y rechaza con "No se puede eliminar un cliente con un contrato activo" si hay un contrato `ACTIVO` de ese cliente en `agencia.getContratos()`. Si el cliente no está registrado no hace nada. Los números ya asignados no se reutilizan: el siguiente cliente sigue la numeración (`Agencia.siguienteIdCliente()`).
- `ModeloTablaClientes.getClienteEn(fila)` devuelve el cliente de esa fila según la búsqueda (`filtroTexto`). `PanelClientes` tiene `eliminarClienteBoton`, habilitado solo con una fila elegida; `eliminarClienteElegido()` (privado) pide confirmación con nombre completo y DNI, llama al servicio y refresca la tabla conservando la búsqueda; si el servicio lanza una excepción la muestra en un diálogo "No se pudo eliminar el cliente".
- Los getters de `Cliente`, `LicenciaConducir` y `Agencia.getClientes()` no se dibujan completos en el diagrama (ver la primera nota).
- El formato de fecha dd/mm/aaaa está en un solo lugar: `Formatos.FECHA` (en `modelo`, estricto, con `uuuu` para que rechace fechas como 31/02). Lo usan `FormularioCliente`, `FormularioDevolucion`, `ModeloTablaClientes`, `ModeloTablaContratos` y `ServicioAlquiler`.
- `Tarifa` es una interfaz (`calcular(v, dias, km)` y `getNombre()`); por ahora solo existe `TarifaDiaria`, que devuelve `v.getPrecioDiario() * dias` e ignora `km`.
- `Contrato` toma `kmInicial` de `vehiculo.getKilometraje()` y queda `ACTIVO`. `getDias()` son los días entre `fechaInicio` y `fechaFinPrevista`; `calcularTotal()` es `tarifa.calcular(vehiculo, getDias(), 0)`. `setNumero()` lo usa solo `ServicioAlquiler`. `Vehiculo.estaDisponible()` es verdadero con estado `DISPONIBLE`.
- `ServicioAlquiler.crearContrato()` valida primero que `fin` sea posterior a hoy ("La fecha de fin prevista debe ser posterior a hoy") y después que el vehículo esté disponible (`VehiculoNoDisponibleException`: "El vehículo <patente> no está disponible"). Si todo está bien asigna el número con `Agencia.siguienteNroContrato()`, deja el vehículo `ALQUILADO`, guarda el contrato con fecha de inicio hoy y lo devuelve. Un contrato rechazado no consume número. `listarContratos(estado)` devuelve los contratos de ese estado, o todos con `null`, en orden de creación.
- `Cliente.toString()` ("<nombre completo> (DNI <dni>)") y `Vehiculo.toString()` ("<patente> - <marca> <modelo>") existen para mostrar los elementos en las listas desplegables de `FormularioContrato`.
- `ModeloTablaContratos` muestra número, nombre completo del cliente, patente, fechas en dd/mm/aaaa, nombre de la tarifa, estado y total; consulta a `ServicioAlquiler.listarContratos(filtroEstado)` cada vez.
- `FormularioContrato` ofrece todos los clientes y solo los vehículos `DISPONIBLE`, sin nada elegido al abrir. `etiquetaTotal` muestra el total estimado (precio diario × días hasta `campoFechaFin`) y "-" si falta el vehículo o la fecha no es válida o no es posterior a hoy; se recalcula al cambiar el vehículo o la fecha. Al guardar valida en este orden: cliente ("Hay que elegir un cliente"), vehículo ("Hay que elegir un vehículo disponible"), formato de la fecha y las reglas de `crearContrato()`; los errores se muestran en un diálogo "No se pudo crear el contrato".
- `PanelContratos` recibe un `Runnable alCambiarFlota` que `VentanaPrincipal` conecta con `PanelFlota.refrescar()`: al crear un contrato o registrar una devolución se refrescan la tabla de contratos y la de la flota, porque el vehículo cambia de estado (`ALQUILADO` / `DISPONIBLE`).
- `Vehiculo.getLicenciaRequerida()` es abstracto: `Auto` y `Camioneta` piden la categoría B y `Moto` la A. `LicenciaConducir.habilitaPara(c, fecha)` es verdadero si la licencia está vigente en esa fecha (inclusive el día del vencimiento) y tiene la categoría `c`. `Cliente.puedeConducir(v, hasta)` es verdadero con `EDAD_MINIMA` años o más (la edad se cuenta a hoy) y una licencia que habilite la categoría del vehículo hasta `hasta`.
- `ServicioAlquiler.crearContrato()` valida en este orden: fecha de fin, vehículo disponible y conductor. Si `!c.puedeConducir(v, fin)` lanza `ConductorNoHabilitadoException` ("El cliente no está habilitado para conducir este vehículo: <motivo>"); `motivoDeRechazo()` (privado) elige el primer motivo: "es menor de 18 años", "su licencia no tiene la categoría <A o B>" o "su licencia no está vigente hasta el <fin en dd/mm/aaaa>". `FormularioContrato` lo muestra como cualquier otro error, sin cambios. No hay equivalencias entre categorías: solo cuenta la exacta.
- El punto de entrada (`Main`) está en el paquete `vista`; crea los tres servicios con la misma `Agencia`.
- `Vehiculo.kilometraje` ya no es `final`: `actualizarKilometraje(km)` lo cambia y solo lo usa `ServicioAlquiler.registrarDevolucion()`.
- `Contrato.finalizar(fecha, kmFinal)` no valida: guarda `fechaDevolucion` y `kmFinal` y pasa a `FINALIZADO`. Mientras está `ACTIVO`, `getFechaDevolucion()` es `null`, `getKmFinal()` es 0 y `getKmRecorridos()` es 0. Ya finalizado, `getKmRecorridos()` es `kmFinal - kmInicial` y `getDias()` es el mayor entre los días previstos y los días entre `fechaInicio` y `fechaDevolucion`, sin recargo; `calcularTotal()` usa esos días. Sigue pasando 0 km a la tarifa porque `TarifaDiaria` los ignora: pasar `getKmRecorridos()` queda para la historia de la tarifa por km.
- `ServicioAlquiler.registrarDevolucion(c, fecha, kmFinal)` valida en este orden: contrato ya finalizado ("El contrato <numero> ya está finalizado"), fecha anterior al inicio ("La fecha de devolución no puede ser anterior al inicio del contrato"), fecha posterior a hoy ("La fecha de devolución no puede ser posterior a hoy") y km final menor al inicial ("El km final no puede ser menor al km inicial (<kmInicial>)"). Si todo está bien finaliza el contrato, actualiza el km del vehículo y lo deja `DISPONIBLE`; si no, no cambia nada.
- `ModeloTablaContratos.getContratoEn(fila)` devuelve el contrato de esa fila, que `PanelContratos` pasa a `FormularioDevolucion`. `devolucionBoton` ("Registrar devolución") está deshabilitado mientras no haya una fila elegida.
- `FormularioDevolucion` abre con `campoFechaDevolucion` precargado con hoy (dd/mm/aaaa) y `campoKmFinal` vacío; `guardar()` lee la fecha con `FormularioCliente.fecha()` y el km con `PanelDatosVehiculo.entero()`. Si el servicio acepta, avisa con `alRegistrar` y muestra "Devolución registrada" con días cobrados, km recorridos y total con dos decimales; los errores se muestran en un diálogo "No se pudo registrar la devolución". En `PanelContratos` el mismo `Runnable` que usa `FormularioContrato` refresca la tabla de contratos y, con `alCambiarFlota`, la de la flota.
- `ModeloTablaContratos` guarda el filtro (`filtroEstado`) y lo cambia con `setFiltro(estado)`, que refresca la tabla; `null` muestra todos los contratos. `getRowCount()`, `getValueAt()` y `getContratoEn()` consultan siempre `ServicioAlquiler.listarContratos(filtroEstado)`, así que la fila elegida corresponde a la lista filtrada y el filtro se conserva al crear un contrato o registrar una devolución (la tabla solo se refresca).
- `PanelContratos` tiene la lista desplegable `comboEstado` ("Todos", "ACTIVO", "FINALIZADO") arriba de la tabla; "Todos" se traduce a `null` con la constante `TODOS` antes de llamar a `setFiltro()`, igual que en `PanelFlota`. Esa traducción está repetida en los dos paneles (candidato a refactor si aparece un tercer uso).
- `Agencia`, `Vehiculo` (y por herencia `Auto`, `Moto` y `Camioneta`), `Cliente`, `LicenciaConducir`, `Contrato` y `TarifaDiaria` implementan `java.io.Serializable` con `serialVersionUID = 1L`; los enums ya lo son. `Tarifa` es una interfaz sin `Serializable`: una tarifa nueva tiene que implementarlo para poder guardarse. Si el modelo cambia, hay que borrar `agencia.dat` (no hay migración de versiones).
- `GestorPersistencia(archivo)` guarda la agencia completa con `ObjectOutputStream` (se reemplaza el archivo) y la carga con `ObjectInputStream`; como se serializa todo el grafo junto, los contratos siguen apuntando a los mismos `Vehiculo` y `Cliente` de las listas y los contadores (`ultimoIdCliente`, `ultimoNroContrato`) se conservan. `guardar()` y `cargar()` lanzan `AlquilerException` ("No se pudieron guardar los datos" / "No se pudieron cargar los datos") con el error original como causa, usando el constructor `AlquilerException(mensaje, causa)`. `cargar()` no contempla un archivo que contenga otra clase (`ClassCastException`). `guardar()` escribe primero `<archivo>.tmp` en la misma carpeta y después lo mueve sobre el archivo de datos de forma atómica (`ATOMIC_MOVE`), para que la otra aplicación nunca lea un archivo a medio escribir; si falla, borra el temporal y deja el archivo anterior intacto.
- `Main` usa `new File("agencia.dat")`, en la carpeta desde la que se ejecuta la aplicación. `cargarAgencia(gestor)` (del paquete) carga el archivo si existe; sin archivo arranca con una agencia vacía y sin mensajes, y si la carga falla muestra el diálogo "No se pudieron cargar los datos" y arranca vacía. La aplicación del operario ya no guarda al cerrar: cada cambio se guarda en el momento a través de `ServicioOperario` (cerrar la ventana usa `EXIT_ON_CLOSE`), así que cerrarla no pisa lo que guardó el kiosco. Si el archivo está dañado al abrir, la aplicación arranca vacía pero el primer cambio falla con "No se pudieron cargar los datos" y no pisa el archivo.
- El kiosco es una aplicación aparte de la del operario: se lanza con `ejecutar-kiosco.sh` (`vista.MainKiosco`) y trabaja sobre el mismo `agencia.dat`: `VentanaKiosco` lo carga con `Main.cargarAgencia()` (del paquete; mismo comportamiento ante un archivo ausente o dañado) al abrir y cada vez que rearma la grilla, y guarda solo al confirmar un alquiler, a través de `ServicioKiosco`. El operario tiene la aplicación abierta al mismo tiempo: sus cambios se aplican sobre datos recién leídos (`ServicioOperario`), así que no pisan los del kiosco. El kiosco, en cambio, no se actualiza solo (#20).
- `VentanaKiosco` es un `JFrame` maximizado que cierra con `EXIT_ON_CLOSE` y contiene un `PanelKiosco`. `PanelKiosco` pide a `ServicioFlota.filtrar(null, DISPONIBLE)` los vehículos y arma una grilla de 3 columnas (pegada arriba, para que las tarjetas conserven su alto natural) dentro de un `JScrollPane`, con una `TarjetaVehiculo` por vehículo en el orden de la flota; al tocar una tarjeta (`mousePressed`) avisa con su vehículo al `Consumer` `alElegir`.
- `ServicioKiosco.alquilar(dni, patente, fin)` no guarda estado: cada vez valida que el DNI no esté vacío ("Hay que escribir el DNI"), vuelve a cargar `agencia.dat`, busca el cliente con `buscarPorDni` ("No hay ningún cliente registrado con el DNI <dni>") y el vehículo con `buscarPorPatente` (si ya no está: "El vehículo <patente> no está disponible"), crea el contrato con `ServicioAlquiler.crearContrato` con `TarifaDiaria` (mismas validaciones y numeración) y guarda enseguida. Si algo falla antes de guardar, no escribe nada; si el archivo falta o está dañado lanza "No se pudieron cargar los datos" y no lo crea. Entre la lectura y la escritura hay una ventana de milisegundos en la que un guardado del operario se perdería (gana el último que guarda).
- `ServicioOperario(gestor, agencia)` concentra los cambios del operario. `agencia` es la agencia "viva" que leen los servicios de consulta y las tablas; el archivo es la fuente de verdad. `aplicar(cambio)` (privado, con la interfaz privada `Cambio<T>`) vuelve a leer `agencia.dat` (si no existe empieza con una agencia vacía; si está dañado lanza "No se pudieron cargar los datos" y no lo toca), aplica el cambio sobre esos datos con los servicios de siempre, guarda enseguida y recién entonces copia el resultado a la agencia viva con `Agencia.reemplazarCon`; si guardar falla, la agencia viva no cambia. Las operaciones buscan por clave en los datos frescos: `eliminarVehiculo(patente)` y `eliminarCliente(dni)` (si ya no existen no hacen nada), `crearContrato(dni, patente, fin)` ("No hay ningún cliente registrado con el DNI <dni>" / "El vehículo <patente> no está disponible") y `registrarDevolucion(numero, fecha, kmFinal)`, que devuelve el contrato ya actualizado. Un número de contrato que no existe en el archivo todavía no tiene mensaje propio (`NoSuchElementException`). `recargar()` copia el archivo a la agencia viva (sin archivo no hace nada).
- `VentanaPrincipal` tiene un `Timer` de `INTERVALO_MS` (3000 ms) que llama a `ServicioOperario.recargar()` y a `refrescar()` de los tres paneles; si la recarga falla se ignora y se reintenta. Cada panel recuerda la clave de la fila elegida (`patenteElegida`, `dniElegido` o `numeroElegido`, actualizada solo cuando el usuario cambia la selección) y la vuelve a seleccionar al refrescar; el filtro y la búsqueda los guarda el modelo de la tabla. Los formularios que cambian datos reciben el `ServicioOperario`; `FormularioContrato` y `FormularioDevolucion` le pasan DNI, patente o número en vez de objetos, porque los objetos de la pantalla pueden ser de una recarga anterior.
- `FormularioAlquiler` (diálogo modal con letra de 28 pt) muestra el vehículo elegido, pide el DNI y la fecha de devolución (`dd/mm/aaaa`) y recalcula el total con `ServicioAlquiler.calcularTotalEstimado` al escribir una fecha válida ("-" si no lo es; formato argentino fijo). Al confirmar cierra el formulario, llama a `alAlquilar` (que rearma la grilla con los datos recién guardados) y muestra `MensajeKiosco.exito` con el número de contrato; ante un error muestra `MensajeKiosco.error` y el formulario sigue abierto. `MensajeKiosco` muestra los mensajes con letra grande: el de éxito se cierra solo a los `SEGUNDOS_PARA_CERRAR` (6) o con Aceptar; el de error espera a que se toque Aceptar.
- `CarpetaFotos.buscarFoto(patente)` devuelve el archivo `<patente>.png` de la carpeta `imagenes/` (en la carpeta desde la que se ejecuta) o vacío si la carpeta o la foto no existen. `TarjetaVehiculo` tiene tres etiquetas con nombre (`imagen`, `marcaModelo` y `precio`): la foto se escala sin deformarse a un recuadro de `ANCHO_FOTO` × `ALTO_FOTO` (320 × 200); sin foto, o con una foto ilegible, la etiqueta `imagen` muestra `getTipo()`. El precio es `getPrecioDiario()` con formato argentino fijo ("$11.500,00 por día"), distinto del formato de la máquina que usan las otras pantallas.
