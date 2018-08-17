package dpm.location.tracker.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;
import java.util.Locale;

@Entity
public class StoredLoctation {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "IsBackground")
    private Boolean isBackground;

    @ColumnInfo(name = "Lat")
    private Double lat;

    @ColumnInfo(name = "Long")
    private Double lon;

    @ColumnInfo(name = "Address")
    private String address;

    @ColumnInfo(name = "Timestamp")
    private Long timestamp;

    public String getAddress() {
        return address;
    }

    public Boolean getBackground() {
        return isBackground;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setBackground(final Boolean background) {
        isBackground = background;
    }

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%b - %s: %s: - %f, %f", isBackground, new Date(getTimestamp()).toGMTString(),getAddress(), getLat(), getLon());
    }
}
