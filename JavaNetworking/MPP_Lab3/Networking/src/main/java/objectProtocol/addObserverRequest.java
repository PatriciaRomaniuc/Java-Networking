package objectProtocol;

public class addObserverRequest implements Request {
    private String user;

    public String getUser() {
        return user;
    }


    public addObserverRequest(String u){
        user = u;
    }
}
