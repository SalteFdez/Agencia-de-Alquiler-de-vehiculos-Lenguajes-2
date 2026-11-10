package servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import excepciones.AlquilerException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;
import modelo.alquiler.Contrato;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import modelo.clientes.CategoriaLicencia;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GeneradorTicketTest {

    @TempDir
    Path carpeta;

    private static Contrato contrato(int numero) {
        return contratoDe("Ana", "Gomez", numero);
    }

    private static Contrato contratoDe(String nombre, String apellido, int numero) {
        Cliente cliente = new Cliente(nombre, apellido, "30123456", LocalDate.of(1990, 5, 20),
                "3874123456", new LicenciaConducir("L1", Set.of(CategoriaLicencia.B),
                        LocalDate.now().plusYears(3)));
        Auto cronos = new Auto("AB123CD", "Fiat", "Cronos", "Blanco", 2022, 15000, 10000.0,
                4, 5, TipoCaja.MANUAL);
        Contrato contrato = new Contrato(cliente, cronos, new TarifaDiaria(),
                LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 13));
        contrato.setNumero(numero);
        return contrato;
    }

    @Test
    void generarEscribeContratoNumeroPdfEnLaCarpetaYDevuelveEseArchivo() throws AlquilerException {
        File tickets = carpeta.resolve("tickets").toFile();
        tickets.mkdirs();

        File ticket = new GeneradorTicket(tickets).generar(contrato(7));

        assertEquals(new File(tickets, "contrato-7.pdf"), ticket);
        assertTrue(ticket.isFile());
    }

    @Test
    void generarCreaLaCarpetaDeTicketsSiNoExiste() throws AlquilerException {
        File tickets = carpeta.resolve("tickets").toFile();

        File ticket = new GeneradorTicket(tickets).generar(contrato(8));

        assertTrue(tickets.isDirectory());
        assertTrue(ticket.isFile());
    }

    private static final Charset WINDOWS_1252 = Charset.forName("windows-1252");

    private static String textoDe(File ticket) throws IOException {
        return new String(Files.readAllBytes(ticket.toPath()), WINDOWS_1252);
    }

    @Test
    void elArchivoEmpiezaConPdfYTerminaConEof() throws AlquilerException, IOException {
        File ticket = new GeneradorTicket(carpeta.toFile()).generar(contrato(9));

        String texto = textoDe(ticket);
        assertTrue(texto.startsWith("%PDF-"));
        assertTrue(texto.stripTrailing().endsWith("%%EOF"));
    }

    @Test
    void startxrefApuntaAlOffsetRealDeLaTablaXref() throws AlquilerException, IOException {
        File ticket = new GeneradorTicket(carpeta.toFile()).generar(contrato(10));

        String texto = textoDe(ticket);
        int marca = texto.lastIndexOf("startxref");
        int offset = Integer.parseInt(texto.substring(marca + "startxref".length()).trim()
                .split("\\s+")[0]);
        assertTrue(texto.startsWith("xref", offset));
    }

    @Test
    void elTicketContieneNumeroDeContratoFechaYClienteConSuDni() throws AlquilerException, IOException {
        File ticket = new GeneradorTicket(carpeta.toFile()).generar(contrato(7));

        String texto = textoDe(ticket);
        assertTrue(texto.contains("(Contrato N° 7)"));
        assertTrue(texto.contains("(Fecha: 10/11/2026)"));
        assertTrue(texto.contains("(Cliente: Ana Gomez)"));
        assertTrue(texto.contains("(DNI: 30123456)"));
    }

    @Test
    void elTicketContieneVehiculoPatenteDevolucionPrecioPorDiaYTotalEstimado()
            throws AlquilerException, IOException {
        File ticket = new GeneradorTicket(carpeta.toFile()).generar(contrato(7));

        String texto = textoDe(ticket);
        assertTrue(texto.contains("(Vehículo: Fiat Cronos)"));
        assertTrue(texto.contains("(Patente: AB123CD)"));
        assertTrue(texto.contains("(Devolución: 13/11/2026)"));
        assertTrue(texto.contains("(Precio por día: $10.000,00)"));
        assertTrue(texto.contains("(Total estimado: $30.000,00)"));
    }

    @Test
    void losAcentosYLasEniesSeEscribenEnWinAnsiYSeLeenBien() throws AlquilerException, IOException {
        File ticket = new GeneradorTicket(carpeta.toFile())
                .generar(contratoDe("Lucía", "Núñez", 11));

        assertTrue(textoDe(ticket).contains("(Cliente: Lucía Núñez)"));
    }

    @Test
    void losParentesisYLaBarraInvertidaDeUnDatoSeEscapanYElPdfSigueSiendoValido()
            throws AlquilerException, IOException {
        File ticket = new GeneradorTicket(carpeta.toFile())
                .generar(contratoDe("Ana", "Gomez (h) a\\b", 12));

        String texto = textoDe(ticket);
        assertTrue(texto.contains("(Cliente: Ana Gomez \\(h\\) a\\\\b)"));
        int marca = texto.lastIndexOf("startxref");
        int offset = Integer.parseInt(texto.substring(marca + "startxref".length()).trim()
                .split("\\s+")[0]);
        assertTrue(texto.startsWith("xref", offset));
        int largo = Integer.parseInt(texto.replaceAll("(?s).*/Length (\\d+).*", "$1"));
        int inicio = texto.indexOf("stream\n") + "stream\n".length();
        assertTrue(texto.startsWith("\nendstream", inicio + largo));
    }

    @Test
    void siLaCarpetaNoSePuedeCrearLanzaAlquilerExceptionYNoDejaTicket() throws IOException {
        File ocupado = carpeta.resolve("tickets").toFile();
        Files.write(ocupado.toPath(), new byte[0]);

        AlquilerException error = assertThrows(AlquilerException.class,
                () -> new GeneradorTicket(ocupado).generar(contrato(13)));

        assertEquals("No se pudo generar el ticket", error.getMessage());
        assertTrue(ocupado.isFile());
        assertEquals(1, carpeta.toFile().list().length);
    }
}
