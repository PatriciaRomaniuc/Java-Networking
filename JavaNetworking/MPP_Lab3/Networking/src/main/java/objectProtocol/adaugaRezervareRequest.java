package objectProtocol;

import domain.Rezervare;
import objectProtocol.Request;

public class adaugaRezervareRequest implements Request {
    private Rezervare rezervare;
    public adaugaRezervareRequest(Rezervare rezervare){
        this.rezervare = rezervare;
    }
    public Rezervare getRezervare(){
        return rezervare;
    }
}
