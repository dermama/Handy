package com.edge.integrated.ui.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edge.integrated.model.ModelInfo
import com.edge.integrated.ui.common.EmptyState
import com.edge.integrated.ui.common.ModelCard
import com.edge.integrated.ui.common.SectionHeader
import com.edge.integrated.ui.navigation.Screen
import androidx.compose.material.icons.filled.Cloud
import com.edge.integrated.model.ModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelManagerScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val localModels = listOf(
        ModelInfo(
            id = "gemma-2b",
            name = "Gemma 2B",
            provider = ModelProvider.EDGE_GALLERY_LOCAL,
            description = "Google's lightweight LLM",
            isDownloaded = true,
            requiresInternet = false
        ),
        ModelInfo(
            id = "gemma-3n-4b",
            name = "Gemma 3n 4B",
            provider = ModelProvider.EDGE_GALLERY_LOCAL,
            description = "Multimodal LLM (text + image)",
            isDownloaded = false,
            requiresInternet = false
        ),
        ModelInfo(
            id = "function-gemma",
            name = "FunctionGemma 270M",
            provider = ModelProvider.EDGE_GALLERY_LOCAL,
            description = "Tool calling specialist",
            isDownloaded = false,
            requiresInternet = false
        )
    )

    val cloudModels = listOf(
        ModelInfo(
            id = "opencode-cloud",
            name = "OpenCode Cloud AI",
            provider = ModelProvider.OPENCODE_CLOUD,
            description = "75+ providers via OpenCode",
            requiresInternet = true
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("AI Models") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search HuggingFace models...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
        ) {
            item {
                SectionHeader(title = "Local Models (Edge Gallery)")
            }
            items(localModels) { model ->
                ModelCard(
                    name = model.name,
                    provider = "Edge Gallery",
                    isDownloaded = model.isDownloaded,
                    isDownloading = false,
                    downloadProgress = 0f,
                    onAction = { }
                )
            }

            item {
                SectionHeader(title = "Cloud Models (OpenCode)")
            }
            items(cloudModels) { model ->
                ModelCard(
                    name = model.name,
                    provider = "OpenCode AI",
                    isDownloaded = true,
                    isDownloading = false,
                    downloadProgress = 0f,
                    onAction = { }
                )
            }
        }
    }
}
