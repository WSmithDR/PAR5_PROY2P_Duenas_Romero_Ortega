package Models;

import Exceptions.CredecialesInvalidasException;
import Persistencia.UsuarioRepositorio;
import android.content.Context;
import com.example.par5_proy2p_duenas_romero_ortega.R;

public class Usuario {
    private String id;
    private String user;
    private String contrasenia;

    /** ID del usuario que ha iniciado sesión actualmente */
    public static String logged_user_id;

    public Usuario(String id, String user, String contrasenia) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "user='" + user + '\'' +
                ", contrasenia='******'" +
                '}';
    }


    /**
     * Autentica a un usuario con su nombre de usuario y contraseña.
     * 
     * @param context Contexto de la aplicación Android
     * @param username Nombre de usuario
     * @param pass Contraseña sin encriptar
     * @return Usuario autenticado
     * @throws CredecialesInvalidasException Si las credenciales son incorrectas
     */
    public static Usuario autenticar(Context context, String username, String pass) throws CredecialesInvalidasException {
        Usuario u = UsuarioRepositorio.buscarUsuarioPorUsername(username);
        if (u == null) {
            throw new CredecialesInvalidasException(context.getString(R.string.usuario_no_encontrado));
        }
        if (!u.getContrasenia().equals(pass)) {
            throw new CredecialesInvalidasException(context.getString(R.string.contrasena_incorrecta));
        }
        return u;
    }
}

