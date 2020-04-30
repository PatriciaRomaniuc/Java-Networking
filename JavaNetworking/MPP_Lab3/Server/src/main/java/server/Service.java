package server;

import domain.Excursie;
import domain.Rezervare;
import domain.User;
import observer.applicationObserver;
import persistance.*;
import service.IService;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
    private IRepositoryRezervare repoRezervare;
    private IRepositoryUsers repoUsers;
    private IRepositoryExcursie repoExcursie;
    private Map<String, applicationObserver> loggedClients;
    private int id;

    public Service(IRepositoryExcursie re,IRepositoryUsers ru,IRepositoryRezervare rr){
        repoExcursie = re;
        repoRezervare = rr;
        repoUsers = ru;
        loggedClients=new ConcurrentHashMap<>();
        id = repoExcursie.findAll().size();

    }

    @Override
    public synchronized void login(User user, applicationObserver applicationObserver) throws PersistanceException {
        System.out.println("aici");
        User user1 = repoUsers.findOne(user.getUsername());
        System.out.println("gasit");
        if (user1 == null)
            throw new PersistanceException("Incorect username");
        if (user1 != null && !user1.getPassword().equals(user.getPassword())) {
            throw new PersistanceException("Incorect password");
        }
        loggedClients.put(user1.getUsername(),applicationObserver);
    }

    public synchronized void logout(String usename) throws PersistanceException {
            loggedClients.remove(usename);
    }


    public synchronized void saveExcursie(String numeObiectiv, String numeFirmaTransport, LocalTime oraPlecarii, Float pret, int nrLocuriDisponibile) {
        id++;
        repoExcursie.save(new Excursie(id, numeObiectiv, numeFirmaTransport, oraPlecarii, pret, nrLocuriDisponibile));
    }

    @Override
    public synchronized Excursie findOneExcursie(int id) throws PersistanceException {
        return repoExcursie.findOne(id);
    }

    public synchronized List<Excursie> findAllExcursii() {
        return  repoExcursie.findAll();
    }

    public synchronized List<Excursie> getExcursiiTableFiltru(String obiectiv, String dupa, String inainte) {
        return repoExcursie.findAllObiectivOra(obiectiv, dupa, inainte);
    }

    public synchronized void updateExcursie(Excursie excursie) throws PersistanceException {
        repoExcursie.update(excursie);
        for(applicationObserver observer:loggedClients.values())
            observer.updateTrips(findAllExcursii());
    }

    public synchronized void addRezervare(Rezervare rezervare) {
        repoRezervare.save(rezervare);
    }



}
