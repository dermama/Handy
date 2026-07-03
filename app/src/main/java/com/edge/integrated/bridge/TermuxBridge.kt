package com.edge.integrated.bridge

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class TermuxSession(
    val id: String,
    val isRunning: Boolean = false,
    val output: String = ""
)

@Singleton
class TermuxBridge @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sessions = MutableStateFlow<List<TermuxSession>>(emptyList())

    val sessionsFlow: Flow<List<TermuxSession>> = sessions

    val termuxHome: File
        get() = context.filesDir.resolve("termux/home")

    val termuxPrefix: File
        get() = context.filesDir.resolve("termux/usr")

    suspend fun createSession(): TermuxSession {
        val session = TermuxSession(
            id = "session_${System.currentTimeMillis()}",
            isRunning = false
        )
        sessions.value = sessions.value + session
        return session
    }

    suspend fun executeCommand(sessionId: String, command: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val process = ProcessBuilder()
                    .command("sh", "-c", command)
                    .directory(termuxHome)
                    .redirectErrorStream(true)
                    .start()

                process.inputStream.bufferedReader().readText()
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
    }

    suspend fun closeSession(sessionId: String) {
        sessions.value = sessions.value.filter { it.id != sessionId }
    }

    fun isTermuxInstalled(): Boolean {
        return termuxHome.exists() && termuxPrefix.exists()
    }

    suspend fun installBaseEnvironment(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                termuxHome.mkdirs()
                termuxPrefix.mkdirs()
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}
