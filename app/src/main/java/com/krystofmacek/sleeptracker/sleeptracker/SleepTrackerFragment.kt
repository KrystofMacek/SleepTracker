package com.krystofmacek.sleeptracker.sleeptracker

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.krystofmacek.sleeptracker.R
import com.krystofmacek.sleeptracker.database.SleepDatabase
import com.krystofmacek.sleeptracker.database.SleepDatabaseDao
import com.krystofmacek.sleeptracker.databinding.FragmentSleepTrackerBinding

class SleepTrackerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_tracker,
            container,
            false
        )

        // get the app reference
        val application: Application = requireNotNull(this.activity).application
        // Initialize DAO for Sleep Database
        val dataSource: SleepDatabaseDao = SleepDatabase.getInstance(application).sleepDatabaseDao
        // Initialize View Model Factory for Sleep Tracker
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        // Initialize sleep tracker View Model using the Factory
        val sleepTrackerViewModel = ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        // set binding view model
        binding.sleepTrackerViewModel = sleepTrackerViewModel
        // set binding life cycle owner
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}
