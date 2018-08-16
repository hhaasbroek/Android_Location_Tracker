package dpm.location.tracker.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {storedLoctation.class},version = 1)
public abstract class LocationDb extends RoomDatabase {
    public abstract Dao locationDao();
}
