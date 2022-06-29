package com.example.artsyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: String = "404",
    val name: String = "Pablo Picasso",
    val thumbnail: String = "")
