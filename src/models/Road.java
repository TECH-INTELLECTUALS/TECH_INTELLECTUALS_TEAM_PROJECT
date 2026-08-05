package models;

/**
 * Represents a connectivity link between two campus locations.
 */
public class Road {

    private int roadId;
    private int fromLocationId;
    private int toLocationId;
    private double distanceKm;
    private double travelTimeMin;
    private double conditionWeight;


    public Road(
            int roadId,
            int fromLocationId,
            int toLocationId,
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


    public int getRoadId() {
        return roadId;
    }


    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }


    public int getFromLocationId() {
        return fromLocationId;
    }


    public void setFromLocationId(int fromLocationId) {
        this.fromLocationId = fromLocationId;
    }


    public int getToLocationId() {
        return toLocationId;
    }


    public void setToLocationId(int toLocationId) {
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