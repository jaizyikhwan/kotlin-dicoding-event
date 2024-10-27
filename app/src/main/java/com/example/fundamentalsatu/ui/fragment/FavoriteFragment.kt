package com.example.fundamentalsatu.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentalsatu.MainViewModel
import com.example.fundamentalsatu.MainViewModelFactory
import com.example.fundamentalsatu.data.database.AppDatabase
import com.example.fundamentalsatu.data.datastore.SettingPreferences
import com.example.fundamentalsatu.data.datastore.dataStore
import com.example.fundamentalsatu.data.response.ListEventsItem
import com.example.fundamentalsatu.databinding.FragmentFavoriteBinding
import com.example.fundamentalsatu.ui.adapter.ListEventAdapter
import java.util.ArrayList

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AppDatabase.getDatabase(requireContext()).favoriteEventDao(),
            SettingPreferences.getInstance(requireContext().dataStore)
            )
    }
    private lateinit var listEventAdapter: ListEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListEventFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvListEventFavorite.addItemDecoration(itemDecoration)

        //INISIALISASI ADAPTER
        listEventAdapter = ListEventAdapter(ArrayList())
        binding.rvListEventFavorite.adapter = listEventAdapter

        //OBSERVE LIVEDATA
        viewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            if (favoriteEvents.isNotEmpty()) {
                val listEventsItems = favoriteEvents.map { favoriteEvent ->
                    ListEventsItem(
                        id = favoriteEvent.id.toIntOrNull(),
                        name = favoriteEvent.name,
                        mediaCover = favoriteEvent.mediaCover,
                        ownerName = favoriteEvent.ownerName,
                        beginTime = favoriteEvent.beginTime,
                        quota = favoriteEvent.quota,
                        registrants = favoriteEvent.registrants,
                        description = favoriteEvent.description,
                        summary = favoriteEvent.summary,
                        link = favoriteEvent.link
                    )
                }
                listEventAdapter.updateData(ArrayList(listEventsItems))
                Log.d("FavoriteFragment", "Data berhasil didapatkan: $listEventsItems")
            } else {
                listEventAdapter.updateData(ArrayList())
                Log.d("FavoriteFragment", "Data favorite kosong")
            }
        }

    }

}