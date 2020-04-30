package persistance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties jdbcProps;
    private Connection instance=null;
    private static final Logger logger= LogManager.getLogger();

    public JdbcUtils(Properties props){
        jdbcProps=props;
    }

    private Connection getNewConnection() {
        //logger.traceEntry("Creare conexiune noua");
        String url = jdbcProps.getProperty("jdbc.url");
        // logger.info("conectare la baza de date ... {}", url);
        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            //  logger.error(e);
            System.out.println("Eroare la conectare " + e);
        }
        return con;
    }

    public Connection getConnection(){
        //logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            //     logger.error(e);
            System.out.println("Eroare BD "+e);
        }
        //  logger.traceExit(instance);
        return instance;
    }
}
