package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//1) Crearemos la clase para poder tener conexión con la base de datos
public class Conexion {

    //2) Creamos el url string para la conexión
    private static final String URL = "jdbc:mysql://localhost:3306/mimir_petshop";
    //3) Colocamos el usuario de nuestra bdd
    private static final String USER = "root";
    //4) Colocamos la contraseña de nuestra bdd (en este caso en blanco porque no tenemos ninguna contraseña)
    private static final String PASS = "";

    //5) Hacemos un constructor para poder instanciar en las demas clases y tener una conexión por peticiones
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
