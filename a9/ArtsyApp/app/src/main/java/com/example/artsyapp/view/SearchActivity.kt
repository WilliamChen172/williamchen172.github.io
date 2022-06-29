package com.example.artsyapp.view

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artsyapp.ArtsyRequest
import com.example.artsyapp.databinding.ActivitySearchBinding
import com.example.artsyapp.viewmodel.SearchViewModel
import com.example.artsyapp.viewmodel.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SearchActivity", "This is run")
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.searchResultBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                startViewModel(query)
                supportActionBar?.title = query.uppercase()
            }
        }

        adapter = SearchAdapter(this, SearchAdapter.OnClickListener { artist ->
            val artistIntent = Intent(this, ArtistInfoActivity::class.java).apply {
                putExtra(EXTRA_ARTIST_ID, artist.id)
                putExtra(EXTRA_ARTIST_NAME, artist.name)
            }
            startActivity(artistIntent)
        })
        binding.resultView.adapter = adapter
        binding.resultView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    private fun startViewModel(query: String) {
        viewModelFactory = SearchViewModelFactory(query, ArtsyRequest.getInstance(this))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SearchViewModel::class.java)
        viewModel.searchResult.observe(this, Observer { result ->
            if (result.isNotEmpty()) {
                binding.noresultView.visibility = View.INVISIBLE
            }
            adapter.setResults(result)
        })
        viewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.searchLoadingView.visibility = View.VISIBLE
                binding.resultView.visibility = View.INVISIBLE
            } else {
                binding.searchLoadingView.visibility = View.INVISIBLE
                binding.resultView.visibility = View.VISIBLE
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}