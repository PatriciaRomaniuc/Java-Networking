package objectProtocol;

import domain.User;

public class loginRequest implements Request {
    private User user;
    public loginRequest(User u){
        user = u;
    }
    public User getUser(){
        return user;
    }
}
