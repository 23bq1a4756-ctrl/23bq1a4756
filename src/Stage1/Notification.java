package Stage1;

public class Notification {

    public int id;
    public String type;
    public String message;
    public String timestamp;

    public Notification() {}

    public Notification(int id, String type, String message, String timestamp) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return id + " | " + type + " | " + message + " | " + timestamp;
    }
}
