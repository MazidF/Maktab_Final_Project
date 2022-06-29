package com.example.onlineshop.woker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.*
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.ui.fragments.setting.NewsWorkerHandler
import com.example.onlineshop.ui.model.ProductList
import com.example.onlineshop.utils.observeOnce
import com.example.onlineshop.utils.toISO8601
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*
import java.util.concurrent.TimeUnit

@HiltWorker
class NewsCheckerWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ShopRepository,
) : CoroutineWorker(context, params) {
    private val notificationId = 7887
    private val notificationChannel = "SetupWorkerNotificationChannel"
    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val notificationBuilder by lazy {
        NotificationCompat.Builder(context, notificationChannel)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_shopping_cart)
            .setAutoCancel(true)
    }

    init {
        setupNotificationChannel()
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannel,
                notificationChannel,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(pending: PendingIntent, title: String) {
        val notification = notificationBuilder
            .setContentIntent(pending)
            .setContentTitle(title)
            .build()
        notificationManager.notify(
            notificationId, notification
        )
    }

    override suspend fun doWork(): Result {
        val duration = inputData.getInt(NewsWorkerHandler.PERIODIC_DURATION_HOUR, -1)
        val delay = inputData.getInt(NewsWorkerHandler.INITIAL_DELAY, 0)

        val date = if (duration == -1) {
            return Result.failure(workDataOf("error" to Exception("Empty input_data!!")))
        } else {
            Calendar.getInstance().apply {
                add(Calendar.HOUR_OF_DAY, -duration)
                add(Calendar.MILLISECOND, -delay)
            }
        }.time

        val result = repository.getProductByDate(date.toISO8601())
        return if (result.isSuccessful) {
            setupNotification(result.asSuccess()!!.body())
            Result.success()
        } else {
            // TODO: maybe replace with failure
            Result.retry()
        }
    }

    private fun setupNotification(list: List<Product>) {
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.navigation_graph_main)
            .setDestination(R.id.fragmentHome)
            .setArguments(
                bundleOf(
                    ARGUMENT_PRODUCT_LIST_KEY to ProductList.Custom(list)
                )
            )
            .createPendingIntent()
        val size = list.size
        val title = "توجه: $size کالای جدید به کالا ها اضافه شد.\nبرای مشاهده کلید کنید."
        showNotification(pendingIntent, title)
    }

    companion object {
        const val ARGUMENT_PRODUCT_LIST_KEY = "products"
    }
}
