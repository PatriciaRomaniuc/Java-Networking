package objectProtocol;

public class findExcursieRequest implements Request {
    private int id;
    public findExcursieRequest(int idE){
        id = idE;
    }
    public int getIdExc(){
        return id;
    }
}
