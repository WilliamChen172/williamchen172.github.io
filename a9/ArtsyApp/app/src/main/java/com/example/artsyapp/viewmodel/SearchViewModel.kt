package com.example.artsyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.artsyapp.ArtsyRequest
import com.example.artsyapp.model.Artist
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SearchViewModel(name: String, artsy: ArtsyRequest) : ViewModel() {
    private var artist_name = name
    private var artsyRequest = artsy

    private val _searchResult = MutableLiveData<List<Artist>>()
    val searchResult: LiveData<List<Artist>>
        get() = _searchResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _failed = MutableLiveData<String>()
    val failed: LiveData<String>
        get() = _failed

    init {
        getSearchResults()
    }

    fun getSearchResults() {
        _loading.value = true
        val searchUrl = "https://hw9-android-app-backend.wl.r.appspot.com/search/"
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, searchUrl+artist_name, null,
            { response ->
                _searchResult.value = Json.decodeFromString<List<Artist>>(response.toString())
                Log.d("SearchViewModel", response.toString())
                _loading.value = false
            }, { error ->
                _failed.value = error.localizedMessage
                Log.d("SearchViewModel", error.localizedMessage)
                _loading.value = false
            }
        )
        artsyRequest.addToRequestQueue(jsonObjectRequest)
    }
}

class SearchViewModelFactory(private val name: String, private val artsy: ArtsyRequest) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(name, artsy) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}