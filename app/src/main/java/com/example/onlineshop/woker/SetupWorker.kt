package com.example.onlineshop.woker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.utils.getDeviceId
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import javax.inject.Inject

@HiltWorker
class SetupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: ShopRepository,
) : CoroutineWorker(appContext, params) {
    private val notificationId = 8778
    private val notificationChannel = "SetupWorkerNotificationChannel"
    private val notificationManager by lazy {
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val notificationBuilder by lazy {
        NotificationCompat.Builder(appContext, notificationChannel)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(3, 0, false)
            .setSmallIcon(R.drawable.ic_setting)
            .setContentTitle("Initial Setup")
            .setContentText("Please wait")
            .setAutoCancel(false)
            .setOngoing(true)
    }

    init {
        setupNotificationChannel()
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannel,
                notificationChannel,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            notificationId,
            notificationBuilder.build(),
        )
    }

    override suspend fun doWork(): Result {
        val deviceId = getDeviceId(applicationContext) + "@gmail.com"
        val result = try {
            signIn(deviceId)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
        endNotification()
        return result
    }

    private suspend fun signIn(deviceId: String) {
        val flow = repository.signIn(
            email = deviceId,
            password = deviceId,
        )
        setProgress(1)
        flow.collect {
            if (it) {
                setProgress(3)
            } else {
                setProgress(2)
                login(deviceId)
            }
        }
    }

    private suspend fun login(deviceId: String) {
        val flow = repository.logIn(deviceId, deviceId)
        flow.collect {
            if (it) {
                setProgress(3)
            } else {
                throw Exception("failed!!")
            }
        }
    }

    private fun setProgress(progress: Int) {
        notificationBuilder
            .setProgress(3, progress, false)
        notificationManager.notify(
            notificationId,
            notificationBuilder.build()
        )
    }

    private fun endNotification() {
        notificationManager.cancel(notificationId)
    }
}