package dpm.location.tracker.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.util.Locale;

@Entity
public class StoredLoctation {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Lat")
    private Double lat;

    @ColumnInfo(name = "Long")
    private Double lon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%f, %f", getLat(), getLon());
    }
}
