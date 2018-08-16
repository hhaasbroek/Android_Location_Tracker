package dpm.location.tracker.room;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@android.arch.persistence.room.Dao
public interface Dao {

    @Query("SELECT * FROM storedLoctation")
    List<storedLoctation> getAll();

    @Insert
    void insertAll(List<storedLoctation> location);

    @Update
    void update(storedLoctation location);

}
