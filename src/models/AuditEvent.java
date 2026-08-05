package models;

/**
 * Represents a system audit trail event.
 */
public class AuditEvent {


    private int eventId;
    private String action;
    private String actor;
    private String timestamp;
    private String description;


    public AuditEvent(
            int eventId,
            String action,
            String actor,
            String timestamp,
            String description
    ) {

        this.eventId = eventId;
        this.action = action;
        this.actor = actor;
        this.timestamp = timestamp;
        this.description = description;
    }


    public int getEventId() {
        return eventId;
    }


    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


    public String getAction() {
        return action;
    }


    public void setAction(String action) {
        this.action = action;
    }


    public String getActor() {
        return actor;
    }


    public void setActor(String actor) {
        this.actor = actor;
    }


    public String getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }
}