package com.callguard.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val id: String,
    val title: String,
    val body: String,
    val publishedAt: String
)
