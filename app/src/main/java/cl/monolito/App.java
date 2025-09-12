package cl.monolito;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static List<Usuario> usuarios = new ArrayList<>();
    private static int contadorId = 1;

    public static Usuario crearUsuario(String nombre, String correo) {
        Usuario u = new Usuario(contadorId++, nombre, correo);
        usuarios.add(u);
        return u;
    }

    public static Usuario obtenerUsuario(int id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static boolean actualizarUsuario(int id, String nuevoNombre, String nuevoCorreo) {
        Usuario u = obtenerUsuario(id);
        if (u != null) {
            u.setNombre(nuevoNombre);
            u.setCorreo(nuevoCorreo);
            return true;
        }
        return false;
    }

    public static boolean eliminarUsuario(int id) {
        return usuarios.removeIf(u -> u.getId() == id);
    }

    public static void main(String[] args) {
        System.out.println("Sistema de Usuarios - E-commerce");

        Usuario u1 = crearUsuario("Ana", "ana@email.com");
        Usuario u2 = crearUsuario("Pedro", "pedro@email.com");

        System.out.println("Usuarios creados:");
        System.out.println(u1);
        System.out.println(u2);

        System.out.println("Consultar usuario con ID 1: " + obtenerUsuario(1));

        actualizarUsuario(2, "Pedro López", "pedro.lopez@email.com");
        System.out.println("Usuario actualizado con ID 2: " + obtenerUsuario(2));

        eliminarUsuario(1);
        System.out.println("Usuarios después de eliminar ID 1: " + usuarios);
    }
    // Añadir dentro de la clase App
    public static void resetForTests() {
    usuarios.clear();
    contadorId = 1;
    }
}
