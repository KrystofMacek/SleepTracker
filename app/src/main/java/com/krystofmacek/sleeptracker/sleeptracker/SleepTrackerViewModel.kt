package com.krystofmacek.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.krystofmacek.sleeptracker.database.SleepDatabaseDao

class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) {



}