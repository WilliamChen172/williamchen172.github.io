package com.example.artsyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.artsyapp.ArtsyRequest
import com.example.artsyapp.model.Gene
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ArtistInfoViewModel(id: String, artsy: ArtsyRequest): ViewModel() {
    private var artist_id = id
    private var artsyRequest = artsy

    private val _artistInfo = MutableLiveData<String>()
    val artistInfo: LiveData<String>
        get() = _artistInfo

    private val _artworks = MutableLiveData<String>()
    val artworks: LiveData<String>
        get() = _artworks

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _failed = MutableLiveData<String>()
    val failed: LiveData<String>
        get() = _failed

    init {
        getArtistInfo()
    }

//    fun setArtsyInstance(artsyRequest: ArtsyRequest) {
//        this.artsyRequest = artsyRequest
//    }

    fun getArtistInfo() {
        _loading.value = true
        val artistInfoUrl = "https://hw9-android-app-backend.wl.r.appspot.com/artist/"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, artistInfoUrl+artist_id, null,
            { response ->
                _artistInfo.value = response.toString()
//                _loading.value = false
//                Log.d("FavoriteViewModel", _artistInfo.value!!.name)
            }, { error ->
                _failed.value = error.localizedMessage
                _loading.value = false
            }
        )
        artsyRequest.addToRequestQueue(jsonObjectRequest)
    }

    fun getArtworks() {
        _loading.value = true
        Log.d("ArtistInfoViewModel", "start requesting artwork")
        val artworkUrl = "https://hw9-android-app-backend.wl.r.appspot.com/artwork/"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, artworkUrl+artist_id, null,
            { response ->
                _artworks.value = response.toString()
                Log.d("ArtistInfoViewModel", _artistInfo.value!!)
                _loading.value = false
            }, { error ->
                _failed.value = error.localizedMessage
                Log.d("ArtistInfoViewModel", _failed.value.toString())
                _loading.value = false
            }
        )
        artsyRequest.addToRequestQueue(jsonArrayRequest)
    }

    fun getCategory(artwork_id: String): MutableLiveData<List<Gene>> {
        Log.d("ArtistInfoViewModel", "start requesting category")
        val geneUrl = "https://hw9-android-app-backend.wl.r.appspot.com/genes/"
        val gene = MutableLiveData<List<Gene>>()
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, geneUrl+artwork_id, null,
            { response ->
                gene.value = Json.decodeFromString(response.toString())
                Log.d("ArtistInfoViewModel", gene.value!!.toString())
            }, { error ->
                _failed.value = error.localizedMessage
                Log.d("ArtistInfoViewModel", _failed.value.toString())
            }
        )
        artsyRequest.addToRequestQueue(jsonArrayRequest)
        return gene
    }
}

class ArtistInfoViewModelFactory(private val artist_id: String, private val artsy: ArtsyRequest) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArtistInfoViewModel(artist_id, artsy) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}