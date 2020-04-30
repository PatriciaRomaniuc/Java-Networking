package domain;

import java.io.Serializable;

public class Rezervare extends Entity<Integer> implements Serializable {
    private String numeClient, nrTelefon;
    private int nrBilete;
    private Integer idExcursie;

    public Rezervare(int id, String numeClient, String nrTelefon, int nrBilete, Integer idExcursie) {
        setId(id);
        this.numeClient = numeClient;
        this.nrTelefon = nrTelefon;
        this.nrBilete = nrBilete;
        this.idExcursie = idExcursie;
    }
    public Integer getId() {
        return super.getId();
    }

    public void setId(int id) {
        super.setId(id);
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public String getNrTelefon() {
        return nrTelefon;
    }

    public void setNrTelefon(String nrTelefon) {
        this.nrTelefon = nrTelefon;
    }

    public int getNrBilete() {
        return nrBilete;
    }

    public void setNrBilete(int nrBilete) {
        this.nrBilete = nrBilete;
    }

    public Integer getIdExcursie() {
        return idExcursie;
    }

    public void setIdExcursie(Integer idExcursie) {
        this.idExcursie = idExcursie;
    }


}
