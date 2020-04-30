package objectProtocol;

import domain.Excursie;

public class updateExcursieRequest implements Request {
    private Excursie excursie;
    public updateExcursieRequest(Excursie e){
        excursie = e;
    }


    public Excursie getExcursie1() {
        return excursie;
    }

}
