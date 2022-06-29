package com.example.artsyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Artwork(
    val id: String,
    val title: String,
    val date: String,
    val thumbnail: String)
