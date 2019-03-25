package sayrunjah.atmlocator.classes;

/**
 * Created by CHARLES on 24/02/2017.
 */
public class AtmClass {
    String bankname,location,distance,duration;
    String lat,lng;

    public AtmClass(String bankname, String location, String distance) {
        this.bankname = bankname;
        this.location = location;
        this.distance = distance;
    }

    public AtmClass(String bankname, String location, String distance, String duration, String lat, String lng) {
        this.bankname = bankname;
        this.location = location;
        this.distance = distance;
        this.duration = duration;
        this.lat = lat;
        this.lng = lng;
    }

    public AtmClass(String bankname, String location, String lat, String lng) {
        this.bankname = bankname;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
    }

    public AtmClass(String distance, String duration) {
        this.distance = distance;
        this.duration = duration;
    }


    public String getBankname() {
        return bankname;
    }

    public String getLocation() {
        return location;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
