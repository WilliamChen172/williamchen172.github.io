package com.example.artsyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val id: String,
    val name: String,
    val nationality: String,
    val birthday: String)
