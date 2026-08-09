package models;

/**
 * Encapsulates a service request submitted by students or staff.
 */
public class ServiceRequest {

    private String requestId;
    private String sourceLocationId;
    private String destinationLocationId;
    private String category;
    private int urgency;
    private String timeSubmitted;
    private String deadline;
    private String status;


    public ServiceRequest(
            String requestId,
            String sourceLocationId,
            String destinationLocationId,
            String category,
            int urgency,
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


    public String getRequestId() {
        return requestId;
    }


    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public String getSourceLocationId() {
        return sourceLocationId;
    }


    public void setSourceLocationId(String sourceLocationId) {
        this.sourceLocationId = sourceLocationId;
    }


    public String getDestinationLocationId() {
        return destinationLocationId;
    }


    public void setDestinationLocationId(String destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public int getUrgency() {
        return urgency;
    }


    public void setUrgency(int urgency) {
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