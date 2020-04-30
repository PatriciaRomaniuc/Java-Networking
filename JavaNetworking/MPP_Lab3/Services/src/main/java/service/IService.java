package service;

import domain.Excursie;
import domain.Rezervare;
import domain.User;
import observer.applicationObserver;
import persistance.PersistanceException;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface IService {
    //public void saveExcursie(String numeObiectiv, String numeFirmaTransport, LocalTime oraPlecarii, Float pret, int nrLocuriDisponibile);

    public Excursie findOneExcursie(int id) throws PersistanceException;

    public List<Excursie> findAllExcursii() throws PersistanceException;

    public List<Excursie> getExcursiiTableFiltru(String obiectiv, String dupa, String inainte) throws PersistanceException;

    public void updateExcursie(Excursie excursie) throws PersistanceException;

    public void addRezervare(Rezervare rezervare) throws PersistanceException;

    public void login(User user, applicationObserver observer) throws PersistanceException;
    public void logout(String username) throws PersistanceException;
}

