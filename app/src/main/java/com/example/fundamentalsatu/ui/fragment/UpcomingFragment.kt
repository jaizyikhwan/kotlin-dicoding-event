package com.example.fundamentalsatu.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentalsatu.MainViewModel
import com.example.fundamentalsatu.MainViewModelFactory
import com.example.fundamentalsatu.data.database.AppDatabase
import com.example.fundamentalsatu.data.datastore.SettingPreferences
import com.example.fundamentalsatu.data.datastore.dataStore
import com.example.fundamentalsatu.data.response.EventResponse
import com.example.fundamentalsatu.data.retrofit.ApiConfig
import com.example.fundamentalsatu.databinding.FragmentUpcomingBinding
import com.example.fundamentalsatu.ui.adapter.ListEventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class UpcomingFragment : Fragment() {

    private lateinit var binding: FragmentUpcomingBinding
    private val viewModel: MainViewModel by viewModels {
   MainViewModelFactory(
       AppDatabase.getDatabase(requireContext()).favoriteEventDao(),
       SettingPreferences.getInstance(requireContext().dataStore))
}
    private lateinit var listEventAdapter: ListEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvListEvent.addItemDecoration(itemDecoration)

        //INISIALISASI ADAPTER
        listEventAdapter = ListEventAdapter(ArrayList())
        binding.rvListEvent.adapter = listEventAdapter

        //OBSERVE LIVEDATA
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            listEventAdapter.updateData(ArrayList(events))
        }

        viewModel.loadDatas(1)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    

}