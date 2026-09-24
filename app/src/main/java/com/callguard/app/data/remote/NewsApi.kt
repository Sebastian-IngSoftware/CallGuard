package com.callguard.app.data.remote

import com.callguard.app.data.remote.dto.NewsDto
import retrofit2.http.GET

interface NewsApi {
    // TODO: reemplazar "news" por el path real de tu API cuando la tengas lista.
    @GET("news")
    suspend fun getNews(): List<NewsDto>
}
