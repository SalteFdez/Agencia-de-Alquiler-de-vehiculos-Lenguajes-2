package servicio;

import excepciones.AlquilerException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import modelo.Formatos;
import modelo.alquiler.Contrato;

public class GeneradorTicket {

    private static final Charset WINDOWS_1252 = Charset.forName("windows-1252");

    private final File carpeta;

    public GeneradorTicket(File carpeta) {
        this.carpeta = carpeta;
    }

    public File generar(Contrato contrato) throws AlquilerException {
        File archivo = new File(carpeta, "contrato-" + contrato.getNumero() + ".pdf");
        try {
            Files.createDirectories(carpeta.toPath());
            Files.write(archivo.toPath(), armarPdf(lineasDe(contrato)).getBytes(WINDOWS_1252));
        } catch (IOException e) {
            throw new AlquilerException("No se pudo generar el ticket", e);
        }
        return archivo;
    }

    private List<String> lineasDe(Contrato contrato) {
        List<String> lineas = new ArrayList<>();
        lineas.add("Contrato N° " + contrato.getNumero());
        lineas.add("Fecha: " + contrato.getFechaInicio().format(Formatos.FECHA));
        lineas.add("Cliente: " + contrato.getCliente().getNombreCompleto());
        lineas.add("DNI: " + contrato.getCliente().getDni());
        lineas.add("Vehículo: " + contrato.getVehiculo().getMarca() + " "
                + contrato.getVehiculo().getModelo());
        lineas.add("Patente: " + contrato.getVehiculo().getPatente());
        lineas.add("Devolución: " + contrato.getFechaFinPrevista().format(Formatos.FECHA));
        lineas.add("Precio por día: " + Formatos.precio(contrato.getVehiculo().getPrecioDiario()));
        lineas.add("Total estimado: " + Formatos.precio(contrato.calcularTotal()));
        return lineas;
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private String armarPdf(List<String> lineas) {
        StringBuilder texto = new StringBuilder("BT\n/F1 16 Tf\n24 TL\n50 780 Td\n");
        for (String linea : lineas) {
            texto.append("(").append(escapar(linea)).append(") Tj T*\n");
        }
        texto.append("ET");
        String[] objetos = {
            "<< /Type /Catalog /Pages 2 0 R >>",
            "<< /Type /Pages /Kids [3 0 R] /Count 1 >>",
            "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842]"
                    + " /Resources << /Font << /F1 5 0 R >> >> /Contents 4 0 R >>",
            "<< /Length " + texto.length() + " >>\nstream\n" + texto + "\nendstream",
            "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>"
        };
        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        List<Integer> posiciones = new ArrayList<>();
        for (int i = 0; i < objetos.length; i++) {
            posiciones.add(pdf.length());
            pdf.append(i + 1).append(" 0 obj\n").append(objetos[i]).append("\nendobj\n");
        }
        int inicioXref = pdf.length();
        pdf.append("xref\n0 ").append(objetos.length + 1).append("\n");
        pdf.append("0000000000 65535 f \n");
        for (int posicion : posiciones) {
            pdf.append(String.format("%010d 00000 n \n", posicion));
        }
        pdf.append("trailer\n<< /Size ").append(objetos.length + 1).append(" /Root 1 0 R >>\n");
        pdf.append("startxref\n").append(inicioXref).append("\n%%EOF\n");
        return pdf.toString();
    }
}
