import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private String host="jdbc:mysql://38.46.218.114:3306/intexcom_vicishop";
    private String user="intexcom_kevinC";
    private String password="Encebollado0401."; //Datos para acceder a la base de datos
    public String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /*Constructor vacio*/
    public ConexionDB(){}

    public Connection ConexionLocal(){
        Connection connection=null;
        try {
             connection = DriverManager.getConnection(this.host, this.user, this.password);
        }
        catch (SQLException e){
            System.out.println(e);
            mensaje = "Algo salió mal :(";
            System.out.println(mensaje);
        }
        return connection;
    }
}
