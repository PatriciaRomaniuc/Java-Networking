package objectProtocol;

import domain.Excursie;

import java.util.ArrayList;
import java.util.List;

public class getExcursiiResponse implements Response {
    private List<Excursie> list;
    public getExcursiiResponse(List<Excursie> l){
        this.list = l;
    }
    public List<Excursie> getList(){
        return list;
    }
}
