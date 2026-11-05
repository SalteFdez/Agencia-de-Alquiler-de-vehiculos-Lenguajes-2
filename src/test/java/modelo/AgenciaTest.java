package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AgenciaTest {

    @Test
    void siguienteIdClienteDevuelveUnoDosTresEnLlamadasSucesivas() {
        Agencia agencia = new Agencia();

        assertEquals(1, agencia.siguienteIdCliente());
        assertEquals(2, agencia.siguienteIdCliente());
        assertEquals(3, agencia.siguienteIdCliente());
    }
}
