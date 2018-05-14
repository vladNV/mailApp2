package sample.controller.exception;

public class ClientException extends Exception {
    private String causedBy;

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, String causedBy) {
        super(message);
        this.causedBy = causedBy;
    }

    public String getCausedBy() {
        return causedBy;
    }
}
