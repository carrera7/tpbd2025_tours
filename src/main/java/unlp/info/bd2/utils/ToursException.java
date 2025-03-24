package unlp.info.bd2.utils;

public class ToursException extends Exception{

    private String message;

    public ToursException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
