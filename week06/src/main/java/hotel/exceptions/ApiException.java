package hotel.exceptions;

public class ApiException extends RuntimeException {
    private int statusCode;
    public ApiException (int statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
