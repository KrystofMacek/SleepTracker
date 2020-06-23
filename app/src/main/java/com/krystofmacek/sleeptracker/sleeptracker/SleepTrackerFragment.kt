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

        val application: Application = requireNotNull(this.activity).application
        val dataSource: SleepDatabaseDao = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        val sleepTrackerViewModel = ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        binding.sleepTrackerViewModel = sleepTrackerViewModel


        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}
