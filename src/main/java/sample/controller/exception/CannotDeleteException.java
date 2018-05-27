package sample.controller.exception;

public class CannotDeleteException extends Exception {
    private String objectName;

    public CannotDeleteException(String objectName) {
        this.objectName = objectName;
    }

    public CannotDeleteException(String message, String objectName) {
        super(message);
        this.objectName = objectName;
    }

    public CannotDeleteException(String message, Throwable cause, String objectName) {
        super(message, cause);
        this.objectName = objectName;
    }
}
