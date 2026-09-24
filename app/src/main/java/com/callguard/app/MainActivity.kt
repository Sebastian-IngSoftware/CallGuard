package com.callguard.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.callguard.app.ui.navigation.CallGuardNavHost
import com.callguard.app.ui.theme.CallGuardTheme
import com.callguard.app.worker.NewsCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduleNewsCheck()

        setContent {
            CallGuardTheme {
                CallGuardNavHost()
            }
        }
    }

    private fun scheduleNewsCheck() {
        val request = PeriodicWorkRequestBuilder<NewsCheckWorker>(6, TimeUnit.HOURS).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "news_check",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
