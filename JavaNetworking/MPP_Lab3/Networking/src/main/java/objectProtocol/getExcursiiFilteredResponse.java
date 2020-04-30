package objectProtocol;

import domain.Excursie;

import java.util.ArrayList;
import java.util.List;

public class getExcursiiFilteredResponse implements Response {
    private List<Excursie> list;

    public List<Excursie> getList() {
        return list;
    }

    public getExcursiiFilteredResponse(List<Excursie> l){
        list = l;
    }
}
