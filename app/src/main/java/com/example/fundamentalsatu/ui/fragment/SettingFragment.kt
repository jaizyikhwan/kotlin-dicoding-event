package com.example.fundamentalsatu.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.fundamentalsatu.MainViewModel
import com.example.fundamentalsatu.MainViewModelFactory
import com.example.fundamentalsatu.R
import com.example.fundamentalsatu.data.database.AppDatabase
import com.example.fundamentalsatu.data.datastore.SettingPreferences
import com.example.fundamentalsatu.data.datastore.dataStore
import com.example.fundamentalsatu.databinding.FragmentSettingBinding
import com.example.fundamentalsatu.databinding.FragmentUpcomingBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AppDatabase.getDatabase(requireContext()).favoriteEventDao(),
            SettingPreferences.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //OBSERVE DATA STORE (THEME SETTING)
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

}