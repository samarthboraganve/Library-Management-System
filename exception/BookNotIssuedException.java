package exception;

public class BookNotIssuedException extends Exception {
    public BookNotIssuedException(String message) {
        super(message);
    }
}
