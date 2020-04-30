package objectProtocol;

import domain.Excursie;

public class findExcursieResponse implements Response {
    private Excursie excursie;
    public findExcursieResponse(Excursie exc){
        excursie = exc;
    }
    public Excursie getExcursie1(){
        return excursie;
    }
}
