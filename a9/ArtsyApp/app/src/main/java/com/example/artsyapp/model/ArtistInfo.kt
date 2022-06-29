package com.example.artsyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistInfo(
    val name: String = "Pablo Picasso",
    val birthday: String = "1881",
    val deathday: String = "2000",
    val nationality: String = "Spanish",
    val biography: String="loren ipsum")
