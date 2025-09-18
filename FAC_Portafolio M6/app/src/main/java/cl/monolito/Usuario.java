package cl.monolito;

import java.util.Objects;

public class Usuario {
  private int id;
  private String nombre;
  private String email;

  public Usuario() {}

  public Usuario(int id, String nombre, String email) {
    this.id = id;
    this.nombre = nombre;
    this.email = email;
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Usuario)) return false;
    Usuario usuario = (Usuario) o;
    return id == usuario.id &&
           Objects.equals(nombre, usuario.nombre) &&
           Objects.equals(email, usuario.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nombre, email);
  }

  @Override
  public String toString() {
    return "Usuario{id=" + id + ", nombre='" + nombre + "', email='" + email + "'}";
  }
}