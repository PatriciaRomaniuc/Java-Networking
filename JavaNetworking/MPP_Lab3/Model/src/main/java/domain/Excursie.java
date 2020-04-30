package domain;

import java.io.Serializable;
import java.time.LocalTime;

public class Excursie extends Entity<Integer> implements Serializable {

    private String numeObiectiv, numeFirmaTransport;
    private LocalTime oraPlecarii;
    private float pret;
    private int nrLocuriDisponibile;

    public Excursie(int id, String numeObiectiv, String numeFirmaTransport, LocalTime oraPlecarii, float pret, int nrLocuriDisponibile) {
        setId(id);
        this.numeObiectiv = numeObiectiv;
        this.numeFirmaTransport = numeFirmaTransport;
        this.oraPlecarii = oraPlecarii;
        this.pret = pret;
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }
    public Integer getId() {
        return super.getId();
    }

    public void setId(int id) {
        super.setId(id);
    }

    public String getNumeObiectiv() {
        return numeObiectiv;
    }

    public void setNumeObiectiv(String numeObiectiv) {
        this.numeObiectiv = numeObiectiv;
    }

    public String getNumeFirmaTransport() {
        return numeFirmaTransport;
    }

    public void setNumeFirmaTransport(String numeFirmaTransport) {
        this.numeFirmaTransport = numeFirmaTransport;
    }

    public LocalTime getOraPlecarii() {
        return oraPlecarii;
    }

    public void setOraPlecarii(LocalTime oraPlecarii) {
        this.oraPlecarii = oraPlecarii;
    }

    public float getPret() {
        return pret;
    }

    public void setPret(float pret) {
        this.pret = pret;
    }

    public int getNrLocuriDisponibile() {
        return nrLocuriDisponibile;
    }

    public void setNrLocuriDisponibile(int nrLocuriDisponibile) {
        this.nrLocuriDisponibile = nrLocuriDisponibile;
    }
}
