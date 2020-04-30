package persistance;
import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryUsers implements IRepositoryUsers {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    public RepositoryUsers(Properties properties){
        dbUtils = new JdbcUtils( properties);
    }
    public void save(User user ) {
        logger.traceEntry("saving user {}",user);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("insert into Users values (?,?)")){
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getPassword());
            int result = preparedStatement.executeUpdate();
            con.close();
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("error bd "+e);
        }
        logger.traceExit();
    }

    public void delete(String username) {
        logger.traceEntry("Deleting user with username {}",username);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("delete from Users where username=?")){
            preparedStatement.setString(1,username);
            int result = preparedStatement.executeUpdate();
            con.close();
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("db error "+e);
        }
        logger.traceExit();
    }
    public void update(User entity) {
        logger.traceEntry("updating user with username {}",entity.getUsername());
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("UPDATE Users SET password=? WHERE username=?")){
            preparedStatement.setString(1,entity.getPassword());
            preparedStatement.setString(2,entity.getUsername());

            int result = preparedStatement.executeUpdate();
            if(result == 0)
                logger.traceExit("user not found");
            else
                logger.traceExit("user updated");
            con.close();
        }
        catch (SQLException e){
            logger.error(e);
            System.out.println("db error "+e);
        }
    }
    public User findOne(String username) {
        logger.traceEntry("finding user with username {}",username);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from Users where username=?")){
            preparedStatement.setString(1,username);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    String username1 = result.getString("username");
                    String pass = result.getString("password");
                    User user = new User(username1,pass);
                    logger.traceExit(user);
                    return user;
                }
                con.close();
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.out.println("error bd "+ e);
        }
        logger.traceExit("no user with that username");
        return null;
    }

    @Override
    public List<User> findAll() {
        Connection con=dbUtils.getConnection();
        List<User> users=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Users")) {
            try(ResultSet rs=preStmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("username");
                    String passwd=rs.getString("password");
                    User user = new User(name,passwd);
                    users.add(user);
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
        logger.traceExit(users);
        return users;
    }
}
