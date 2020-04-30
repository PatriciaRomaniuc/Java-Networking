package persistance;

import domain.Rezervare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import Utils.*;

public class RepositoryRezervare implements IRepositoryRezervare<Integer,Rezervare> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    public RepositoryRezervare(Properties properties){
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void save(Rezervare rez) {
        logger.traceEntry("saving reservation {}",rez);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("insert into Rezervari values (?,?,?,?,?)")){
            preparedStatement.setInt(1,rez.getId());
            preparedStatement.setString(2,rez.getNumeClient());
            preparedStatement.setString(3,rez.getNrTelefon());
            preparedStatement.setInt(4,rez.getNrBilete());
            preparedStatement.setInt(5,rez.getIdExcursie());
            int result = preparedStatement.executeUpdate();
            con.close();
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("error bd "+e);
        }
        logger.traceExit();
    }
    @Override
    public void delete(int idRez) {
        logger.traceEntry("Deleting reservation with id {}",idRez);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("delete from Rezervari where id=?")){
            preparedStatement.setInt(1,idRez);
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
    public void update(Rezervare entity) {
        logger.traceEntry("updating reservation with id {}",entity.getId());
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("UPDATE Rezervari SET numeClient=?,nrTelefon=?,nrBilete=?,idExcursie=? WHERE id=?")){
            preparedStatement.setInt(5,entity.getId());
            preparedStatement.setString(1,entity.getNumeClient());
            preparedStatement.setString(2,entity.getNrTelefon());
            preparedStatement.setInt(3,entity.getNrBilete());
            preparedStatement.setInt(4,entity.getIdExcursie());
            int result = preparedStatement.executeUpdate();
            if(result == 0)
                logger.traceExit("reservation not found");
            else
                logger.traceExit("reservation updated");
            con.close();
        }
        catch (SQLException e){
            logger.error(e);
            System.out.println("db error "+e);
        }
    }
    @Override
        public Rezervare findOne(int idRez){
        logger.traceEntry("Finding reservation with id {}",idRez);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from Rezervari where id=?")){
            preparedStatement.setInt(1,idRez);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    String numeClient = result.getString("numeClient");
                    String telefonClient = result.getString("nrTelefon");
                    int numarBilete = result.getInt("nrBilete");
                    int idExcursie = result.getInt("idExcursie");
                    int idRezervare = result.getInt("id");
                    Rezervare rez = new Rezervare(idRezervare, numeClient,telefonClient,numarBilete,idExcursie);
                    logger.traceExit(rez);
                    return rez;
                }
                con.close();
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("db error "+e);
        }
        logger.traceExit("no reservation found with id {}",idRez);
        return null;
    }

    public List<Rezervare> findAll() {
        Connection con=dbUtils.getConnection();
        List<Rezervare> rezervari=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Rezervari")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    String numeClient = result.getString("numeClient");
                    String telefonClient = result.getString("nrTelefon");
                    int numarBilete = result.getInt("nrBilete");
                    int idExcursie = result.getInt("idExcursie");
                    int idRezervare = result.getInt("id");
                    Rezervare rez = new Rezervare(idRezervare, numeClient,telefonClient,numarBilete,idExcursie);
                    rezervari.add(rez);
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
        logger.traceExit(rezervari);
        return rezervari;
    }


}
