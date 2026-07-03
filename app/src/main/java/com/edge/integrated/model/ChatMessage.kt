package com.edge.integrated.model

data class ChatMessage(
    val id: String,
    val role: Role,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelName: String? = null,
    val isStreaming: Boolean = false
) {
    enum class Role { USER, ASSISTANT, SYSTEM }
}
