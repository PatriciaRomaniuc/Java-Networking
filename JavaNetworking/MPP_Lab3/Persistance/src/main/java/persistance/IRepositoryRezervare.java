package persistance;

import domain.Entity;
import domain.Excursie;
import domain.Rezervare;

import java.util.List;

public interface IRepositoryRezervare<I extends Number, R extends Entity<Integer>> {
    public List<Rezervare> findAll() ;
    Rezervare findOne(int id);
    void save(Rezervare entity);
    void delete(int id);
    void update(Rezervare entity);

}
