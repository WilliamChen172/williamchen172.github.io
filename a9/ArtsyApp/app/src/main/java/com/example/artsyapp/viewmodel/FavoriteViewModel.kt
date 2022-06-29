package com.example.artsyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.artsyapp.ArtsyRequest
import com.example.artsyapp.model.ArtistInfo
import com.example.artsyapp.model.Favorite


class FavoriteViewModel: ViewModel() {

    private val favorites: LiveData<List<Favorite>> = MutableLiveData()
    private lateinit var artsyRequest: ArtsyRequest

    private val _response = MutableLiveData<ArtistInfo>()
    val response: LiveData<ArtistInfo>
        get() = _response

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _failed = MutableLiveData<String>()
    val failed: LiveData<String>
        get() = _failed

    init {
//        _loading.value = true
//        getApiResponse()
    }

}