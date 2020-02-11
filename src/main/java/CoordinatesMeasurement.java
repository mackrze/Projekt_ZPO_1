/**
 * Part of Measurement class value
 */

public class CoordinatesMeasurement {


    private double latitude;
    private double longitude;

    public CoordinatesMeasurement() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "CoordinatesMeasurement{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}


