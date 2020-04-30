package objectProtocol;

public class logoutRequest implements Request {
    private String username;

    public logoutRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
