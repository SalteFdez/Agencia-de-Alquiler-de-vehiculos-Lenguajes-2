package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import modelo.alquiler.Contrato;
import modelo.clientes.CategoriaLicencia;
import modelo.clientes.Cliente;
import modelo.clientes.LicenciaConducir;
import modelo.flota.Auto;
import modelo.flota.TipoCaja;
import modelo.tarifas.TarifaDiaria;
import org.junit.jupiter.api.Test;

class AgenciaTest {

    @Test
    void siguienteIdClienteDevuelveUnoDosTresEnLlamadasSucesivas() {
        Agencia agencia = new Agencia();

        assertEquals(1, agencia.siguienteIdCliente());
        assertEquals(2, agencia.siguienteIdCliente());
        assertEquals(3, agencia.siguienteIdCliente());
    }

    @Test
    void siguienteNroContratoDevuelveUnoDosTresEnLlamadasSucesivas() {
        Agencia agencia = new Agencia();

        assertEquals(1, agencia.siguienteNroContrato());
        assertEquals(2, agencia.siguienteNroContrato());
        assertEquals(3, agencia.siguienteNroContrato());
    }

    @Test
    void unaAgenciaNuevaNoTieneContratos() {
        assertTrue(new Agencia().getContratos().isEmpty());
    }

    private static Auto auto(String patente) {
        return new Auto(patente, "Fiat", "Cronos", "Blanco", 2022, 15000, 10000.0, 4, 5, TipoCaja.MANUAL);
    }

    private static Cliente cliente(String dni) {
        return new Cliente("Ana", "Gomez", dni, LocalDate.of(1990, 5, 20), "3874123456",
                new LicenciaConducir("L123456", Set.of(CategoriaLicencia.B), LocalDate.now().plusYears(3)));
    }

    @Test
    void reemplazarConCopiaVehiculosClientesYContratosDeLaOtraYDescartaLosAnteriores() {
        Agencia viva = new Agencia();
        viva.getVehiculos().add(auto("ZZ999ZZ"));
        viva.getClientes().add(cliente("99999999"));
        Agencia otra = new Agencia();
        Auto cronos = auto("AB123CD");
        Cliente ana = cliente("30123456");
        Contrato contrato = new Contrato(ana, cronos, new TarifaDiaria(), LocalDate.now(),
                LocalDate.now().plusDays(3));
        otra.getVehiculos().add(cronos);
        otra.getClientes().add(ana);
        otra.getContratos().add(contrato);

        viva.reemplazarCon(otra);

        assertEquals(List.of(cronos), viva.getVehiculos());
        assertEquals(List.of(ana), viva.getClientes());
        assertEquals(1, viva.getContratos().size());
        assertSame(contrato, viva.getContratos().get(0));
    }

    @Test
    void reemplazarConCopiaLosContadoresYLaNumeracionContinuaLaDeLaOtra() {
        Agencia viva = new Agencia();
        Agencia otra = new Agencia();
        otra.siguienteIdCliente();
        otra.siguienteIdCliente();
        otra.siguienteNroContrato();
        otra.siguienteNroContrato();
        otra.siguienteNroContrato();

        viva.reemplazarCon(otra);

        assertEquals(3, viva.siguienteIdCliente());
        assertEquals(4, viva.siguienteNroContrato());
    }
}
