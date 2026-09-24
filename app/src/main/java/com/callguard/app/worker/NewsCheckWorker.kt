package com.callguard.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.callguard.app.data.repository.NewsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Chequea periódicamente si hay novedades/actualizaciones desde la API.
 * Se programa desde MainActivity con WorkManager (cada 6 horas).
 */
@HiltWorker
class NewsCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val newsRepository: NewsRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val result = newsRepository.fetchNews()
        return if (result.isSuccess) Result.success() else Result.retry()
    }
}
