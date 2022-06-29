package com.example.onlineshop.ui.fragments.setting

import android.content.Context
import androidx.work.*
import com.example.onlineshop.woker.NewsCheckerWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsWorkerHandler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val tag = "NewsCheckerWorker_Tag"

    private val workManager by lazy {
        WorkManager.getInstance(context)
    }

    private val constraints by lazy {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    fun test(
        initialDelay: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Operation {
        val request = OneTimeWorkRequestBuilder<NewsCheckerWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInitialDelay(initialDelay, timeUnit)
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    INITIAL_DELAY to initialDelay.toInt(),
                    PERIODIC_DURATION_HOUR to 0,
                )
            )
            .addTag(tag)
            .build()
        return workManager.enqueueUniqueWork(
            tag,
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    fun setup(
        periodicHour: Long,
        initialDelay: Long = periodicHour * 3600 * 1000,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    ) {
        val request = PeriodicWorkRequestBuilder<NewsCheckerWorker>(periodicHour, TimeUnit.HOURS)
            .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST)
            .setInitialDelay(initialDelay, timeUnit)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                PeriodicWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .setInputData(
                workDataOf(
                    INITIAL_DELAY to initialDelay.toInt(),
                    PERIODIC_DURATION_HOUR to periodicHour.toInt(),
                )
            )
            .addTag(tag)
            .build()
        uniqueEnqueue(request)
    }

    private fun uniqueEnqueue(request: PeriodicWorkRequest) {
        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun cancel() {
        workManager.cancelUniqueWork(tag)
    }

    companion object {
        const val INITIAL_DELAY = "initialDelayKey"
        const val PERIODIC_DURATION_HOUR = "periodicDurationKey"
    }

}