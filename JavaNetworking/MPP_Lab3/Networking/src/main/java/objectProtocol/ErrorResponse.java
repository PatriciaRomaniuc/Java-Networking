package objectProtocol;

public class ErrorResponse implements Response {
    private String error;
    public ErrorResponse(String e) {
        error = e;
    }
    public String getError(){
        return error;
    }
}
