package Models;

import Exceptions.CredecialesInvalidasException;
import Persistencia.UsuarioRepositorio;


public class Usuario {
    private String user;
    private String contrasenia;

    public Usuario(String user, String contrasenia) {
        this.user = user;
        this.contrasenia = contrasenia;
    }

    public String getUser() {
        return user;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "user='" + user + '\'' +
                ", contrasenia='******'" +
                '}';
    }

    public static Usuario autenticar(String user, String pass, UsuarioRepositorio repo) throws CredecialesInvalidasException {
        Usuario u = repo.buscarUsuario(user);
        if (u == null) {
            throw new CredecialesInvalidasException("Usuario no encontrado");
        }
        if (!u.getContrasenia().equals(pass)) {
            throw new CredecialesInvalidasException("Contraseña incorrecta");
        }
        return u;
    }
}

