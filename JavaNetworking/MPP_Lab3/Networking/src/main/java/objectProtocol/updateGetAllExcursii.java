package objectProtocol;

import domain.Excursie;

import java.util.ArrayList;
import java.util.List;

public class updateGetAllExcursii implements Response {
    private List<Excursie> list;
    public updateGetAllExcursii(List<Excursie> l){
        list = l;
    }
    public List<Excursie> getList(){
        return list;
    }
}
