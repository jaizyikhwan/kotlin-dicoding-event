package com.example.fundamentalsatu.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.fundamentalsatu.MainViewModel
import com.example.fundamentalsatu.MainViewModelFactory
import com.example.fundamentalsatu.R
import com.example.fundamentalsatu.data.database.AppDatabase
import com.example.fundamentalsatu.data.datastore.SettingPreferences
import com.example.fundamentalsatu.data.datastore.dataStore
import com.example.fundamentalsatu.data.model.FavoriteEvent
import com.example.fundamentalsatu.data.response.ListEventsItem
import com.example.fundamentalsatu.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AppDatabase.getDatabase(this).favoriteEventDao(),
            SettingPreferences.getInstance(applicationContext.dataStore)
        )
    }
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TERIMA DATA
        val dataEvent = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ListEventsItem>("key_event", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ListEventsItem>("key_event")
        }

        if (dataEvent != null) {
            setupUI(dataEvent)
            observeFavoriteStatus(dataEvent.id.toString())
        } else {
            Toast.makeText(this, "Data event tidak tersedia", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(dataEvent?.link)
            startActivity(intent)
        }

        //TOMBOL FAVORIT
        binding.fabFavorite.setOnClickListener {
            dataEvent?.let { event ->
                val favoriteEvent = FavoriteEvent(
                    id = event.id.toString(),
                    name = event.name.toString(),
                    mediaCover = event.imageLogo ?: event.mediaCover,
                    ownerName = event.ownerName,
                    beginTime = event.beginTime,
                    quota = event.quota,
                    registrants =  event.registrants,
                    description = event.description,
                    summary = event.summary,
                    link = event.link
                )
                mainViewModel.toggleFavorite(favoriteEvent)
                if (isFavorite) {
                    mainViewModel.removeFavorite(favoriteEvent)
                } else {
                    mainViewModel.addFavorite(favoriteEvent)
                }
            }
        }
        Log.d("DetailActivity", "dataEvent: $dataEvent")
    }

    //MENAMPILKAN DATA DI UI
    private fun setupUI(event: ListEventsItem) {
        binding.name.text = event.name
        binding.ownerName.text = event.ownerName
        binding.beginTime.text = event.beginTime
        binding.quota.text = "Sisa Kuota ${event.quota?.minus(event.registrants ?: 0) ?: 0}"
        binding.description.text = Html.fromHtml(event.description ?: "", Html.FROM_HTML_MODE_LEGACY)
        Glide.with(this)
            .load(event.imageLogo ?: event.mediaCover)
            .into(binding.imageLogo)
        Log.d("DetailActivity", "dataEvent: $event")
    }

    //OBSERVE STATUS FAVORIT
    private fun observeFavoriteStatus(eventId: String) {
        mainViewModel.checkFavoriteStatus(eventId)

        mainViewModel.favoriteEvents.observe(this) { favoriteEvents ->
            val isFavorited = favoriteEvents.any { it.id == eventId }
            if (isFavorited) {
                binding.fabFavorite.setImageResource(R.drawable.outline_favorite_24)
                isFavorite = true
            } else {
                binding.fabFavorite.setImageResource(R.drawable.outline_favorite_border_24)
                isFavorite = false
            }
        }
    }

}