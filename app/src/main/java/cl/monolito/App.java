package cl.monolito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    // CREATE
    public static void crearUsuario(String nombre, String email, String password) {
        String sql = "INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            System.out.println("✅ Usuario creado en BD: " + email);
        } catch (SQLException e) {
            System.err.println("Error creando usuario: " + e.getMessage());
        }
    }

    // READ (uno)
    public static Usuario obtenerUsuario(int id) {
        String sql = "SELECT id, nombre, email, password FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email")  // usamos 'correo' como 'email'
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo usuario: " + e.getMessage());
        }
        return null;
    }

    // READ (lista)
    public static List<Usuario> listarUsuarios() {
        String sql = "SELECT id, nombre, email, password FROM usuarios ORDER BY id";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listando usuarios: " + e.getMessage());
        }
        return lista;
    }

    // UPDATE
    public static boolean actualizarUsuario(int id, String nombre, String email) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setInt(3, id);
            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando usuario: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public static boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando usuario: " + e.getMessage());
            return false;
        }
    }

    // Demo rápida por consola
    public static void main(String[] args) {
        System.out.println("=== CRUD Usuarios con PostgreSQL ===");

        crearUsuario("Ana", "ana@email.com", "1234");
        crearUsuario("Pedro", "pedro@email.com", "abcd");

        System.out.println("Listado:");
        listarUsuarios().forEach(System.out::println);

        System.out.println("Obtener ID 1: " + obtenerUsuario(1));

        System.out.println("Actualizar ID 2:");
        boolean ok = actualizarUsuario(2, "Pedro López", "pedro.lopez@email.com");
        System.out.println("Actualizado? " + ok);

        System.out.println("Eliminar ID 1:");
        boolean del = eliminarUsuario(1);
        System.out.println("Eliminado? " + del);

        System.out.println("Listado final:");
        listarUsuarios().forEach(System.out::println);
    }
}