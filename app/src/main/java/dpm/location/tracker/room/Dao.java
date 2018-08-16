package dpm.location.tracker.room;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@android.arch.persistence.room.Dao
public interface Dao {

    @Query("SELECT * FROM StoredLoctation")
    List<StoredLoctation> getAll();

    @Insert
    void insertAll(List<StoredLoctation> location);

    @Update
    void update(StoredLoctation location);

    @Query("DELETE FROM StoredLoctation")
    public void nukeTable();

}
