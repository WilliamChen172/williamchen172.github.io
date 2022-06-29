package com.example.artsyapp.view
//import androidx.appcompat.widget.SearchView
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artsyapp.R
import com.example.artsyapp.databinding.ActivityMainBinding
import com.example.artsyapp.model.Favorite
import com.example.artsyapp.viewmodel.FavoriteViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

const val EXTRA_ARTIST_ID = "com.example.artsyapp.artist_id"
const val EXTRA_ARTIST_NAME = "com.example.artsyapp.artist_name"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.searchBar)
        setText()
        val loadTimer = object: CountDownTimer(2000, 1000) {
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
                binding.mainLoadingView.visibility = View.INVISIBLE
            }
        }

        val splashTimer = object: CountDownTimer(1000, 1000) {
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
                binding.splashView.visibility = View.INVISIBLE
                loadTimer.start()
            }
        }
        splashTimer.start()

        adapter = FavoriteAdapter(FavoriteAdapter.OnClickListener { favorite ->
            val favoriteIntent = Intent(applicationContext, ArtistInfoActivity::class.java).apply {
                putExtra(EXTRA_ARTIST_ID, favorite.id)
                putExtra(EXTRA_ARTIST_NAME, favorite.name)
            }
            startActivity(favoriteIntent)
        })
        updateFavorites()
        binding.favoriteView.adapter = adapter
        binding.favoriteView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.poweredbyText.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.artsy.net/")
            startActivity(openURL)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search_view).actionView as SearchView).apply {
            // Change hint color to white
            maxWidth = 100000
            val searchSrcTextId = resources.getIdentifier("android:id/search_src_text", null, null) as Int
            val searchEditText = this.findViewById(searchSrcTextId) as EditText
            searchEditText.setTextColor(Color.WHITE)
            searchEditText.setHintTextColor(Color.GRAY)
//            // Hide hint icon
            val searchHintIconId = resources.getIdentifier("android:id/search_mag_icon", null, null) as Int
            val searchHintIcon = this.findViewById(searchHintIconId) as ImageView
            searchHintIcon.layoutParams = LinearLayout.LayoutParams(0, 0)
//            // Hide searchbox underline
            val searchPlateId = resources.getIdentifier("android:id/search_plate", null, null) as Int
            val searchPlate = this.findViewById(searchPlateId) as View
            searchPlate.setBackgroundColor(Color.TRANSPARENT)
//            // make wider
            val id: Int = this.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null)
            val textView = this.findViewById(id) as TextView

//            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }

        return true
    }

    override fun onRestart() {
        super.onRestart()
        updateFavorites()
    }

    private fun setText() {
        val DateTextView = findViewById<TextView>(R.id.date_text)
        val simpleDate = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val currentDate = simpleDate.format(Date())
        DateTextView.setText(currentDate)
    }

    private fun updateFavorites() {
        val pref = applicationContext.getSharedPreferences("favorites", MODE_PRIVATE)
        val allMap = pref.all
        val artists = mutableListOf<Favorite>()
        for ((key, value) in allMap.entries) {
            val artist: Favorite = Json.decodeFromString(value as String)
            artists.add(artist)
            Log.d(TAG, value)
        }
        adapter.setFavorite(artists.reversed())
    }
}

