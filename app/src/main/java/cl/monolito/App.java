package cl.monolito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    // =========================
    //  Compatibilidad con tests
    // =========================

    /** Limpia la tabla y reinicia el ID (útil para tests/demos). */
    public static void resetForTests() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement()) {
            st.execute("TRUNCATE TABLE usuarios RESTART IDENTITY");
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo resetear la tabla usuarios", e);
        }
    }

    /** Sobrecarga sin password para compatibilidad con tests antiguos. */
    public static Usuario crearUsuario(String nombre, String email) {
        return crearUsuario(nombre, email, "changeme");
    }

    // =========================
    //  CRUD REAL (PostgreSQL)
    // =========================

    /**
     * CREATE con UPSERT por email:
     * - Inserta (nombre, email, password)
     * - Si el email ya existe (único), actualiza el nombre
     * - Devuelve el usuario con ID asignado
     */
    public static Usuario crearUsuario(String nombre, String email, String password) {
        final String sql = """
            INSERT INTO usuarios (nombre, email, password)
            VALUES (?, ?, ?)
            ON CONFLICT (email) DO UPDATE
                SET nombre = EXCLUDED.nombre
            RETURNING id
            """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return new Usuario(id, nombre, email);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creando/actualizando usuario (upsert)", e);
        }
        return null;
    }

    /** READ (uno): obtiene usuario por ID */
    public static Usuario obtenerUsuario(int id) {
        final String sql = "SELECT id, nombre, email FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo usuario id=" + id, e);
        }
        return null;
    }

    /** READ (lista): retorna todos los usuarios */
    public static List<Usuario> listarUsuarios() {
        final String sql = "SELECT id, nombre, email FROM usuarios ORDER BY id";
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
            throw new RuntimeException("Error listando usuarios", e);
        }
        return lista;
    }

    /** UPDATE: actualiza nombre y email por ID */
    public static boolean actualizarUsuario(int id, String nombre, String email) {
        final String sql = "UPDATE usuarios SET nombre = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando usuario id=" + id, e);
        }
    }

    /** DELETE: elimina usuario por ID */
    public static boolean eliminarUsuario(int id) {
        final String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando usuario id=" + id, e);
        }
    }

    // =========================
    //  Demo por consola
    // =========================
    public static void main(String[] args) {
        System.out.println("=== CRUD Usuarios con PostgreSQL (UPSERT) ===");

        // Para demo limpia en cada ejecución, destapa esta línea:
        // resetForTests();

        // CREATE / UPSERT
        Usuario u1 = crearUsuario("Ana",   "ana@email.com",   "1234");
        Usuario u2 = crearUsuario("Pedro", "pedro@email.com", "abcd");

        // Reintenta con el mismo email (no revienta, hace UPSERT del nombre)
        crearUsuario("Ana Actualizada", "ana@email.com", "xyz");

        // LIST
        System.out.println("Listado inicial:");
        listarUsuarios().forEach(System.out::println);

        // READ
        if (u1 != null) {
            System.out.println("Obtener ID " + u1.getId() + ": " + obtenerUsuario(u1.getId()));
        }

        // UPDATE
        if (u2 != null) {
            System.out.println("Actualizar ID " + u2.getId() + ":");
            boolean ok = actualizarUsuario(u2.getId(), "Pedro López", "pedro.lopez@email.com");
            System.out.println("Actualizado? " + ok);
        }

        // DELETE
        if (u1 != null) {
            System.out.println("Eliminar ID " + u1.getId() + ":");
            boolean del = eliminarUsuario(u1.getId());
            System.out.println("Eliminado? " + del);
        }

        // LIST final
        System.out.println("Listado final:");
        listarUsuarios().forEach(System.out::println);
    }
}
