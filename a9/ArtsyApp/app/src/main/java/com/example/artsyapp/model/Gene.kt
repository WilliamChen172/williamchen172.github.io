package com.example.artsyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Gene(
    val name: String,
    val thumbnail: String,
    val description: String)
