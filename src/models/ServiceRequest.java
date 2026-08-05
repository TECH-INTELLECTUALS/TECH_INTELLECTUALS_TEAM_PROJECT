package models;

/**
 * Encapsulates a service request submitted by students or staff.
 */
public class ServiceRequest {

    private int requestId;
    private int sourceLocationId;
    private int destinationLocationId;
    private String category;
    private String urgency;
    private String timeSubmitted;
    private String deadline;
    private String status;


    public ServiceRequest(
            int requestId,
            int sourceLocationId,
            int destinationLocationId,
            String category,
            String urgency,
            String timeSubmitted,
            String deadline,
            String status
    ) {

        this.requestId = requestId;
        this.sourceLocationId = sourceLocationId;
        this.destinationLocationId = destinationLocationId;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }


    public int getRequestId() {
        return requestId;
    }


    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }


    public int getSourceLocationId() {
        return sourceLocationId;
    }


    public void setSourceLocationId(int sourceLocationId) {
        this.sourceLocationId = sourceLocationId;
    }


    public int getDestinationLocationId() {
        return destinationLocationId;
    }


    public void setDestinationLocationId(int destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String getUrgency() {
        return urgency;
    }


    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }


    public String getTimeSubmitted() {
        return timeSubmitted;
    }


    public void setTimeSubmitted(String timeSubmitted) {
        this.timeSubmitted = timeSubmitted;
    }


    public String getDeadline() {
        return deadline;
    }


    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }
}