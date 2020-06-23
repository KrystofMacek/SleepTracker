package com.krystofmacek.sleeptracker

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.krystofmacek.sleeptracker.database.SleepDatabase
import com.krystofmacek.sleeptracker.database.SleepDatabaseDao
import com.krystofmacek.sleeptracker.database.SleepNight
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

//@RunWith annotation identifies the test runner, which is the program that sets up and executes the tests.
@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {
    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    //During setup, the function annotated with @Before is executed, and it creates an in-memory SleepDatabase with the SleepDatabaseDao.
    // "In-memory" means that this database is not saved on the file system and will be deleted after the tests run.
    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
            // Allowing main thread queries, just for testing.
            // By default, you get an error if you try to run queries on the main thread.
            // This method allows you to run tests on the main thread, which you should only do during testing.
            .allowMainThreadQueries()
            .build()
        sleepDao = db.sleepDatabaseDao
    }

    //When testing is done, the function annotated with @After executes to close the database.
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    //In a test method annotated with @Test, you create, insert, and retrieve a SleepNight, and assert that they are the same.
    //If anything goes wrong, throw an exception. In a real test, you would have multiple @Test methods.
    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        Assert.assertEquals(tonight?.sleepQuality, -1)
    }
}