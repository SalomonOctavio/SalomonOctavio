package cl.monolito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @BeforeEach
    void setUp() {
    App.resetForTests();
    }

    @Test
    void testCrearUsuario() {
        Usuario u = App.crearUsuario("Ana", "ana@email.com");
        assertNotNull(u);
        assertEquals(1, u.getId());
        assertEquals("Ana", u.getNombre());
    }

    @Test
    void testObtenerUsuario() {
        App.crearUsuario("Ana", "ana@email.com");
        Usuario u = App.obtenerUsuario(1);
        assertNotNull(u);
        assertEquals("Ana", u.getNombre());
    }

    @Test
    void testActualizarUsuario() {
        App.crearUsuario("Pedro", "pedro@email.com");
        boolean actualizado = App.actualizarUsuario(1, "Pedro López", "pedro.lopez@email.com");
        assertTrue(actualizado);
        Usuario u = App.obtenerUsuario(1);
        assertEquals("Pedro López", u.getNombre());
    }

    @Test
    void testEliminarUsuario() {
        App.crearUsuario("Carlos", "carlos@email.com");
        boolean eliminado = App.eliminarUsuario(1);
        assertTrue(eliminado);
        assertNull(App.obtenerUsuario(1));
    }
}