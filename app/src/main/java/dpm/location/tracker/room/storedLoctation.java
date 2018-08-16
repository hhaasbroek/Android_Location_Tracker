package dpm.location.tracker.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

@Entity
public class storedLoctation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Lat")
    private String lat;

    @ColumnInfo(name = "Long")
    private String lon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

}
