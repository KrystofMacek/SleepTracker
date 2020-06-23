package com.krystofmacek.sleeptracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepNight::class] , version = 1, exportSchema = false)
abstract class SleepDatabase: RoomDatabase() {

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {
        //The value of a volatile variable will never be cached, and all writes and reads will be done to and from the main memory.
        @Volatile
        //The INSTANCE variable will keep a reference to the database, once one has been created
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context): SleepDatabase {
            //synchronized means that only one thread of execution at a time can enter this block of code, which makes sure the database only gets initialized once.
            synchronized(this) {
                var instance = INSTANCE

                // If there is no database yet
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_databse"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}