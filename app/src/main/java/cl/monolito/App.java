package cl.monolito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

  // ====== Almacenamiento en memoria para los tests ======
  private static final Map<Integer, Usuario> STORE = new LinkedHashMap<>();
  private static final AtomicInteger SEQ = new AtomicInteger(0);

  // Resetea el estado entre tests
  public static void resetForTests() {
    STORE.clear();
    SEQ.set(0);
  }

  public static Usuario crearUsuario(String nombre, String email) {
    int id = SEQ.incrementAndGet();
    Usuario u = new Usuario(id, nombre, email);
    STORE.put(id, u);
    return u;
  }

  public static Usuario obtenerUsuario(int id) {
    return STORE.get(id);
  }

  public static boolean actualizarUsuario(int id, String nombre, String email) {
    Usuario u = STORE.get(id);
    if (u == null) return false;
    u.setNombre(nombre);
    u.setEmail(email);
    return true;
  }

  public static boolean eliminarUsuario(int id) {
    return STORE.remove(id) != null;
  }

  // ====== Main opcional: prueba de conexión a BD (no lo usan los tests) ======
  public static void main(String[] args) {
    System.out.println("🚀 Iniciando app...");

    try (Connection conn = DatabaseConfig.getConnection()) {
      System.out.println("✅ Conexión OK");
      System.out.println("   URL: " + conn.getMetaData().getURL());
      System.out.println("   Usuario: " + conn.getMetaData().getUserName());

      try (Statement st = conn.createStatement();
           ResultSet rs = st.executeQuery("select version(), now()")) {
        if (rs.next()) {
          System.out.println("   PostgreSQL: " + rs.getString(1));
          System.out.println("   now():      " + rs.getString(2));
        }
      }
    } catch (Exception e) {
      System.err.println("❌ Error de conexión a la base de datos: " + e.getMessage());
      System.err.println("   Revisa variables: DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, DB_SSLMODE");
      System.err.println("   Si usas Docker y Postgres en el host: DB_HOST=host.docker.internal");
    }
  }
}