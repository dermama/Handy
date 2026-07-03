package com.edge.integrated.ui.opencode

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.edge.integrated.ui.common.SectionHeader
import com.edge.integrated.ui.common.StatusIndicator

private data class TaskResult(
    val task: String,
    val result: String,
    val isSuccess: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpencodeScreen() {
    var isRunning by remember { mutableStateOf(false) }
    var taskInput by remember { mutableStateOf("") }
    val results = remember { mutableStateListOf<TaskResult>() }
    var selectedMode by remember { mutableStateOf("Cloud") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("OpenCode AI")
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusIndicator(isActive = isRunning)
                }
            },
            actions = {
                Text(
                    text = selectedMode,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Code,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Coding Agent",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "OpenCode is an AI coding agent that can write, edit, and debug code. " +
                                "It runs in the Termux environment and can connect to 75+ AI providers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (!isRunning) {
                        ElevatedButton(
                            onClick = { isRunning = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start OpenCode Server")
                        }
                    } else {
                        Button(
                            onClick = { isRunning = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Stop Server")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = taskInput,
                    onValueChange = { taskInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Describe a coding task...") },
                    shape = RoundedCornerShape(12.dp),
                    enabled = isRunning,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (taskInput.isNotBlank()) {
                            results.add(
                                TaskResult(
                                    task = taskInput,
                                    result = "[$selectedMode] OpenCode execution placeholder.\n" +
                                            "In production, this would connect to:\n" +
                                            "- Cloud: Claude/GPT/Gemini via opencode CLI\n" +
                                            "- Local: Edge Gallery LiteRT model via Termux bridge"
                                )
                            )
                            taskInput = ""
                        }
                    },
                    enabled = isRunning && taskInput.isNotBlank(),
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Execute")
                }
            }
        }

        if (results.isNotEmpty()) {
            SectionHeader(title = "Task Results")
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)
            ) {
                items(results.reversed()) { result ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Task: ${result.task}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = result.result,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
