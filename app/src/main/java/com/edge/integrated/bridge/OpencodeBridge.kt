package com.edge.integrated.bridge

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

data class OpencodeStatus(
    val isRunning: Boolean = false,
    val serverPort: Int = 8080,
    val mode: OpencodeMode = OpencodeMode.CLOUD,
    val connectedProvider: String? = null
)

enum class OpencodeMode { CLOUD, LOCAL, AUTO }

@Singleton
class OpencodeBridge @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val status = MutableStateFlow(OpencodeStatus())
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val statusFlow: Flow<OpencodeStatus> = status

    val configDir: File
        get() = context.filesDir.resolve("opencode/config")

    val sessionsDir: File
        get() = context.filesDir.resolve("opencode/sessions")

    suspend fun startServer(mode: OpencodeMode = OpencodeMode.CLOUD): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                configDir.mkdirs()
                sessionsDir.mkdirs()

                status.value = status.value.copy(
                    isRunning = true,
                    mode = mode
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun stopServer() {
        status.value = OpencodeStatus()
    }

    suspend fun sendMessage(
        message: String,
        sessionId: String? = null
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("message", message)
                    put("sessionId", sessionId ?: "default")
                    put("mode", status.value.mode.name.lowercase())
                }

                val body = json.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("http://127.0.0.1:${status.value.serverPort}/api/chat")
                    .post(body)
                    .build()

                val response = client.newCall(request).execute()
                response.body?.string() ?: "No response"
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
    }

    suspend fun executeTask(task: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("task", task)
                }

                val body = json.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("http://127.0.0.1:${status.value.serverPort}/api/execute")
                    .post(body)
                    .build()

                val response = client.newCall(request).execute()
                response.body?.string() ?: "No response"
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
    }

    suspend fun checkHealth(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("http://127.0.0.1:${status.value.serverPort}/health")
                    .get()
                    .build()

                val response = client.newCall(request).execute()
                response.isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }
}
