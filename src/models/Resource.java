package models;

/**
 * Represents movable or consumable campus resources.
 */
public class Resource {

    private String resourceId;
    private String resourceType;
    private String homeLocationId;
    private int capacity;
    private String availabilityStatus;


    public Resource(
            String resourceId,
            String resourceType,
            String homeLocationId,
            int capacity,
            String availabilityStatus
    ) {

        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.homeLocationId = homeLocationId;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }


    public String getResourceId() {
        return resourceId;
    }


    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }


    public String getResourceType() {
        return resourceType;
    }


    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public String getHomeLocationId() {
        return homeLocationId;
    }


    public void setHomeLocationId(String homeLocationId) {
        this.homeLocationId = homeLocationId;
    }


    public int getCapacity() {
        return capacity;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    public String getAvailabilityStatus() {
        return availabilityStatus;
    }


    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}