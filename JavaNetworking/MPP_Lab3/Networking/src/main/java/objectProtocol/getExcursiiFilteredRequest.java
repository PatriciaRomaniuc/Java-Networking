package objectProtocol;

import java.time.LocalTime;

public class getExcursiiFilteredRequest implements Request {
    String obiectiv;
    String dupaOra;
    String inainteDe;
    public getExcursiiFilteredRequest(String o,String d,String i){
        obiectiv = o;
        dupaOra = d;
        inainteDe = i;
    }

    public String getObiectiv() {
        return obiectiv;
    }

    public String getDupaOra() {
        return dupaOra;
    }

    public String getInainteDe() {
        return inainteDe;
    }
}
