package com.edge.integrated.ui.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.edge.integrated.model.ChatMessage
import com.edge.integrated.ui.common.StatusIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen(
    onNavigateToModels: () -> Unit
) {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var isLocalMode by remember { mutableStateOf(true) }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("AI Chat")
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusIndicator(isActive = true)
                }
            },
            actions = {
                IconButton(onClick = {
                    isLocalMode = !isLocalMode
                }) {
                    Text(
                        text = if (isLocalMode) "Local" else "Cloud",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                IconButton(onClick = onNavigateToModels) {
                    Icon(Icons.Default.Memory, contentDescription = "Models")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        if (messages.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Start a conversation",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isLocalMode) "Using local Edge AI model"
                    else "Using cloud OpenCode AI",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    ChatBubble(message = message)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        messages.add(
                            ChatMessage(
                                id = System.currentTimeMillis().toString(),
                                role = ChatMessage.Role.USER,
                                content = inputText
                            )
                        )
                        messages.add(
                            ChatMessage(
                                id = "resp_${System.currentTimeMillis()}",
                                role = ChatMessage.Role.ASSISTANT,
                                content = if (isLocalMode)
                                    "[Local inference placeholder - uses Edge Gallery LiteRT model]"
                                else
                                    "[Cloud inference placeholder - routes via OpenCode server]",
                                modelName = if (isLocalMode) "Edge Local" else "OpenCode Cloud"
                            )
                        )
                        inputText = ""
                    }
                },
                enabled = inputText.isNotBlank()
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isUser = message.role == ChatMessage.Role.USER
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val color = if (isUser)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = color)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (message.modelName != null) {
                    Text(
                        text = message.modelName,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isUser)
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = if (isUser)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
