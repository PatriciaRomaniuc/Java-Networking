package persistance;

import domain.Entity;
import domain.Excursie;

import java.util.ArrayList;
import java.util.List;

public interface IRepositoryExcursie<I extends Number, E extends Entity<Integer>>  {
    public List<Excursie> findAll() ;
    Excursie findOne(int id);
    public List<Excursie> findAllObiectivOra(String obiectiv, String dupa, String inainte) ;
    void save(Excursie entity);
    void delete(int id);
    void update(Excursie entity);


    }
