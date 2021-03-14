package com.lisaeva.silenttimer.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SilentIntervalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SilentIntervalData silentIntervalData);

    @Update
    void update(SilentIntervalData silentIntervalData);

    @Delete
    void delete(SilentIntervalData silentIntervalData);

    @Query("SELECT * FROM silent_timer_data")
    List<SilentIntervalData> getAll();

    @Query("SELECT * FROM silent_timer_data WHERE id = :uuid")
    SilentIntervalData get(String uuid);
}
