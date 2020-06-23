package com.krystofmacek.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepDatabaseDao {
    //Insert new SleepNight
    @Insert
    fun insert(night: SleepNight)

    //Update SleepNight with same ID as the one passed in
    @Update
    fun update(night: SleepNight)

    //Get one specific record of SleepNight
    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId = :key")
    fun get(key: Long): SleepNight?

    //Clear the table
    @Query("DELETE FROM daily_sleep_quality_table")
    fun clear()

    //Get tonight's data
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight?

    //Get all nights as LiveData
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>
}