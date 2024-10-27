package com.example.fundamentalsatu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fundamentalsatu.data.dao.FavoriteEventDao
import com.example.fundamentalsatu.data.datastore.SettingPreferences
import com.example.fundamentalsatu.data.model.FavoriteEvent
import com.example.fundamentalsatu.data.response.EventResponse
import com.example.fundamentalsatu.data.response.ListEventsItem
import com.example.fundamentalsatu.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val favoriteEventDao: FavoriteEventDao, private val pref: SettingPreferences) : ViewModel() {

    //DATA UPCOMING
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    //DATA FINISHED
    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    // LOADING
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //DATA FAVORITE
    private val _favoriteEvents = MutableLiveData<List<FavoriteEvent>>()
    val favoriteEvents: LiveData<List<FavoriteEvent>> = favoriteEventDao.getFavoriteEvents()


    fun loadDatas(active: Int) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListEvent(active)

        client.enqueue(object: Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
               if (response.isSuccessful) {
                   when (active) {
                       1 -> _upcomingEvents.value = response.body()?.listEvents
                       0 -> _finishedEvents.value = response.body()?.listEvents
                   }
               } else {
                   Log.e("error", "your data API is empty")
               }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("error", t.message!!)
            }

        })
    }

    //TAMBAH EVENT KE FAVORIT
    fun addFavorite(event: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventDao.addFavorite(event)
            updateFavoriteStatus(event)
        }
    }

    //HAPUS EVENT DRI FAVORIT
    fun removeFavorite(event: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventDao.removeFavorite(event)
            updateFavoriteStatus(null)
        }
    }

    // CHECK FAVORITE STATUS UNTUK SINGLE EVENT
    fun checkFavoriteStatus(id: String) {
        viewModelScope.launch {
            val event = favoriteEventDao.getEventById(id)
            event?.let {
                _favoriteEvents.postValue(listOf(it))
            } ?: run {
                _favoriteEvents.postValue(emptyList())
            }
        }
    }

    // TOGGLE FAVORITE STATUS
    fun toggleFavorite(event: FavoriteEvent) {
        viewModelScope.launch {
            val currentFavorite = favoriteEventDao.getEventById(event.id)
            if (currentFavorite != null) {
                removeFavorite(currentFavorite)
            } else {
                addFavorite(event)
            }
        }
    }

    //UNTUK TRIGGER UI
    private fun updateFavoriteStatus(event: FavoriteEvent?) {
        val favoriteList = event?.let {
            listOf(it)
        } ?: emptyList()
        _favoriteEvents.postValue(favoriteList)  // Memberi tahu UI tentang perubahan status favorit
    }

    //DARK LIGHT MODE
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}