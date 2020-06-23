package com.krystofmacek.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.krystofmacek.sleeptracker.database.SleepDatabaseDao
import com.krystofmacek.sleeptracker.database.SleepNight
import com.krystofmacek.sleeptracker.util.formatNights
import kotlinx.coroutines.*

class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    //Live Data to navigate to the SleepQualityFragment
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality
    // Func to reset the variable that triggers navigation.
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    // Coroutine job
    private var viewModelJob: Job = Job()
    // Coroutine Scope - what thread will the coroutine run on, created with Dispatcher and Job
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    // Variable holding current night
    //  every time the data in the database changes, the LiveData nights is updated to show the latest data.
    //You never need to explicitly set the LiveData or update it. Room updates the data to match the database.
    private var tonight = MutableLiveData<SleepNight?>()

    private val nights = database.getAllNights()

    val nightString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        // launching coroutine inside the UI scope
        uiScope.launch {
            // get the value of tonight from database
            tonight.value = getTonightFromDatabase()
        }
    }

    // getting tonight from database
    private suspend fun getTonightFromDatabase(): SleepNight? {
        // Return the result from a coroutine that runs in the Dispatchers.IO context.
        // Using the I/O dispatcher, because getting data from the database is an I/O operation and has nothing to do with the UI.
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if(night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        // Launch new Coroutine
        uiScope.launch {
            //create new night
            val newNight = SleepNight()
            insert(newNight)
            //Get tonight from database
            tonight.value = getTonightFromDatabase()
        }
    }
    //Insert new night into database
    private suspend fun insert(night: SleepNight) {
        //inserting in coroutine
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    //Stop button Coroutines func
    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }
    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    // Clear Button coroutines func
    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
        }
    }
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        // when view model is destroyed cancel all Coroutines
        viewModelJob.cancel()
    }
}

/** The coroutine pattern...
 *
 * Launch a coroutine that runs on the main or UI thread, because the result affects the UI.
 * Call a suspend function to do the long-running work, so that you don't block the UI thread while waiting for the result.
 * The long-running work has nothing to do with the UI. Switch to the I/O context, so that the work can run in a thread pool that's optimized and set aside for these kinds of operations.
 * Then call the database function to do the work.
 *
 ***********************************************************
    fun someWorkNeedsToBeDone {
        uiScope.launch {
            suspendFunction()
        }
    }

    suspend fun suspendFunction() {
        withContext(Dispatchers.IO) {
            longrunningWork()
        }
    }
 **********************************************************
 * */