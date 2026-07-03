package com.edge.integrated.model

data class ModelInfo(
    val id: String,
    val name: String,
    val provider: ModelProvider,
    val description: String,
    val sizeBytes: Long = 0,
    val isDownloaded: Boolean = false,
    val downloadProgress: Float = 0f,
    val isDownloading: Boolean = false,
    val huggingFaceRepo: String? = null,
    val requiresInternet: Boolean = false
)

enum class ModelProvider {
    EDGE_GALLERY_LOCAL,
    OPENCODE_CLOUD,
    HUGGING_FACE
}
