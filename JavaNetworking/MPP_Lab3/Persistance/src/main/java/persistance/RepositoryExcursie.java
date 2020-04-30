package persistance;

import domain.Excursie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryExcursie implements IRepositoryExcursie<Integer, Excursie> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    public RepositoryExcursie(Properties properties){
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void save(Excursie e) {
        logger.traceEntry("saving trip {}",e);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("insert into Excursii values (?,?,?,?,?,?)")){
            preparedStatement.setInt(1,e.getId());
            preparedStatement.setString(2,e.getNumeObiectiv());
            preparedStatement.setString(3,e.getNumeFirmaTransport());
            preparedStatement.setString(4,e.getOraPlecarii().toString());
            preparedStatement.setFloat(5,e.getPret());
            preparedStatement.setInt(6,e.getNrLocuriDisponibile());

            int result = preparedStatement.executeUpdate();
            con.close();
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.out.println("error bd "+ex);
        }
        logger.traceExit();
    }
    @Override
    public void delete(int id) {
        logger.traceEntry("Deleting trip with id {}",id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("delete from Excursii where id=?")){
            preparedStatement.setInt(1,id);
            int result = preparedStatement.executeUpdate();
            con.close();
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("db error "+e);
        }
        logger.traceExit();
    }
    @Override
    public Excursie findOne(int id) {
        logger.traceEntry("finding trip with id {}",id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Excursii WHERE id=?"))
        {
            preparedStatement.setInt(1,id);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    String obiectiv = result.getString("numeObiectiv");
                    String firmaTransport = result.getString("numeFirmaTransport");
                    LocalTime time = LocalTime.parse(result.getString("oraPlecarii"));
                    float pret = result.getFloat("pret");
                    int locuriLibere = result.getInt("nrLocuriDisponibile");
                    int id1 = result.getInt("id");
                    Excursie excursie = new Excursie(id1,obiectiv,firmaTransport,time, pret,locuriLibere);
                    logger.traceExit(excursie);
                    return excursie;
                }
                con.close();
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("db error "+e);
        }
        logger.traceExit("nu s-a gasit excursie cu id {}",id);
        return null;
    }

    @Override
    public void update(Excursie excursie ) {
        logger.traceEntry("updating trip with id {}",excursie.getId());
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("UPDATE Excursii SET numeObiectiv=?,numeFirmaTransport=?,oraPlecarii=?,pret=?,nrLocuriDisponibile=? WHERE id=?")){
            preparedStatement.setString(1,excursie.getNumeObiectiv());
            preparedStatement.setString(2,excursie.getNumeFirmaTransport());
            preparedStatement.setString(3,excursie.getOraPlecarii().toString());
            preparedStatement.setFloat(4,excursie.getPret());
            preparedStatement.setInt(5,excursie.getNrLocuriDisponibile());
            preparedStatement.setInt(6,excursie.getId());
            int result = preparedStatement.executeUpdate();
            if(result == 0)
                logger.traceExit("trip not found");
            else
                logger.traceExit("trip updated");
            con.close();
        }
        catch (SQLException e){
            logger.error(e);
            System.out.println("bd error "+e);
        }
    }

    public List<Excursie> findAll() {
        Connection con=dbUtils.getConnection();
        List<Excursie> excursii=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Excursii")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String obiectiv = result.getString("numeObiectiv");
                    String firmaTransport = result.getString("numeFirmaTransport");
                    LocalTime time = LocalTime.parse(result.getString("oraPlecarii"));
                    float pret = result.getFloat("pret");
                    int locuriLibere = result.getInt("nrLocuriDisponibile");
                    Excursie ex = new Excursie(id, obiectiv,firmaTransport,time,pret,locuriLibere);
                    excursii.add(ex);
                }}}
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.traceExit(excursii);
        return excursii;
    }
    public List<Excursie> findAllObiectivOra(String obiectiv, String dupa, String inainte) {
        logger.traceEntry("finding all trips with obiectiv {}",obiectiv);
        List<Excursie> all = new ArrayList<>();
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM Excursii WHERE numeObiectiv=? AND ?<=oraPlecarii AND oraPlecarii<=?")){
            preparedStatement.setString(1,obiectiv);
            preparedStatement.setString(2,dupa);
            preparedStatement.setString(3,inainte);
            try(ResultSet result = preparedStatement.executeQuery()){
                while (result.next()) {
                    String obiectiv1 = result.getString("numeObiectiv");
                    String firmaTransport = result.getString("numeFirmaTransport");
                    LocalTime time = LocalTime.parse(result.getString("oraPlecarii"));
                    Float pret = result.getFloat("pret");
                    Integer locuriLibere = result.getInt("nrLocuriDisponibile");
                    int id = result.getInt("id");
                    Excursie excursie = new Excursie(id,obiectiv1,firmaTransport,time,pret,locuriLibere);
                    all.add(excursie);
                }
                conn.close();
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.out.println("db error "+ex);
        }
        logger.traceExit(all);
        return all;
    }
}
