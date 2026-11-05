package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
