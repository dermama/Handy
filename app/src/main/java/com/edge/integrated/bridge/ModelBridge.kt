package com.edge.integrated.bridge

import android.content.Context
import com.edge.integrated.model.ModelInfo
import com.edge.integrated.model.ModelProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelBridge @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val models = MutableStateFlow<List<ModelInfo>>(emptyList())

    val modelsFlow: Flow<List<ModelInfo>> = models

    val modelsDir: File
        get() = context.filesDir.resolve("models")

    init {
        modelsDir.mkdirs()
        loadDefaultModels()
    }

    private fun loadDefaultModels() {
        val defaultModels = listOf(
            ModelInfo(
                id = "gemma-2b",
                name = "Gemma 2B",
                provider = ModelProvider.EDGE_GALLERY_LOCAL,
                description = "Google's lightweight LLM for on-device inference",
                huggingFaceRepo = "google/gemma-2b-it-litert-lm",
                requiresInternet = false
            ),
            ModelInfo(
                id = "gemma-3n-4b",
                name = "Gemma 3n 4B",
                provider = ModelProvider.EDGE_GALLERY_LOCAL,
                description = "Multimodal LLM supporting text and image inputs",
                huggingFaceRepo = "google/gemma-3n-E2B-it-litert-lm",
                requiresInternet = false
            ),
            ModelInfo(
                id = "function-gemma",
                name = "FunctionGemma 270M",
                provider = ModelProvider.EDGE_GALLERY_LOCAL,
                description = "Specialized for tool calling and function execution",
                huggingFaceRepo = "google/function-gemma-270m-litert-lm",
                requiresInternet = false
            ),
            ModelInfo(
                id = "opencode-cloud",
                name = "OpenCode Cloud AI",
                provider = ModelProvider.OPENCODE_CLOUD,
                description = "Cloud AI via OpenCode (75+ providers)",
                requiresInternet = true
            ),
            ModelInfo(
                id = "llama-3.2-1b",
                name = "Llama 3.2 1B",
                provider = ModelProvider.HUGGING_FACE,
                description = "Meta's efficient on-device model",
                huggingFaceRepo = "meta-llama/Llama-3.2-1B-litert-lm",
                requiresInternet = false
            ),
            ModelInfo(
                id = "phi-3-mini",
                name = "Phi-3 Mini",
                provider = ModelProvider.HUGGING_FACE,
                description = "Microsoft's capable small language model",
                huggingFaceRepo = "microsoft/Phi-3-mini-4k-instruct-litert-lm",
                requiresInternet = false
            )
        )

        val downloadedModels = getDownloadedModels()
        models.value = defaultModels.map { model ->
            model.copy(isDownloaded = downloadedModels.contains(model.id))
        }
    }

    private fun getDownloadedModels(): Set<String> {
        return modelsDir.listFiles()
            ?.filter { it.isDirectory }
            ?.map { it.name }
            ?.toSet() ?: emptySet()
    }

    suspend fun downloadModel(modelId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val model = models.value.find { it.id == modelId } ?: return@withContext false

                models.value = models.value.map {
                    if (it.id == modelId) it.copy(isDownloading = true, downloadProgress = 0f) else it
                }

                val modelDir = modelsDir.resolve(modelId)
                modelDir.mkdirs()

                if (model.huggingFaceRepo != null) {
                    downloadFromHuggingFace(model.huggingFaceRepo, modelDir)
                }

                models.value = models.value.map {
                    if (it.id == modelId) it.copy(
                        isDownloading = false,
                        isDownloaded = true,
                        downloadProgress = 1f
                    ) else it
                }
                true
            } catch (e: Exception) {
                models.value = models.value.map {
                    if (it.id == modelId) it.copy(
                        isDownloading = false,
                        downloadProgress = 0f
                    ) else it
                }
                false
            }
        }
    }

    private fun downloadFromHuggingFace(repo: String, targetDir: File) {
        val modelFileUrl = "https://huggingface.co/$repo/resolve/main/model.tflite"
        val url = URL(modelFileUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 30000

        connection.inputStream.use { input ->
            targetDir.resolve("model.tflite").outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    suspend fun deleteModel(modelId: String) {
        withContext(Dispatchers.IO) {
            val modelDir = modelsDir.resolve(modelId)
            if (modelDir.exists()) {
                modelDir.deleteRecursively()
            }
        }
        models.value = models.value.map {
            if (it.id == modelId) it.copy(isDownloaded = false, downloadProgress = 0f) else it
        }
    }

    suspend fun searchHuggingFace(query: String): List<ModelInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://huggingface.co/api/models?search=$query&filter=litert-lm&sort=downloads&direction=-1&limit=20")
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 10000

                val response = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(response)

                (0 until jsonArray.length()).map { i ->
                    val item = jsonArray.getJSONObject(i)
                    ModelInfo(
                        id = item.getString("modelId"),
                        name = item.optString("modelId", "Unknown"),
                        provider = ModelProvider.HUGGING_FACE,
                        description = item.optString("description", ""),
                        huggingFaceRepo = item.getString("modelId"),
                        requiresInternet = false
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun runInference(
        modelId: String,
        input: String
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                val modelDir = modelsDir.resolve(modelId)
                val modelFile = modelDir.resolve("model.tflite")

                if (!modelFile.exists()) {
                    return@withContext "Model not downloaded"
                }

                "Inference with $modelId: [placeholder - requires LiteRT runtime]"
            } catch (e: Exception) {
                "Inference error: ${e.message}"
            }
        }
    }
}
