import persistance.RepositoryExcursie;
import persistance.RepositoryRezervare;
import persistance.RepositoryUsers;
import service.IService;
import server.Service;
import utils.AbstractServer;
import utils.ObjectConcurrentServer;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;


public class StartServer  {
    private static Properties getProperty(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(new FileReader("baza.config1"));

            System.out.println("Proprietati setate. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.out.println("Nu am gasit bd.config "+e);
            return null;
        }
        return serverProps;
    }

    public static void main(String[] args) {
        RepositoryRezervare repositoryRezervare= new RepositoryRezervare(getProperty());
        RepositoryExcursie repositoryExcursie=new RepositoryExcursie(getProperty());
        RepositoryUsers repositoryUsers=new RepositoryUsers(getProperty());
        IService service= new Service(repositoryExcursie, repositoryUsers, repositoryRezervare);
        AbstractServer server = new ObjectConcurrentServer(55555, service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }

}
