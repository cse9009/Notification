package bd.edu.su.notification;

public class InstantMessage {

    private String from,message;

    public InstantMessage() {
    }

    public InstantMessage(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }
}
