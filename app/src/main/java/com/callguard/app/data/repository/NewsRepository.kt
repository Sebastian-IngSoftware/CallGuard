package com.callguard.app.data.repository

import com.callguard.app.domain.model.NewsItem

interface NewsRepository {
    suspend fun fetchNews(): Result<List<NewsItem>>
}
