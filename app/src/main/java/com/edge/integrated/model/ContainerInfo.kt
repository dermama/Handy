package com.edge.integrated.model

data class ContainerInfo(
    val id: String,
    val name: String,
    val status: ContainerStatus,
    val wineVersion: String = "wine-9.0",
    val boxPreset: BoxPreset = BoxPreset.INTERMEDIATE,
    val resolution: String = "800x600",
    val dxvkEnabled: Boolean = true,
    val virglEnabled: Boolean = false
)

enum class ContainerStatus {
    STOPPED, STARTING, RUNNING, ERROR
}

enum class BoxPreset {
    LITE, INTERMEDIATE, HIGH_PERFORMANCE
}
