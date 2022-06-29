package com.example.artsyapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.artsyapp.ArtsyRequest
import com.example.artsyapp.R
import com.example.artsyapp.databinding.ActivityArtistInfoBinding
import com.example.artsyapp.model.ArtistInfo
import com.example.artsyapp.model.Favorite
import com.example.artsyapp.viewmodel.ArtistInfoViewModel
import com.example.artsyapp.viewmodel.ArtistInfoViewModelFactory
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ArtistInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtistInfoBinding
    private lateinit var viewModel: ArtistInfoViewModel
    private lateinit var viewModelFactory: ArtistInfoViewModelFactory
    private lateinit var favorite: Favorite

    private var artistInfo = Bundle()
    private var artworks = Bundle()
    private var isFavorite = MutableLiveData<Boolean>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.infoBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val artist_id = intent.getStringExtra(EXTRA_ARTIST_ID) as String
        val artist_name = intent.getStringExtra(EXTRA_ARTIST_NAME) as String
        binding.artistTitleText.text = artist_name

        // Load viewmodel functionality
        viewModelFactory = ArtistInfoViewModelFactory(artist_id, ArtsyRequest.getInstance(this))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ArtistInfoViewModel::class.java)

        viewModel.artistInfo.observe(this, Observer { info ->
            artistInfo.putSerializable("info", info)
            // Create favorite object
            val favoriteInfo: ArtistInfo = Json.decodeFromString(info)
            favorite = Favorite(artist_id, artist_name, favoriteInfo.nationality, favoriteInfo.birthday)
            // Get artworks
            viewModel.getArtworks()
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    Log.d("ArtistInfoActivity", "fragment called")
                    setReorderingAllowed(true)
                    add<ArtistDetailFragment>(R.id.fragmentContainerView, args = artistInfo)
                }
            }
        })

        viewModel.artworks.observe(this, Observer { works ->
            artworks.putSerializable("artworks", works)
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.artworkBtn.isEnabled = false
                binding.detailsBtn.isEnabled = false
                binding.favoriteBtn.isEnabled = false
                binding.infoLoading.visibility = View.VISIBLE
                binding.infoLoadingText.visibility = View.VISIBLE
                binding.fragmentContainerView.visibility = View.INVISIBLE
            } else {
                binding.artworkBtn.isEnabled = true
                binding.detailsBtn.isEnabled = true
                binding.favoriteBtn.isEnabled = true
                binding.infoLoading.visibility = View.INVISIBLE
                binding.infoLoadingText.visibility = View.INVISIBLE
                binding.fragmentContainerView.visibility = View.VISIBLE
            }
        })

        // Set tab button functionality
        binding.detailsBtn.setOnClickListener { view ->
            binding.artworkBorder.setBackgroundColor(getColor(R.color.white))
            binding.detailBorder.setBackgroundColor(getColor(R.color.blue))
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<ArtistDetailFragment>(R.id.fragmentContainerView, args = artistInfo)
                }
            }
        }

        binding.artworkBtn.setOnClickListener {
            binding.artworkBorder.setBackgroundColor(getColor(R.color.blue))
            binding.detailBorder.setBackgroundColor(getColor(R.color.white))
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    if (artworks.isEmpty) {
                        Log.d("ArtistInfoActivity", "No artworks")
                        viewModel.getArtworks()
                    } else {
                        Log.d("ArtistInfoActivity", "going to artwork fragment")
                        replace<ArtworkFragment>(R.id.fragmentContainerView, args = artworks)
                    }
                }
            }
        }
        // Set favorite button functionality
        isFavorite.observe(this, Observer { favorite ->
            if (favorite) {
                binding.favoriteBtn.setImageResource(R.drawable.star)
            } else {
                binding.favoriteBtn.setImageResource(R.drawable.star_outline)
            }
        })

        val pref = applicationContext.getSharedPreferences("favorites", MODE_PRIVATE)
        val editor = pref.edit()
        val favStr = pref.getString(artist_id, null)
        isFavorite.value = favStr != null

        binding.favoriteBtn.setOnClickListener {
            isFavorite.value = !isFavorite.value!!
            if (isFavorite.value == true) {
                editor.putString(artist_id, Json.encodeToString(favorite))
                editor.apply()
                Toast.makeText(applicationContext, artist_name + getString(R.string.add_favorites), Toast.LENGTH_SHORT).show()
            } else {
                editor.remove(artist_id)
                editor.apply()
                Toast.makeText(applicationContext, artist_name + getString(R.string.remove_favorites), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}