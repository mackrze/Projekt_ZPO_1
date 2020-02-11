/**
 * Part of Measurement class value
 */


public class DateMeasurement {
    private String utc;
    private String local;


    public DateMeasurement() {
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    @Override
    public String toString() {
        return "DateMeasurement{" +
                "utc='" + utc + '\'' +
                ", local='" + local + '\'' +
                '}';
    }
}

