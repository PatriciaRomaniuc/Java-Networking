package observer;

import persistance.PersistanceException;
import domain.Excursie;


import java.util.ArrayList;
import java.util.List;

public interface applicationObserver {
    public void updateTrips(List<Excursie> excursies) throws PersistanceException;
}
