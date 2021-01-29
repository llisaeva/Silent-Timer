package com.lisaeva.silenttimer.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface SilentAlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)//TODO !!
    Completable insert(SilentAlarmData silentAlarmData);

    @Update
    Completable update(SilentAlarmData silentAlarmData);

    @Delete
    Completable delete(SilentAlarmData silentAlarmData);

    @Query("SELECT * FROM silent_alarms")
    Flowable<SilentAlarmData> getAll();

    @Query("SELECT * FROM silent_alarms WHERE id = :uuid")
    Maybe<SilentAlarmData> get(String uuid);
}
