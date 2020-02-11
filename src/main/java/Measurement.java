/**
 * Class which will be map of results given by JSON
 */


public class Measurement {

    private String location;
    private String parameter;
    private DateMeasurement date;
    private double value;
    private String unit;
    private CoordinatesMeasurement coordinates;
    private String country;
    private String city;

    public Measurement() {
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "location='" + location + '\'' +
                ", parameter='" + parameter + '\'' +
                ", date=" + date +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                ", coordinates=" + coordinates +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public DateMeasurement getDate() {
        return date;
    }

    public void setDate(DateMeasurement date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public CoordinatesMeasurement getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesMeasurement coordinates) {
        this.coordinates = coordinates;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
