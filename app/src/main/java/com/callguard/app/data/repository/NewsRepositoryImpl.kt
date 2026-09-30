package com.callguard.app.data.repository

import com.callguard.app.data.remote.NewsApi
import com.callguard.app.data.remote.dto.NewsDto
import com.callguard.app.domain.model.NewsItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi
) : NewsRepository {

    override suspend fun fetchNews(): Result<List<NewsItem>> = try {
        Result.success(api.getNews().map { it.toDomain() })
    } catch (e: Exception) {
        Result.failure(e)
    }
}

private fun NewsDto.toDomain() = NewsItem(
    id = id,
    title = title,
    body = body,
    publishedAt = publishedAt
)
