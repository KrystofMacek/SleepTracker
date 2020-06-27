package com.krystofmacek.sleeptracker.sleeptracker

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.krystofmacek.sleeptracker.R
import com.krystofmacek.sleeptracker.adapters.SleepNightAdapter
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

        //Recycler Adapter
        val adapter = SleepNightAdapter()
        binding.sleepList.adapter = adapter
        // Nights observer
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer{
            // if it !=null then adapter data = it
            it?.let {
                // when a changed list is available. ListAdapter provides a method called submitList() to tell ListAdapter that a new version of the list is available.
                // When this method is called, the ListAdapter diffs the new list against the old one
                // and detects items that were added, removed, moved, or changed. Then the ListAdapter updates the items shown by RecyclerView.
                adapter.submitList(it)
            }
        })

        //Observer navigateToSleepQuality
        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(night.nightId)
                )
                sleepTrackerViewModel.doneNavigating()
            }
        })

        sleepTrackerViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        return binding.root
    }
}
