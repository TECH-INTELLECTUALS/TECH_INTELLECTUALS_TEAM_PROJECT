package models;

/**
 * Represents a connectivity link between two campus locations.
 */
public class Road {

    private String roadId;
    private String fromLocationId;
    private String toLocationId;
    private double distanceKm;
    private double travelTimeMin;
    private double conditionWeight;


    public Road(
            String roadId,
            String fromLocationId,
            String toLocationId,
            double distanceKm,
            double travelTimeMin,
            double conditionWeight
    ) {

        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.travelTimeMin = travelTimeMin;
        this.conditionWeight = conditionWeight;
    }


    public String getRoadId() {
        return roadId;
    }


    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }


    public String getFromLocationId() {
        return fromLocationId;
    }


    public void setFromLocationId(String fromLocationId) {
        this.fromLocationId = fromLocationId;
    }


    public String getToLocationId() {
        return toLocationId;
    }


    public void setToLocationId(String toLocationId) {
        this.toLocationId = toLocationId;
    }


    public double getDistanceKm() {
        return distanceKm;
    }


    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }


    public double getTravelTimeMin() {
        return travelTimeMin;
    }


    public void setTravelTimeMin(double travelTimeMin) {
        this.travelTimeMin = travelTimeMin;
    }


    public double getConditionWeight() {
        return conditionWeight;
    }


    public void setConditionWeight(double conditionWeight) {
        this.conditionWeight = conditionWeight;
    }
}