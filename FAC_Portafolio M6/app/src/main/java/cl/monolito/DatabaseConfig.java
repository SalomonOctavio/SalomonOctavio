package cl.monolito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConfig {

  private DatabaseConfig() {}

  public static Connection getConnection() throws SQLException {
    // Lee variables de entorno con valores por defecto
    String host = getenvOrDefault("DB_HOST", "localhost");
    String port = getenvOrDefault("DB_PORT", "5432");
    String db   = getenvOrDefault("DB_NAME", "monolito_ecommerce");
    String user = getenvOrDefault("DB_USER", "postgres");
    String pass = getenvOrDefault("DB_PASSWORD", "postgres");
    // Para AWS RDS puedes usar DB_SSLMODE=require
    String ssl  = getenvOrDefault("DB_SSLMODE", "disable");

    String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);

    // (Opcional) registrar driver explícitamente por si tu runtime lo requiere
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException ignore) {
      // Con JDBC 4+ normalmente no es necesario
    }

    Properties props = new Properties();
    props.setProperty("user", user);
    props.setProperty("password", pass);
    props.setProperty("sslmode", ssl);

    return DriverManager.getConnection(url, props);
  }

  private static String getenvOrDefault(String key, String def) {
    String v = System.getenv(key);
    return (v == null || v.isBlank()) ? def : v;
  }
}