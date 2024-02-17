import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    String host, user, password; //Datos para acceder a la base de datos
    String mensaje;
    /*Constructor*/
    public ConexionDB(String host, String user, String password){
        this.host = host;
        this.user = user;
        this.password = password;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /*Constructor vacio*/
    public ConexionDB(){

    }
    public void ConexionLocal(String host, String user, String password){
        try (Connection connection = DriverManager.getConnection(host, user, password)) {
            if(connection != null){
                mensaje = "Conexión correcta";
                System.out.println(mensaje);
            }else {
                mensaje="";
                System.out.println(mensaje);
            }
        }
        catch (SQLException e){
            System.out.println(e);
            mensaje = "Algo salió mal :(";
            System.out.println(mensaje);
        }catch (Exception ex){
            ex.getMessage();
        }
    }
}
