package dpm.location.tracker.room;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;

public class LocationRepository extends Application {

    private static final String DATABASE_NAME = "MyDatabase";

    private static final String PREFERENCES = "RoomDemo.preferences";

    private static final String KEY_FORCE_UPDATE = "force_update";

    private LocationDb database;

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        database = Room.databaseBuilder(getApplicationContext(), LocationDb.class, DATABASE_NAME)
                .build();
    }

    public LocationDb getDB() {
        return database;
    }

    public boolean isForceUpdate() {
        return getSP().getBoolean(KEY_FORCE_UPDATE, true);
    }

    public void setForceUpdate(boolean force) {
        SharedPreferences.Editor edit = getSP().edit();
        edit.putBoolean(KEY_FORCE_UPDATE, force);
        edit.apply();
    }

    private SharedPreferences getSP() {
        return getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }
}
