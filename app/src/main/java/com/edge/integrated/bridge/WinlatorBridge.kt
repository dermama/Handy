package com.edge.integrated.bridge

import android.content.Context
import com.edge.integrated.model.BoxPreset
import com.edge.integrated.model.ContainerInfo
import com.edge.integrated.model.ContainerStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WinlatorBridge @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val containers = MutableStateFlow<List<ContainerInfo>>(emptyList())

    val containersFlow: Flow<List<ContainerInfo>> = containers

    val containersDir: File
        get() = context.filesDir.resolve("winlator/containers")

    val wineDir: File
        get() = context.filesDir.resolve("winlator/wine")

    suspend fun createContainer(
        name: String,
        wineVersion: String = "wine-9.0",
        boxPreset: BoxPreset = BoxPreset.INTERMEDIATE
    ): ContainerInfo {
        val container = ContainerInfo(
            id = "container_${System.currentTimeMillis()}",
            name = name,
            status = ContainerStatus.STOPPED,
            wineVersion = wineVersion,
            boxPreset = boxPreset
        )
        containers.value = containers.value + container

        withContext(Dispatchers.IO) {
            val containerDir = containersDir.resolve(container.id)
            containerDir.mkdirs()
            containerDir.resolve("drive_c").mkdirs()
            containerDir.resolve("drive_c/windows").mkdirs()
            containerDir.resolve("drive_c/users").mkdirs()
        }

        return container
    }

    suspend fun launchContainer(containerId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val container = containers.value.find { it.id == containerId }
                if (container == null) return@withContext false

                updateContainerStatus(containerId, ContainerStatus.STARTING)

                val containerDir = containersDir.resolve(containerId)
                val winePrefix = containerDir.resolve("drive_c")

                val env = mapOf(
                    "WINEPREFIX" to winePrefix.absolutePath,
                    "BOX86_PRESET" to container.boxPreset.name.lowercase(),
                    "BOX64_PRESET" to container.boxPreset.name.lowercase(),
                    "DISPLAY" to ":0"
                )

                val processBuilder = ProcessBuilder(
                    wineDir.resolve(container.wineVersion)
                        .resolve("bin/wine").absolutePath,
                    "explorer.exe"
                ).apply {
                    directory(containerDir)
                    environment().putAll(env)
                    redirectErrorStream(true)
                }

                processBuilder.start()
                updateContainerStatus(containerId, ContainerStatus.RUNNING)
                true
            } catch (e: Exception) {
                updateContainerStatus(containerId, ContainerStatus.ERROR)
                false
            }
        }
    }

    suspend fun stopContainer(containerId: String) {
        updateContainerStatus(containerId, ContainerStatus.STOPPED)
    }

    suspend fun deleteContainer(containerId: String) {
        withContext(Dispatchers.IO) {
            val containerDir = containersDir.resolve(containerId)
            containerDir.deleteRecursively()
        }
        containers.value = containers.value.filter { it.id != containerId }
    }

    private fun updateContainerStatus(containerId: String, status: ContainerStatus) {
        containers.value = containers.value.map {
            if (it.id == containerId) it.copy(status = status) else it
        }
    }
}
