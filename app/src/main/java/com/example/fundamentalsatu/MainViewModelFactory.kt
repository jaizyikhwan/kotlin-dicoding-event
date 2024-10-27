package com.example.fundamentalsatu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundamentalsatu.data.dao.FavoriteEventDao
import com.example.fundamentalsatu.data.datastore.SettingPreferences

class MainViewModelFactory(private val favoriteEventDao: FavoriteEventDao, private val pref: SettingPreferences) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(favoriteEventDao, pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}