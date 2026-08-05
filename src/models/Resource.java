package models;

/**
 * Represents movable or consumable campus resources.
 */
public class Resource {

    private int resourceId;
    private String resourceType;
    private int homeLocationId;
    private int capacity;
    private String availabilityStatus;


    public Resource(
            int resourceId,
            String resourceType,
            int homeLocationId,
            int capacity,
            String availabilityStatus
    ) {

        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.homeLocationId = homeLocationId;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }


    public int getResourceId() {
        return resourceId;
    }


    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }


    public String getResourceType() {
        return resourceType;
    }


    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public int getHomeLocationId() {
        return homeLocationId;
    }


    public void setHomeLocationId(int homeLocationId) {
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