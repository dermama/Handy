package com.edge.integrated.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.edge.integrated.bridge.ModelBridge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ModelDownloadService : Service() {

    @Inject
    lateinit var modelBridge: ModelBridge

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val modelId = intent?.getStringExtra(EXTRA_MODEL_ID) ?: return START_NOT_STICKY
        val notification = createNotification("Downloading model...")
        startForeground(NOTIFICATION_ID, notification)

        scope.launch {
            modelBridge.downloadModel(modelId)
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Model Downloads",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows AI model download progress"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading AI Model")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setProgress(0, 0, true)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "model_download"
        private const val NOTIFICATION_ID = 1004
        const val EXTRA_MODEL_ID = "model_id"

        fun start(context: android.content.Context, modelId: String) {
            val intent = android.content.Intent(context, ModelDownloadService::class.java).apply {
                putExtra(EXTRA_MODEL_ID, modelId)
            }
            context.startForegroundService(intent)
        }
    }
}
